package com.team.printo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.team.printo.model.Payment.PaymentMethod;
import com.team.printo.model.Payment.PaymentStatus;

import lombok.Data;

@Data
public class PaymentDTO {

    private Long id;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

    private BigDecimal amount;

    private Long orderId;
}
