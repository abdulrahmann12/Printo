package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class AttributeNotFoundException extends RuntimeException{

	public AttributeNotFoundException() {
		super(Messages.ATTRIBUTE_NOT_FOUND);
	}
	
}