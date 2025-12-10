package com.helpdesk.controller.exception;

public class AdminNotFoundException extends BaseException {
    public AdminNotFoundException(Long adminId) {
        super("Admin with ID " + adminId + " not found.");
    }
}
