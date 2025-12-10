package com.helpdesk.controller.exception;

public class EmployeeNotActiveException extends BaseException {
    public EmployeeNotActiveException(Long employeeId) {
        super("Employee with ID " + employeeId + " is not active. Cannot proceed.");
    }
}
