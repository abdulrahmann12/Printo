package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class AttributeValueNotFoundException extends RuntimeException{

	public AttributeValueNotFoundException() {
		super(Messages.ATTRIBUTE_VALUE_NOT_FOUND);
	}
	
}