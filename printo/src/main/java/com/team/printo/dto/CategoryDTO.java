package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {

	private Long id;
	
    @NotBlank(message = "Name is required")
	private String name;
	
	private String iamge;
	
	private Long parentId;
}
