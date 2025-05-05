package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductTemplateDTO {

	private Long id;
	
    @NotBlank(message = "Image is required")
    private String image;

    private Long productId;
}
