package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttributeValueDTO {

	private Long productId;
	
    @NotNull(message = "Attribute ID is required")
	private Long attributeId;
	
    @NotBlank(message = "Value is required")
	private String value;
	
	private boolean available; 
}
