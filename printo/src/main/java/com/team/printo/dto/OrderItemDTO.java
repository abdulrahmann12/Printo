package com.team.printo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDTO {

	private long id;
	
	private long productId;
	
	private long orderId;
	
	private Long designId;
	
	@Positive
	private int quantity;
	
	@Positive
	private BigDecimal price;
}
