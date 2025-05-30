package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttributeDTO {

	private Long id;
	
    @NotBlank(message = "Attribute name is required")
	private String name;
	
	private Long categoryId;
	
	private String category;
}
