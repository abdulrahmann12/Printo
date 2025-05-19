package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class InvalidConfirmationCodeException extends RuntimeException {
    public InvalidConfirmationCodeException() {
        super(Messages.INVAILED_CONFIRM_EMAIL);
    }
}