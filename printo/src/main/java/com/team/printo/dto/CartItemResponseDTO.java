package com.team.printo.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartItemResponseDTO {

	private long id;
	
	private String productName;
	
	private String productImage;
	
	private long cartId;
	
	private String design;
	
	private int quantity;
    
    private List<CartItemAttributeValueResponseDTO> attributeValues;
    
}
