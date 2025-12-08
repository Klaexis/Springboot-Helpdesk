package com.helpdesk.controller;

import com.helpdesk.model.response.EmployeeProfileResponseDTO;
import com.helpdesk.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/{currentEmployee}")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET /employee/{currentEmployee}/profile
    @GetMapping("/profile")
    public ResponseEntity<EmployeeProfileResponseDTO> viewOwnProfile(@PathVariable Long currentEmployee) {
        // Ensure employee can only view their own profile
        return ResponseEntity.ok(employeeService.viewOwnProfile(currentEmployee));
    }
}
