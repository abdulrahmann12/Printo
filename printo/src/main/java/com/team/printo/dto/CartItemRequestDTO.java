package com.team.printo.dto;

import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CartItemRequestDTO {

	
	private Long productId;
	
	
	private Long designId;
	
    @PositiveOrZero(message = "Quantity must be positive or zero")
	private int quantity;
    
    private List<CartItemAttributeValueRequestDTO> attributeValuesId;
    
}
