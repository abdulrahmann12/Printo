package com.team.printo.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.team.printo.dto.Messages;
import com.team.printo.dto.OrderDTO;
import com.team.printo.exception.OrderNotFoundException;
import com.team.printo.exception.UserNotFoundException;
import com.team.printo.model.Order;
import com.team.printo.model.User;
import com.team.printo.repository.OrderRepository;
import com.team.printo.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public PdfGeneratorService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("XHTML");
        resolver.setCharacterEncoding("UTF-8");

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    public byte[] generatePdf(OrderDTO orderDTO) throws Exception {
    	
	    Order order = orderRepository.findById(orderDTO.getId())
	            .orElseThrow(() -> new OrderNotFoundException());
	    
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();
	    User currentUser = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException());
	    
	    if(order.getUser().getId() != currentUser.getId()) {
	    	throw new IllegalStateException(Messages.ORDER_NOT_BELONG_TO_USER);
	    }
    	
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
