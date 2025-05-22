package com.team.printo.dto;

import com.team.printo.model.Order.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusDTO {
	
	@NotNull(message = "Status is required")
    private OrderStatus status;
	
}
