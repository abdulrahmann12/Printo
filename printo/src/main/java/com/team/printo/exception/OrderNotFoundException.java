package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class OrderNotFoundException extends RuntimeException{

	public OrderNotFoundException() {
		super(Messages.ORDER_NOT_FOUND);
	}
	
}