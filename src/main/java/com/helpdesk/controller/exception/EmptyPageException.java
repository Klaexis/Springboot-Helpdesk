package com.helpdesk.controller.exception;

public class EmptyPageException extends RuntimeException {
    public EmptyPageException(String message) {
        super(message);
    }
}
