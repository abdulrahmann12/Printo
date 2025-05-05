package com.team.printo.dto;

import lombok.Data;

@Data

public class EmailConfirmationRequest {

	private String email;
	private String confirmationCode;
}