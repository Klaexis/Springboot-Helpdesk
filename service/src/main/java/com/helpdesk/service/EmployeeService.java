package com.helpdesk.service;

import com.helpdesk.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee findEmployee(Long id);

    List<Employee> getAllEmployees();

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Long id, Employee newData);

    void deleteEmployee(Long id);

    Employee assignPositionToEmployee(Long employeeId, String positionTitle);
}
