package com.team.printo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderDTO {

	private long id;
	

    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private LocalDateTime createdAt;

    @NotBlank(message = "Status is required")
    private String status;

    private Long userId;

    private Long addressId;

    private List<OrderItemDTO> orderItems;
}
