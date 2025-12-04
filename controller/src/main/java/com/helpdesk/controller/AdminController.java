package com.helpdesk.controller;

import com.helpdesk.model.Employee;
import com.helpdesk.service.impl.AdminServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/{adminId}/employees")
public class AdminController {
    private final AdminServiceImpl adminServiceImpl;

    public AdminController(AdminServiceImpl adminServiceImpl) {
        this.adminServiceImpl = adminServiceImpl;
    }

    // GET /admin/{adminId}/employees
    @GetMapping
    public List<Employee> getAllEmployees(@PathVariable Long adminId) {
        return adminServiceImpl.getAllEmployees(adminId);
    }

    // GET /admin/{adminId}/employees/find/{employeeId}
    @GetMapping("/find/{employeeId}")
    public Employee getEmployeeById(@PathVariable Long adminId, Long employeeId) {
        return adminServiceImpl.findEmployee(adminId, employeeId);
    }

    // POST /admin/{adminId}/employees/create
    @PostMapping("/create")
    public Employee createEmployee(@PathVariable Long adminId, @RequestBody Employee employee) {
        return adminServiceImpl.createEmployee(adminId, employee);
    }

    // PUT /admin/{adminId}/employees/update/{employeeId}
    @PutMapping("/update/{employeeId}")
    public Employee updateEmployee(@PathVariable Long adminId, @PathVariable Long employeeId, @RequestBody Employee employee) {
        return adminServiceImpl.updateEmployee(adminId, employeeId, employee);
    }

    // DELETE /admin/{adminId}/employees/delete/{employeeId}
    @DeleteMapping("/delete/{employeeId}")
    public void deleteEmployee(@PathVariable Long adminId, @PathVariable Long employeeId) {
        adminServiceImpl.deleteEmployee(adminId, employeeId);
    }

    // PUT /admin/{adminId}/employees/{employeeId}/assign-position?positionTitle=positionTitle
    @PutMapping("/assign-position/{employeeId}")
    public Employee assignPosition(@PathVariable Long adminId,
                                   @PathVariable Long employeeId,
                                   @RequestParam String positionTitle) {
        return adminServiceImpl.assignPositionToEmployee(adminId, employeeId, positionTitle);
    }
}