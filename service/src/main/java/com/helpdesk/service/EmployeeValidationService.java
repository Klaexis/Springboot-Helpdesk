package com.helpdesk.service;

import com.helpdesk.model.Employee;

public interface EmployeeValidationService {
    boolean isAdmin(Employee employee);

    boolean isActive(Employee employee);

    boolean isAdminAndActive(Employee employee);

    void validateAdmin(Employee employee);

    void validateActive(Employee employee);
}
