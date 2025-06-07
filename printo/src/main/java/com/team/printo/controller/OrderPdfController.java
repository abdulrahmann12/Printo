package com.team.printo.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.OrderDTO;
import com.team.printo.service.OrderService;
import com.team.printo.service.PdfGeneratorService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Invoice Controller", description = "API for generating and viewing invoices in PDF format.")
@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class OrderPdfController {

	
    private final OrderService orderService; 
    private final PdfGeneratorService pdfGenerator;

    @GetMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long orderId) throws Exception {
        OrderDTO orderDTO = orderService.getOrder(orderId); 

        byte[] pdfBytes = pdfGenerator.generatePdf(orderDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("invoice-" + orderId + ".pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
