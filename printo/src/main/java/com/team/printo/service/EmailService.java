package com.team.printo.service;

import com.team.printo.dto.OrderDTO;
import com.team.printo.mapper.OrderMapper;
import com.team.printo.model.Order;
import com.team.printo.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PdfGeneratorService pdfGenerator;
    private final OrderMapper orderMapper;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendCode(User user, String subject) {
        sendEmail(
                user.getEmail(),
                subject,
                "emails/confirmation", // template path inside /resources/templates
                new ContextBuilder()
                        .add("name", user.getFirstName())
                        .add("code", user.getConfirmationCode())
                        .build()
        );
    }

    public void sendOrderConfirmationEmail(Order order, String subject) {
        try {
        	
            OrderDTO orderDTO = orderMapper.toDTO(order); 

            // Generate PDF
            byte[] pdfBytes = pdfGenerator.generatePdf(orderDTO);

            // Build context
            Context context = new Context();
            context.setVariable("name", order.getUser().getFirstName());
            context.setVariable("orderId", order.getId());

            // Send email with attachment
            sendEmailWithAttachment(
                order.getUser().getEmail(),
                subject,
                "emails/order-confirmation",
                context,
                pdfBytes,
                "invoice-" + order.getId() + ".pdf"
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send confirmation email with invoice", e);
        }
    }


    public void sendOrderStatusUpdateEmail(Order order, String subject) {
        sendEmail(
                order.getUser().getEmail(),
                subject,
                "emails/status-update",
                new ContextBuilder()
                        .add("name", order.getUser().getFirstName())
                        .add("orderId", order.getId())
                        .add("status", order.getStatus().name())
                        .build()
        );
    }

    private void sendEmail(String to, String subject, String templatePath, Context context) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = templateEngine.process(templatePath, context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // اطبع الخطأ
            throw new RuntimeException("Failed to send email", e);
        }
    }

    
    private void sendEmailWithAttachment(
            String to,
            String subject,
            String templatePath,
            Context context,
            byte[] attachmentBytes,
            String attachmentFilename
    ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = templateEngine.process(templatePath, context);
            helper.setText(htmlContent, true);

            // Attach the PDF
            ByteArrayResource pdfAttachment = new ByteArrayResource(attachmentBytes);
            helper.addAttachment(attachmentFilename, pdfAttachment);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }

    // Nested helper class to make adding context variables easier
    private static class ContextBuilder {
        private final Context context = new Context();

        public ContextBuilder add(String key, Object value) {
            context.setVariable(key, value);
            return this;
        }

        public Context build() {
            return context;
        }
    }
}
