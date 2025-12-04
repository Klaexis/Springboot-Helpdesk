package com.helpdesk.controller;

import com.helpdesk.model.EmployeePosition;
import com.helpdesk.service.impl.EmployeePositionServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/{adminId}/positions")
public class EmployeePositionController {
    private final EmployeePositionServiceImpl employeePositionServiceImpl;

    public EmployeePositionController(EmployeePositionServiceImpl employeePositionService) {
        this.employeePositionServiceImpl = employeePositionService;
    }

    // GET /admin/{adminId}/positions
    @GetMapping
    public List<EmployeePosition> getAllPositions(@PathVariable Long adminId) {
        return employeePositionServiceImpl.getAllPositions(adminId);
    }

    // GET /admin/{adminId}/positions/{employeeId}
    @GetMapping("/get/{employeeId}")
    public EmployeePosition getPositionById(@PathVariable Long adminId, @PathVariable Long employeeId) {
        return employeePositionServiceImpl.findPosition(adminId, employeeId);
    }

    // POST /admin/{adminId}/positions/create
    @PostMapping("/create")
    public EmployeePosition createEmployeePosition(@PathVariable Long adminId, @RequestBody String positionTitle) {
        return employeePositionServiceImpl.createPosition(adminId, positionTitle);
    }

    // PUT /admin/{adminId}/positions/update/{employeeId}
    @PutMapping("/update/{employeeId}")
    public EmployeePosition updateEmployeePosition(@PathVariable Long adminId, @PathVariable Long employeeId, @RequestBody String positionTitle) {
        return employeePositionServiceImpl.updatePosition(adminId, employeeId, positionTitle);
    }

    // DELETE /admin/{adminId}/positions/delete/{employeeId}
    @DeleteMapping("/delete/{employeeId}")
    public void deleteEmployeePosition(@PathVariable Long adminId, @PathVariable Long employeeId) {
        employeePositionServiceImpl.deletePosition(adminId, employeeId);
    }
}
