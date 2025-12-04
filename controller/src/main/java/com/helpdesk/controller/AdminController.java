//package com.helpdesk.controller;
//
//import com.helpdesk.model.Employee;
//import com.helpdesk.service.impl.AdminServiceImpl;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/employees")
//public class AdminController {
//
//    private final AdminServiceImpl adminServiceImpl;
//
//    public AdminController(AdminServiceImpl adminServiceImpl) {
//        this.adminServiceImpl = adminServiceImpl;
//    }
//
//    // GET /api/admin/employees
//    @GetMapping
//    public List<Employee> getAllEmployees() {
//        return adminServiceImpl.getAllEmployees();
//    }
//
//    // GET /api/admin/employees/{id}
//    @GetMapping("/{id}")
//    public Employee getEmployeeById(@PathVariable Long id) {
//        return adminServiceImpl.findEmployee(id);
//    }
//
//    // POST /api/admin/employees
//    @PostMapping
//    public Employee createEmployee(@RequestBody Employee employee) {
//        return adminServiceImpl.createEmployee(employee);
//    }
//
//    // PUT /api/admin/employees/{id}
//    @PutMapping("/{id}")
//    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
//        return adminServiceImpl.updateEmployee(id, employee);
//    }
//
//    // DELETE /api/admin/employees/{id}
//    @DeleteMapping("/{id}")
//    public void deleteEmployee(@PathVariable Long id) {
//        adminServiceImpl.deleteEmployee(id);
//    }
//
//    // PUT /api/admin/employees/id/assign-position?title=positionTitle
//    @PutMapping("/{id}/assign-position")
//    public Employee assignPosition(@PathVariable Long id,
//                                   @RequestParam String title) {
//        return adminServiceImpl.assignPositionToEmployee(id, title);
//    }
//}