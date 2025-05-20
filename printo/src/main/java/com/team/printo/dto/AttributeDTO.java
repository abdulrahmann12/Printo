package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttributeDTO {

	private Long id;
	
    @NotBlank(message = "Name is required")
	private String name;
	
	private Long categoryId;
	
}
