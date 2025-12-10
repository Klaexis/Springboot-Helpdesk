package com.helpdesk.controller.exception;

public class EmployeeNotAuthorizedException extends BaseException {
    public EmployeeNotAuthorizedException(Long adminId) {
        super("Unauthorized access: Employee with ID " + adminId + " is not an admin.");
    }
}
