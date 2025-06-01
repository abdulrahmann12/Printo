package com.team.printo.dto;

import com.team.printo.model.Payment.PaymentStatus;

import lombok.Data;

@Data
public class PaymentStatusUpdateDTO {
    private Long paymentId;
    private PaymentStatus paymentStatus;
}
