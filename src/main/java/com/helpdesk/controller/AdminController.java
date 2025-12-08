package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.model.request.AdminRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.service.AdminService;
import com.helpdesk.service.mapper.EmployeeMapper;
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
        List<AdminResponseDTO> list = adminService.getAllEmployees(adminId)
                .stream()
                .map(EmployeeMapper::toDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    // GET /admin/{adminId}/employees/find/{employeeId}
    @GetMapping("/find/{employeeId}")
    public ResponseEntity<AdminResponseDTO> getEmployeeById(@PathVariable Long adminId,
                                                            @PathVariable Long employeeId) {
        return ResponseEntity.ok(
                EmployeeMapper.toDTO(adminService.findEmployee(adminId, employeeId))
        );
    }

    // POST /admin/{adminId}/employees/create
    @PostMapping("/create")
    public ResponseEntity<AdminResponseDTO> createEmployee(@PathVariable Long adminId,
                                                           @RequestBody AdminRequestDTO request) {

        Employee created = adminService.createEmployee(adminId, request);
        return ResponseEntity.ok(EmployeeMapper.toDTO(created));
    }

    // PATCH /admin/{adminId}/employees/update/{employeeId}
    @PatchMapping("/update/{employeeId}")
    public ResponseEntity<AdminResponseDTO> updateEmployee(@PathVariable Long adminId,
                                                           @PathVariable Long employeeId,
                                                           @RequestBody AdminRequestDTO request) {
        Employee updated = adminService.updateEmployee(adminId, employeeId, request);
        return ResponseEntity.ok(EmployeeMapper.toDTO(updated));
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
        Employee employee = adminService.assignPositionToEmployee(adminId, employeeId, positionTitle);
        return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
    }
}
