package com.team.printo.service;

import com.team.printo.model.Order;
import com.team.printo.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        sendEmail(
                order.getUser().getEmail(),
                subject,
                "emails/order-confirmation",
                new ContextBuilder()
                        .add("name", order.getUser().getFirstName())
                        .add("orderId", order.getId())
                        .build()
        );
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
