package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class ImageNotFoundException extends RuntimeException{

	public ImageNotFoundException() {
		super(Messages.IMAGE_NOT_FOUND);
	}
	
}