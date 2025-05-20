package com.team.printo.exception;

import com.team.printo.dto.Messages;

public class MailSendingException extends RuntimeException {
    public MailSendingException() {
        super(Messages.FAILED_EMAIL);
    }
}