package com.team.printo.dto;

import com.team.printo.model.User;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequest {
	@NotNull(message = "Role is required")
    private User.Role role;
}
	