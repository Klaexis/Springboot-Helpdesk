package com.helpdesk.service;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeePositionRepository positionRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeePositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
    }

    public Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        EmployeePosition employeePosition = positionRepository.findByPositionTitle(employee.getEmployeePosition().getPositionTitle());

        if (employeePosition == null) {
            throw new RuntimeException("Employee position not found");
        }

        employee.setEmployeePosition(employeePosition);  // Assign the position to the employee
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee newData) {
        Employee emp = findEmployee(id);

        Optional.ofNullable(newData.getEmployeeName()).ifPresent(emp::setEmployeeName);
        Optional.ofNullable(newData.getEmployeeAge()).ifPresent(emp::setEmployeeAge);
        Optional.ofNullable(newData.getEmployeeAddress()).ifPresent(emp::setEmployeeAddress);
        Optional.ofNullable(newData.getEmployeeContactNumber()).ifPresent(emp::setEmployeeContactNumber);
        Optional.ofNullable(newData.getEmployeeEmail()).ifPresent(emp::setEmployeeEmail);
        Optional.ofNullable(newData.getEmploymentStatus()).ifPresent(emp::setEmploymentStatus);

        if (newData.getEmployeePosition() != null) {
            String positionTitle = newData.getEmployeePosition().getPositionTitle();
            EmployeePosition employeePosition = positionRepository.findByPositionTitle(positionTitle);

            if (employeePosition == null) {
                throw new RuntimeException("Employee position not found");
            }

            emp.setEmployeePosition(employeePosition);
        }

        return employeeRepository.save(emp);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
