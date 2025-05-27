package com.team.printo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDTO {

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Code is required")
	private String code;
	
	@NotBlank(message = "Passwo	rd is required")
	@Size(min = 6, message = "Password too short")
	private String newPassword;
}
