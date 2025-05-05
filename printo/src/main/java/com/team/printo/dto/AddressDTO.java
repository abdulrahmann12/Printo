package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO {
	
	private Long id;
	
	
	private Long userId;
	
	@NotBlank(message = "Street name is required")
	private String street;
	
	@NotBlank(message = "City name is required")
	private String city;
	
	@NotBlank(message = "Country name is required")
	private String country;
	
	private boolean isDefault;
}
