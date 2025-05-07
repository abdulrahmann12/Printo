package com.team.printo.dto;

import lombok.Data;

@Data
public class ResetPasswodDTO {

	private String email;
	private String code;
	private String newPassword;
}
