package com.team.printo.config;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.team.printo.dto.OrderDTO;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;

@Component
public class PdfGenerator {

    private TemplateEngine templateEngine;

    public PdfGenerator() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/"); 
        resolver.setSuffix(".html");
        resolver.setTemplateMode("XHTML");
        resolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    public byte[] generatePdf(OrderDTO orderDTO) throws Exception {
        Context context = new Context();
        context.setVariable("order", orderDTO);
        String htmlContent = templateEngine.process("invoice", context);    
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        }
    }
   
}
