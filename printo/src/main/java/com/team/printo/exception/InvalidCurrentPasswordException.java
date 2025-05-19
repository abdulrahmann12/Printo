package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class InvalidCurrentPasswordException extends RuntimeException {
    public InvalidCurrentPasswordException() {
        super(Messages.INVALID_PASSWORD);
    }
}