package com.team.printo.dto;

import java.math.BigDecimal;

import com.team.printo.model.Payment.PaymentMethod;

import lombok.Data;

@Data
public class PaymentCreateDTO {
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private Long orderId;
}
