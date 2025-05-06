package com.team.printo.exception;

public class InsufficientStockException extends RuntimeException{

	public InsufficientStockException(String message) {
		super(message);
	}
	
}