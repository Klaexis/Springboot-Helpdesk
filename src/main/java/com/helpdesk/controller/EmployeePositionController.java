package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.service.EmployeePositionService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<EmployeePosition>>  getAllPositions(@PathVariable Long adminId) {
        List<EmployeePosition> employeePositions = employeePositionService.getAllPositions(
                adminId
        );
        return ResponseEntity.ok(employeePositions);
    }

    // GET /admin/{adminId}/positions/get/{positionId}
    @GetMapping("/get/{positionId}")
    public ResponseEntity<EmployeePosition> getPositionById(@PathVariable Long adminId,
                                                            @PathVariable Long positionId) {
        EmployeePosition employeePosition = employeePositionService.findPosition(
                adminId,
                positionId
        );
        return ResponseEntity.ok(employeePosition);
    }

    // POST /admin/{adminId}/positions/create
    @PostMapping("/create")
    public ResponseEntity<EmployeePosition> createEmployeePosition(@PathVariable Long adminId,
                                                                   @RequestBody String positionTitle) {
        EmployeePosition createdEmployeePosition = employeePositionService.createPosition(
                adminId,
                positionTitle
        );
        return ResponseEntity.ok(createdEmployeePosition);
    }

    // PATCH /admin/{adminId}/positions/update/{positionId}
    @PatchMapping("/update/{positionId}")
    public ResponseEntity<EmployeePosition> updateEmployeePosition(@PathVariable Long adminId,
                                                                   @PathVariable Long positionId,
                                                                   @RequestBody String positionTitle) {
        EmployeePosition updatedEmployeePosition = employeePositionService.updatePosition(
                adminId,
                positionId,
                positionTitle
        );
        return ResponseEntity.ok(updatedEmployeePosition);
    }

    // DELETE /admin/{adminId}/positions/delete/{positionId}
    @DeleteMapping("/delete/{positionId}")
    public ResponseEntity<Void> deleteEmployeePosition(@PathVariable Long adminId,
                                                       @PathVariable Long positionId) {
        employeePositionService.deletePosition(
                adminId,
                positionId
        );
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
