package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET /api/admin/employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // GET /api/admin/employees/{id}
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.findEmployee(id);
    }

    // POST /api/admin/employees
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    // PUT /api/admin/employees/{id}
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    // DELETE /api/admin/employees/{id}
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    // PUT /api/admin/employees/id/assign-position?title=positionTitle
    @PutMapping("/{id}/assign-position")
    public Employee assignPosition(
            @PathVariable Long id,
            @RequestParam String title) {
        return employeeService.assignPositionToEmployee(id, title);
    }
}