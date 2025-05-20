package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException() {
		super(Messages.USER_NOT_FOUND);
	}
	
}