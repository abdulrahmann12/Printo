package com.team.printo.exception;


public class EmailAlreadyExistsException extends RuntimeException{

	public EmailAlreadyExistsException() {
		super("Email is Already Exist");
	}
	
}