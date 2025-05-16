package com.team.printo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewDTO {
	
	private Long id;
	
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
	private int rating;
	
    @NotBlank(message = "Comment is required")
	private String comment;
	
	private String userName;
	
	private Long productId;
}
