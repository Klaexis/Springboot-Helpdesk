package com.helpdesk.controller.exception;

public class EmployeePositionNotFoundException extends BaseException {
    public EmployeePositionNotFoundException(Long positionId) {
        super("Employee position with ID " + positionId + " not found.");
    }

    public EmployeePositionNotFoundException(String message) {
        super(message);
    }
}