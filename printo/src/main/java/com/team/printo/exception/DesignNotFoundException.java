package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class DesignNotFoundException extends RuntimeException{

	public DesignNotFoundException() {
		super(Messages.DESIGN_NOT_FOUND);
	}
	
}