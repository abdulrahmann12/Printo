package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}