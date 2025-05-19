package com.team.printo.exception;

public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException(String message) {
		super("User not found for email: "+ message);
	}
	
}