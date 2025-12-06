package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.service.AdminService;
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
    public ResponseEntity<List<Employee>> getAllEmployees(@PathVariable Long adminId) {
        List<Employee> employee = adminService.getAllEmployees(
                adminId
        );
        return ResponseEntity.ok(employee);
    }

    // GET /admin/{adminId}/employees/find/{employeeId}
    @GetMapping("/find/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long adminId,
                                                    @PathVariable Long employeeId) {
        Employee employee = adminService.findEmployee(
                adminId,
                employeeId
        );
        return ResponseEntity.ok(employee);
    }

    // POST /admin/{adminId}/employees/create
    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@PathVariable Long adminId,
                                                   @RequestBody Employee employee) {
        Employee createdEmployee = adminService.createEmployee(
                adminId,
                employee
        );
        return ResponseEntity.ok(createdEmployee);
    }

    // PATCH /admin/{adminId}/employees/update/{employeeId}
    @PatchMapping("/update/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long adminId,
                                                   @PathVariable Long employeeId,
                                                   @RequestBody Employee employee) {
        Employee updatedEmployee = adminService.updateEmployee(
                adminId,
                employeeId,
                employee
        );
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE /admin/{adminId}/employees/delete/{employeeId}
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long adminId,
                                               @PathVariable Long employeeId) {
        adminService.deleteEmployee(
                adminId,
                employeeId
        );
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // PATCH /admin/{adminId}/employees/assign-position/{employeeId}
    @PatchMapping("/assign-position/{employeeId}")
    public ResponseEntity<Employee> assignPosition(@PathVariable Long adminId,
                                                   @PathVariable Long employeeId,
                                                   @RequestBody String positionTitle) {
        Employee employee = adminService.assignPositionToEmployee(
                adminId,
                employeeId,
                positionTitle
        );
        return ResponseEntity.ok(employee);
    }
}