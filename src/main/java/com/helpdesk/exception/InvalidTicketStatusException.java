package com.helpdesk.exception;

public class InvalidTicketStatusException extends BaseException {
    public InvalidTicketStatusException(String message) {
        super(message);
    }
}
