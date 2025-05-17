package com.team.printo.dto;

import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDTO {

	private long id;
	
	private Long productId;
	
	private long cartId;
	
	private Long designId;
	
    @Positive(message = "Quantity must be positive")
	private int quantity;
    
    private List<CartItemAttributeValueRequestDTO> attributeValuesId;
    
}
