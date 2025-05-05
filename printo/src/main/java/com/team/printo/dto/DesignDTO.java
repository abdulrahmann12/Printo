package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DesignDTO {

	private Long id;
	
    @NotBlank(message = "Image is required")
    private String image;

    private Long userId;

    private Long productId;
}
