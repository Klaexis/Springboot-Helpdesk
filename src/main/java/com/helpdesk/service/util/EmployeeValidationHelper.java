package com.helpdesk.service.util;

import com.helpdesk.controller.exception.EmployeeNotActiveException;
import com.helpdesk.controller.exception.EmployeeNotAuthorizedException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.EmploymentStatus;

import org.springframework.stereotype.Service;

@Service
public class EmployeeValidationHelper {

    public boolean isAdmin(Employee employee) {
        return employee.getEmployeePosition() != null && employee.getEmployeePosition().getPositionTitle().equals("Admin");
    }

    public boolean isActive(Employee employee) {
        return employee.getEmploymentStatus().equals(EmploymentStatus.ACTIVE);
    }

    public void validateAdmin(Employee employee) {
        if (!isAdmin(employee)) {
            throw new EmployeeNotAuthorizedException(employee.getEmployeeId());
        }
    }

    public void validateActive(Employee employee) {
        if (!isActive(employee)) {
            throw new EmployeeNotActiveException(employee.getEmployeeId());
        }
    }
}
