package com.team.printo.exception;

public class InvalidResetCodeException extends RuntimeException {
    public InvalidResetCodeException() {
        super("Invalid reset code.");
    }
}