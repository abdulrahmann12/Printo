package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class ReviewNotFoundException extends RuntimeException{

	public ReviewNotFoundException() {
		super(Messages.REVIEW_NOT_FOUND);
	}
	
}