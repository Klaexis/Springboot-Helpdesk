package com.helpdesk.controller.exception;

public class InvalidTicketStatusException extends BaseException {
    public InvalidTicketStatusException(String message) {
        super(message);
    }
}
