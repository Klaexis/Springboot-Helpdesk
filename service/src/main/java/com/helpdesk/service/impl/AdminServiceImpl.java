package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.AdminService;
import com.helpdesk.service.util.EmployeeValidationHelper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeePositionRepository positionRepository;

    @Autowired
    private final EmployeeValidationHelper employeeValidationHelper;

    public AdminServiceImpl(EmployeeRepository employeeRepository,
                            EmployeePositionRepository positionRepository,
                            EmployeeValidationHelper employeeValidationHelper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    public Employee findEmployee(Long adminId, Long employeeId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getAllEmployees(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return employeeRepository.findAll();
    }

    public Employee createEmployee(Long adminId, Employee employee) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        EmployeePosition employeePosition = positionRepository.findByPositionTitle(employee.getEmployeePosition().getPositionTitle());

        if (employeePosition == null) {
            throw new RuntimeException("Employee position not found");
        }

        employee.setEmployeePosition(employeePosition);  // Assign the position to the employee
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long adminId, Long employeeId, Employee newData) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Optional.ofNullable(newData.getEmployeeName()).ifPresent(employee::setEmployeeName);
        Optional.ofNullable(newData.getEmployeeAge()).ifPresent(employee::setEmployeeAge);
        Optional.ofNullable(newData.getEmployeeAddress()).ifPresent(employee::setEmployeeAddress);
        Optional.ofNullable(newData.getEmployeeContactNumber()).ifPresent(employee::setEmployeeContactNumber);
        Optional.ofNullable(newData.getEmployeeEmail()).ifPresent(employee::setEmployeeEmail);
        Optional.ofNullable(newData.getEmploymentStatus()).ifPresent(employee::setEmploymentStatus);

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long adminId, Long employeeId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        employeeRepository.deleteById(employeeId);
    }

    public Employee assignPositionToEmployee(Long adminId, Long employeeId, String positionTitle) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeePosition position = positionRepository.findByPositionTitle(positionTitle);

        if (position == null) {
            throw new RuntimeException("Position '" + positionTitle + "' not found");
        }

        employee.setEmployeePosition(position);
        return employeeRepository.save(employee);
    }
}
