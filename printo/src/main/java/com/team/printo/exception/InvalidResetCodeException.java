package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class InvalidResetCodeException extends RuntimeException {
    public InvalidResetCodeException() {
        super(Messages.INVALID_RESET_CODE);
    }
}