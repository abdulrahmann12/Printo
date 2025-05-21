package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class CartNotFoundException extends RuntimeException{

	public CartNotFoundException() {
		super(Messages.CART_NOT_FOUND);
	}
	
}