package com.helpdesk.controller.exception;

public class InvalidEmployeeStatusException extends BaseException {
    public InvalidEmployeeStatusException(String message) {
        super(message);
    }
}
