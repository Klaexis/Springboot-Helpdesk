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

    // GET /admin/{adminId}/positions/get/{positionId}
    @GetMapping("/get/{positionId}")
    public EmployeePosition getPositionById(@PathVariable Long adminId,
                                            @PathVariable Long positionId) {
        return employeePositionService.findPosition(adminId, positionId);
    }

    // POST /admin/{adminId}/positions/create
    @PostMapping("/create")
    public EmployeePosition createEmployeePosition(@PathVariable Long adminId,
                                                   @RequestBody String positionTitle) {
        return employeePositionService.createPosition(adminId, positionTitle);
    }

    // PUT /admin/{adminId}/positions/update/{positionId}
    @PutMapping("/update/{positionId}")
    public EmployeePosition updateEmployeePosition(@PathVariable Long adminId,
                                                   @PathVariable Long positionId,
                                                   @RequestBody String positionTitle) {
        return employeePositionService.updatePosition(adminId, positionId, positionTitle);
    }

    // DELETE /admin/{adminId}/positions/delete/{positionId}
    @DeleteMapping("/delete/{positionId}")
    public void deleteEmployeePosition(@PathVariable Long adminId,
                                       @PathVariable Long positionId) {
        employeePositionService.deletePosition(adminId, positionId);
    }
}
