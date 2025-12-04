package com.helpdesk.controller;

import com.helpdesk.model.EmployeePosition;
import com.helpdesk.service.EmployeePositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/{adminId}/positions")
public class EmployeePositionController {
    private final EmployeePositionService employeePositionService;

    public EmployeePositionController(EmployeePositionService employeePositionService) {
        this.employeePositionService = employeePositionService;
    }

    // GET /admin/{adminId}/positions
    @GetMapping
    public List<EmployeePosition> getAllPositions(@PathVariable Long adminId) {
        return employeePositionService.getAllPositions(adminId);
    }

    // GET /admin/{adminId}/positions/{employeeId}
    @GetMapping("/get/{employeeId}")
    public EmployeePosition getPositionById(@PathVariable Long adminId, @PathVariable Long employeeId) {
        return employeePositionService.findPosition(adminId, employeeId);
    }

    // POST /admin/{adminId}/positions/create
    @PostMapping("/create")
    public EmployeePosition createEmployeePosition(@PathVariable Long adminId, @RequestBody String positionTitle) {
        return employeePositionService.createPosition(adminId, positionTitle);
    }

    // PUT /admin/{adminId}/positions/update/{employeeId}
    @PutMapping("/update/{employeeId}")
    public EmployeePosition updateEmployeePosition(@PathVariable Long adminId, @PathVariable Long employeeId, @RequestBody String positionTitle) {
        return employeePositionService.updatePosition(adminId, employeeId, positionTitle);
    }

    // DELETE /admin/{adminId}/positions/delete/{employeeId}
    @DeleteMapping("/delete/{employeeId}")
    public void deleteEmployeePosition(@PathVariable Long adminId, @PathVariable Long employeeId) {
        employeePositionService.deletePosition(adminId, employeeId);
    }
}
