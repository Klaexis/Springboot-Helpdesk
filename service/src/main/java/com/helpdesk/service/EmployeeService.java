package com.helpdesk.service;

import com.helpdesk.model.Employee;
import com.helpdesk.repository.EmployeeRepository;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee findEmployee(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee newData) {
        Employee emp = findEmployee(id);

        emp.setEmployeeName(newData.getEmployeeName());
        emp.setEmployeeAge(newData.getEmployeeAge());
        emp.setEmployeeAddress(newData.getEmployeeAddress());
        emp.setEmployeeContactNumber(newData.getEmployeeContactNumber());
        emp.setEmployeeEmail(newData.getEmployeeEmail());
        emp.setEmployeePosition(newData.getEmployeePosition());
        emp.setEmploymentStatus(newData.getEmploymentStatus());

        return repository.save(emp);
    }

    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}
