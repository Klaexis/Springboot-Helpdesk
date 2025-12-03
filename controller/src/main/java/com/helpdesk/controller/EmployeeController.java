package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.service.impl.EmployeeServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeServiceImpl = employeeService;
    }

    // GET /api/admin/employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeServiceImpl.getAllEmployees();
    }

    // GET /api/admin/employees/{id}
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeServiceImpl.findEmployee(id);
    }

    // POST /api/admin/employees
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeServiceImpl.createEmployee(employee);
    }

    // PUT /api/admin/employees/{id}
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeServiceImpl.updateEmployee(id, employee);
    }

    // DELETE /api/admin/employees/{id}
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeServiceImpl.deleteEmployee(id);
    }

    // PUT /api/admin/employees/id/assign-position?title=positionTitle
    @PutMapping("/{id}/assign-position")
    public Employee assignPosition(@PathVariable Long id,
                                   @RequestParam String title) {
        return employeeServiceImpl.assignPositionToEmployee(id, title);
    }
}