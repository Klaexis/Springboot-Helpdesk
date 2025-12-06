package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET /employee/profile/{employeeId}
    @GetMapping("/profile/{employeeId}")
    public ResponseEntity<Employee> viewProfile(@PathVariable  Long employeeId) {
        Employee employee = employeeService.viewProfile(
                employeeId
        );
        return ResponseEntity.ok(employee);
    }
}
