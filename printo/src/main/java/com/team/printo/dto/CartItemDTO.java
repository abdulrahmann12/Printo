package com.team.printo.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemDTO {

	private long id;
	
	private long productId;
	
	private long cartId;
	
	private Long designId;
	
    @Positive(message = "Quantity must be positive")
	private int quantity;
}
