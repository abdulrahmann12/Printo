package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class CategoryNotFoundException extends RuntimeException{

	public CategoryNotFoundException() {
		super(Messages.CATEGORY_NOT_FOUND);
	}
	
}