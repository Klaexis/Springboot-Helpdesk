package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeServiceController {
    private final EmployeeService employeeService;

    public EmployeeServiceController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET /employee/profile/{employeeId}
    @GetMapping("/profile/{employeeId}")
    public Employee viewProfile(@PathVariable  Long employeeId) {
        return employeeService.viewProfile(employeeId);
    }
}
