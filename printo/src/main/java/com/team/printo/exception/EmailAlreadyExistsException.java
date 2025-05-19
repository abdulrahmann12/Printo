package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class EmailAlreadyExistsException extends RuntimeException{

	public EmailAlreadyExistsException() {
		super(Messages.EMAIL_EXIST);
	}
	
}