package com.team.printo.exception;

public class AuthenticationCredentialsNotFoundException extends RuntimeException{

	public AuthenticationCredentialsNotFoundException(String message) {
		super(message);
	}
	
}