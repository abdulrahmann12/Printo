package com.team.printo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String currentPassword;
    
    @Size(min = 6, message = "Password too short")
    @NotBlank(message = "Password is required")
    private String newPassword;
}