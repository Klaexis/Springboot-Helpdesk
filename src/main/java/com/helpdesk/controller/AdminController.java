package com.helpdesk.controller;

import com.helpdesk.model.request.AdminCreateRequestDTO;
import com.helpdesk.model.request.AdminUpdateRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.model.response.PageResponseDTO;
import com.helpdesk.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/{adminId}/employees")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // GET /admin/{adminId}/employees
    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> getAllEmployees(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminService.getAllEmployees(
                adminId
        ));
    }

    // GET /admin/{adminId}/employees/pages?page=0&size=5
    @GetMapping("/pages")
    public ResponseEntity<PageResponseDTO<Page<AdminResponseDTO>>> getAllEmployeesPaginated(
            @PathVariable Long adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<AdminResponseDTO> result = adminService.getAllEmployeesPaginated
                (adminId, page, size, sortBy, direction);

        return ResponseEntity.ok(
            new PageResponseDTO<>(
                "Page loaded successfully.",
                result
            )
        );
    }

    // GET /admin/{adminId}/employees/find/{employeeId}
    @GetMapping("/find/{employeeId}")
    public ResponseEntity<AdminResponseDTO> getEmployeeById(@PathVariable Long adminId,
                                                            @PathVariable Long employeeId) {
        return ResponseEntity.ok(adminService.findEmployee(
                adminId, employeeId
        ));
    }

    // POST /admin/{adminId}/employees/create
    @PostMapping("/create")
    public ResponseEntity<AdminResponseDTO> createEmployee(@PathVariable Long adminId,
                                                           @Valid @RequestBody AdminCreateRequestDTO request) {
        return ResponseEntity.ok(adminService.createEmployee(
                adminId,
                request
        ));
    }

    // PATCH /admin/{adminId}/employees/update/{employeeId}
    @PatchMapping("/update/{employeeId}")
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
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long adminId,
                                               @PathVariable Long employeeId) {
        adminService.deleteEmployee(adminId, employeeId);
        return ResponseEntity.noContent().build();
    }

    // PATCH /admin/{adminId}/employees/assign-position/{employeeId}
    @PatchMapping("/assign-position/{employeeId}")
    public ResponseEntity<AdminResponseDTO> assignPosition(@PathVariable Long adminId,
                                                           @PathVariable Long employeeId,
                                                           @RequestBody String positionTitle) {
        return ResponseEntity.ok(adminService.assignPositionToEmployee(
                adminId,
                employeeId,
                positionTitle
        ));
    }
}
