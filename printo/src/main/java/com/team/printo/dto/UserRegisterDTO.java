package com.team.printo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

	private Long id;
	
	@NotBlank(message = "First name is required")
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	private String lastName;
	
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Phone is required")
	private String phone;
	
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password too short")
	private String password;
	
}
