package com.team.printo.dto;

import lombok.Data;

@Data
public class CartItemAttributeValueResponseDTO {
    private Long id;
    
    private Long cartItemId;

    private Long attributeValueId;

    private String attributeName;

    private String attributeValue;
    
}