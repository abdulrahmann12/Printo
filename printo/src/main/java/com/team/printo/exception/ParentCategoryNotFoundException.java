package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class ParentCategoryNotFoundException extends RuntimeException{

	public ParentCategoryNotFoundException() {
		super(Messages.PARENT_CATEGORY_NOT_FOUND);
	}
	
}