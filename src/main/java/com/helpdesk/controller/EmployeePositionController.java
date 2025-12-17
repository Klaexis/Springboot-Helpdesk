package com.helpdesk.controller;

import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.response.PageResponseDTO;
import com.helpdesk.service.EmployeePositionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class EmployeePositionController {
    private final EmployeePositionService employeePositionService;

    public EmployeePositionController(EmployeePositionService employeePositionService) {
        this.employeePositionService = employeePositionService;
    }

    // GET /admin/{adminId}/positions
    @GetMapping("/{adminId}/positions")
    public ResponseEntity<List<EmployeePosition>>  getAllPositions(@PathVariable Long adminId) {
        return ResponseEntity.ok(employeePositionService.getAllPositions(
                adminId
        ));
    }

    // GET /admin/{adminId}/positions/pages?page=0&size=5&sortBy=position&direction=asc
    // sortBy = position
    @GetMapping("/{adminId}/positions/pages")
    public ResponseEntity<PageResponseDTO<Page<EmployeePosition>>> getAllPositionsPaginated(
            @PathVariable Long adminId,
            Pageable pageable
    ) {
        Page<EmployeePosition> result = employeePositionService.getAllPositionsPaginated(
                adminId, pageable.getPageNumber(), pageable.getPageSize(), "position", "asc");

        return ResponseEntity.ok(
            new PageResponseDTO<>(
                "Page loaded successfully.",
                result
            )
        );
    }

    // GET /admin/{adminId}/positions/get/{positionId}
    @GetMapping("/{adminId}/positions/get/{positionId}")
    public ResponseEntity<EmployeePosition> getPositionById(@PathVariable Long adminId,
                                                            @PathVariable Long positionId) {
        return ResponseEntity.ok(employeePositionService.findPosition(
                adminId,
                positionId
        ));
    }

    // POST /admin/{adminId}/positions/create
    @PostMapping("/{adminId}/positions/create")
    public ResponseEntity<EmployeePosition> createEmployeePosition(@PathVariable Long adminId,
                                                                   @RequestBody String positionTitle) {
        return ResponseEntity.ok(employeePositionService.createPosition(
                adminId,
                positionTitle
        ));
    }

    // PATCH /admin/{adminId}/positions/update/{positionId}
    @PatchMapping("/{adminId}/positions/update/{positionId}")
    public ResponseEntity<EmployeePosition> updateEmployeePosition(@PathVariable Long adminId,
                                                                   @PathVariable Long positionId,
                                                                   @RequestBody String positionTitle) {
        return ResponseEntity.ok(employeePositionService.updatePosition(
                adminId,
                positionId,
                positionTitle
        ));
    }

    // DELETE /admin/{adminId}/positions/delete/{positionId}
    @DeleteMapping("/{adminId}/positions/delete/{positionId}")
    public ResponseEntity<Void> deleteEmployeePosition(@PathVariable Long adminId,
                                                       @PathVariable Long positionId) {
        employeePositionService.deletePosition(
                adminId,
                positionId
        );
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
