package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmploymentStatus;
import com.helpdesk.service.EmployeeValidationService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeValidationServiceImpl implements EmployeeValidationService {

    public boolean isAdmin(Employee employee) {
        return employee.getEmployeePosition() != null && employee.getEmployeePosition().getPositionTitle().equals("Admin");
    }

    public boolean isActive(Employee employee) {
        return employee.getEmploymentStatus().equals(EmploymentStatus.ACTIVE);
    }

    public void validateAdmin(Employee employee) {
        if (!isAdmin(employee)) {
            throw new RuntimeException("Unauthorized access: Employee is not an admin.");
        }
    }

    public void validateActive(Employee employee) {
        if (!isActive(employee)) {
            throw new RuntimeException("Employee is not active. Cannot proceed.");
        }
    }
}
