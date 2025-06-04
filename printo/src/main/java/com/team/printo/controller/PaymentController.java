package com.team.printo.controller;

import com.team.printo.dto.PaymentCreateDTO;
import com.team.printo.dto.PaymentDTO;
import com.team.printo.dto.PaymentStatusUpdateDTO;
import com.team.printo.model.User;
import com.team.printo.service.PaymentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment Controller", description = "API for managing payment operations, including creating and tracking payments.")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentDTO> createPayment(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PaymentCreateDTO paymentCreateDTO) {
		Long userId = ((User) userDetails).getId();
    	PaymentDTO paymentDTO = paymentService.createPayment(userId, paymentCreateDTO);
        return ResponseEntity.ok(paymentDTO);
    }

    @PutMapping("/status")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@Valid @RequestBody PaymentStatusUpdateDTO paymentStatusUpdateDTO) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(paymentStatusUpdateDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentDTO> getPaymentById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long paymentId) {
    	Long userId = ((User) userDetails).getId();
        PaymentDTO paymentDTO = paymentService.getPaymentById(userId, paymentId);
        return ResponseEntity.ok(paymentDTO);
    }
}
