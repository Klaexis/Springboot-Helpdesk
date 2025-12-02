package com.helpdesk.controller;

import com.helpdesk.model.EmployeePosition;
import com.helpdesk.service.EmployeePositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/positions")
public class EmployeePositionController {
    private final EmployeePositionService employeePositionService;

    public EmployeePositionController(EmployeePositionService employeePositionService) {
        this.employeePositionService = employeePositionService;
    }

    // GET /api/admin/positions
    @GetMapping
    public List<EmployeePosition> getAllPositions() {
        return employeePositionService.getAllPositions();
    }

    // GET /api/admin/positions/{id}
    @GetMapping("/{id}")
    public EmployeePosition getPositionById(@PathVariable Long id) {
        return employeePositionService.findPosition(id);
    }

    // POST /api/admin/position
    @PostMapping
    public EmployeePosition createEmployeePosition(@RequestBody String title) {
        return employeePositionService.createPosition(title);
    }

    // PUT /api/admin/position/{id}
    @PutMapping("/{id}")
    public EmployeePosition updateEmployeePosition(@PathVariable Long id, @RequestBody String title) {
        return employeePositionService.updatePosition(id, title);
    }

    // DELETE /api/admin/position/{id}
    @DeleteMapping("/{id}")
    public void deleteEmployeePosition(@PathVariable Long id) {
        employeePositionService.deletePosition(id);
    }
}
