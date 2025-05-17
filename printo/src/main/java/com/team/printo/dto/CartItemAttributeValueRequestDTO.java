package com.team.printo.dto;

import lombok.Data;

@Data
public class CartItemAttributeValueRequestDTO {
    private Long id;
    
    private Long cartItemId;

    private Long attributeValueId;
    
}