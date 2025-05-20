package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class TemplateNotFoundException extends RuntimeException{

	public TemplateNotFoundException() {
		super(Messages.TEMPLATE_NOT_FOUND);
	}
	
}