package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class ProductNotFoundException extends RuntimeException{

	public ProductNotFoundException() {
		super(Messages.PRODUCT_NOT_FOUND);
	}
	
}