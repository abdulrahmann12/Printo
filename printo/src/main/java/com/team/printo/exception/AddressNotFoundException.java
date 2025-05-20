package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class AddressNotFoundException extends RuntimeException{

	public AddressNotFoundException() {
		super(Messages.ADDRESS_NOT_FOUND);
	}
	
}