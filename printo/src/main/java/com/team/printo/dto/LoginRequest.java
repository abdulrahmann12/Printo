package com.team.printo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@Email(message = "Invalid email format")
    @NotBlank(message = "email is required")
	private String email;
    
    @NotBlank(message = "password is required")
    private String password;
}