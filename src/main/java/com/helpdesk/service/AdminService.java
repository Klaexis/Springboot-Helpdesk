package com.helpdesk.service;

import com.helpdesk.model.Employee;
import com.helpdesk.model.request.AdminRequestDTO;

import java.util.List;

public interface AdminService {
    Employee findEmployee(Long adminId, Long employeeId);

    List<Employee> getAllEmployees(Long adminId);

    Employee createEmployee(Long adminId, AdminRequestDTO dto);

    Employee updateEmployee(Long adminId, Long employeeId, AdminRequestDTO dto);

    void deleteEmployee(Long adminId, Long employeeId);

    Employee assignPositionToEmployee(Long adminId, Long employeeId, String positionTitle);
}
