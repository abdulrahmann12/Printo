package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class InvalidResetCodeException extends RuntimeException {
    public InvalidResetCodeException() {
        super(Messages.INVAILD_RESET_CODE);
    }
}