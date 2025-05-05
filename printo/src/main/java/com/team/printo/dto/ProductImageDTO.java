package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductImageDTO {

	private Long id;
	
	private Long productId;
	
    @NotBlank(message = "Image URL is required")
	private String image;
}
