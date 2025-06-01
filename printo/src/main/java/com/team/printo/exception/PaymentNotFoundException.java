package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class PaymentNotFoundException extends RuntimeException{

	public PaymentNotFoundException() {
		super(Messages.PAYMENT_NOT_FOUND);
	}
	
}