package com.team.printo.dto;

import lombok.Data;

@Data
public class OrderItemAttributeValueResponseDTO {
    private Long id;
    
    private Long orderItemId;

    private Long attributeValueId;

    private String attributeName;

    private String attributeValue;   
}