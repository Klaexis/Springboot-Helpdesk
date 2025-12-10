package com.helpdesk.exception;

public class EmployeeNotFoundException extends BaseException {
    public EmployeeNotFoundException(Long employeeId) {
        super("Employee with ID " + employeeId + " not found.");
    }
}