package com.team.printo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

	private Long id;
	
	@NotBlank(message = "First name is required")
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	private String lastName;
	
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email name is required")
	private String email;
	
	@NotBlank(message = "Phone name is required")
	private String phone;
	
	
	private String image;
	
	private String role;
	
}
