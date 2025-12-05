package com.helpdesk.service;

import com.helpdesk.model.Employee;

import java.util.List;

public interface AdminService {
    Employee findEmployee(Long adminId, Long employeeId);

    List<Employee> getAllEmployees(Long adminId);

    Employee createEmployee(Long adminId, Employee employee);

    Employee updateEmployee(Long adminId, Long employeeId, Employee newData);

    void deleteEmployee(Long adminId, Long employeeId);

    Employee assignPositionToEmployee(Long adminId, Long employeeId, String positionTitle);
}
