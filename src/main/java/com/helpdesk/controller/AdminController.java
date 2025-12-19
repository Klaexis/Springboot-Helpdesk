package com.helpdesk.controller;

import com.helpdesk.model.request.AdminCreateRequestDTO;
import com.helpdesk.model.request.AdminSearchRequestDTO;
import com.helpdesk.model.request.AdminUpdateRequestDTO;
import com.helpdesk.model.request.AssignPositionRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.model.response.PageResponseDTO;
import com.helpdesk.service.AdminService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // GET /admin/{adminId}/employees
    @GetMapping("/{adminId}/employees")
    public ResponseEntity<List<AdminResponseDTO>> getAllEmployees(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminService.getAllEmployees(
                adminId
        ));
    }

    // GET /admin/{adminId}/employees/pages?page=0&size=5&sort=name,desc
    // sortBy = name, position, status
    @GetMapping("/{adminId}/employees/pages")
    public ResponseEntity<PageResponseDTO<Page<AdminResponseDTO>>> getAllEmployeesPaginated(
            @PathVariable Long adminId,
            Pageable pageable
    ) {
        Page<AdminResponseDTO> result = adminService.getAllEmployeesPaginated
                (adminId, pageable);

        return ResponseEntity.ok(
            new PageResponseDTO<>(
                "Page loaded successfully.",
                result
            )
        );
    }

    // GET /admin/{adminId}/employees/find/{employeeId}
    @GetMapping("/{adminId}/employees/find/{employeeId}")
    public ResponseEntity<AdminResponseDTO> getEmployeeById(@PathVariable Long adminId,
                                                            @PathVariable Long employeeId) {
        return ResponseEntity.ok(adminService.findEmployee(
                adminId, employeeId
        ));
    }

    // POST /admin/{adminId}/employees/create
    @PostMapping("/{adminId}/employees/create")
    public ResponseEntity<AdminResponseDTO> createEmployee(@PathVariable Long adminId,
                                                           @Valid @RequestBody AdminCreateRequestDTO request) {
        return ResponseEntity.ok(adminService.createEmployee(
                adminId,
                request
        ));
    }

    // PATCH /admin/{adminId}/employees/update/{employeeId}
    @PatchMapping("/{adminId}/employees/update/{employeeId}")
    public ResponseEntity<AdminResponseDTO> updateEmployee(@PathVariable Long adminId,
                                                           @PathVariable Long employeeId,
                                                           @RequestBody AdminUpdateRequestDTO request) {
        return ResponseEntity.ok(adminService.updateEmployee(
                adminId,
                employeeId,
                request
        ));
    }

    // DELETE /admin/{adminId}/employees/delete/{employeeId}
    @DeleteMapping("/{adminId}/employees/delete/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long adminId,
                                               @PathVariable Long employeeId) {
        adminService.deleteEmployee(adminId, employeeId);
        return ResponseEntity.noContent().build();
    }

    // PATCH /admin/{adminId}/employees/assign-position/{employeeId}
    @PatchMapping("/{adminId}/employees/assign-position/{employeeId}")
    public ResponseEntity<AdminResponseDTO> assignPosition(@PathVariable Long adminId,
                                                           @PathVariable Long employeeId,
                                                           @RequestBody AssignPositionRequestDTO position) {
        return ResponseEntity.ok(adminService.assignPositionToEmployee(
                adminId,
                employeeId,
                position
        ));
    }

    // PATCH /admin/{adminId}/employees/unassign-position/{employeeId}
    @PatchMapping("/{adminId}/employees/unassign-position/{employeeId}")
    public ResponseEntity<AdminResponseDTO> unassignPosition(@PathVariable Long adminId,
                                                             @PathVariable Long employeeId) {
        return ResponseEntity.ok(adminService.unassignPositionFromEmployee(
                adminId,
                employeeId
        ));
    }

    // GET /admin/{adminId}/employees/search?name=John&position=Employee&status=ACTIVE&page=0&size=5&sort=name,asc
    @GetMapping("/{adminId}/employees/search")
    public ResponseEntity<PageResponseDTO<Page<AdminResponseDTO>>> searchEmployees(
            @PathVariable Long adminId,
            AdminSearchRequestDTO searchRequest,
            Pageable pageable
    ) {
        Page<AdminResponseDTO> result = adminService.searchEmployees(
                adminId,
                searchRequest,
                pageable
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Search completed successfully.",
                        result
                )
        );
    }
}
