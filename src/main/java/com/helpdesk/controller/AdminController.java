package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.model.request.AdminRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.service.AdminService;
import com.helpdesk.service.mapper.EmployeeMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Employee> employees = adminService.getAllEmployees(adminId);
        List<AdminResponseDTO> dtoList = employees.stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // GET /admin/{adminId}/employees/find/{employeeId}
    @GetMapping("/find/{employeeId}")
    public ResponseEntity<AdminResponseDTO> getEmployeeById(@PathVariable Long adminId,
                                                            @PathVariable Long employeeId) {
        Employee employee = adminService.findEmployee(adminId, employeeId);
        return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
    }

    // POST /admin/{adminId}/employees/create
    @PostMapping("/create")
    public ResponseEntity<AdminResponseDTO> createEmployee(@PathVariable Long adminId,
                                                           @RequestBody AdminRequestDTO requestDTO) {
        Employee employee = EmployeeMapper.fromDTO(requestDTO);
        String positionTitle = requestDTO.getEmployeePosition() != null
                ? requestDTO.getEmployeePosition().getPositionTitle()
                : null;

        Employee createdEmployee = adminService.createEmployee(adminId, employee, positionTitle);
        return ResponseEntity.ok(EmployeeMapper.toDTO(createdEmployee));
    }

    // PATCH /admin/{adminId}/employees/update/{employeeId}
    @PatchMapping("/update/{employeeId}")
    public ResponseEntity<AdminResponseDTO> updateEmployee(@PathVariable Long adminId,
                                                           @PathVariable Long employeeId,
                                                           @RequestBody AdminRequestDTO requestDTO) {
        Employee employee = adminService.findEmployee(adminId, employeeId);
        EmployeeMapper.updateEmployeeFromDTO(employee, requestDTO);

        if (requestDTO.getEmployeePosition() != null) {
            employee = adminService.assignPositionToEmployee(
                    adminId,
                    employeeId,
                    requestDTO.getEmployeePosition().getPositionTitle()
            );
        } else {
            employee = adminService.updateEmployee(adminId, employeeId, employee);
        }

        return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
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
