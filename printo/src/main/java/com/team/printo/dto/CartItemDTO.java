package com.team.printo.dto;

import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemDTO {

	private long id;
	
	private String productName;
	
	private String productImage;
	
	private long cartId;
	
	private String design;
	
    @Positive(message = "Quantity must be positive")
	private int quantity;
    
    private List<CartItemAttributeValueDTO> attributeValues;
}
