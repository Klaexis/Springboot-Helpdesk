//package com.helpdesk.controller;
//
//import com.helpdesk.model.EmployeePosition;
//import com.helpdesk.service.impl.EmployeePositionServiceImpl;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/positions")
//public class EmployeePositionController {
//    private final EmployeePositionServiceImpl employeePositionServiceImpl;
//
//    public EmployeePositionController(EmployeePositionServiceImpl employeePositionService) {
//        this.employeePositionServiceImpl = employeePositionService;
//    }
//
//    // GET /api/admin/positions
//    @GetMapping
//    public List<EmployeePosition> getAllPositions() {
//        return employeePositionServiceImpl.getAllPositions();
//    }
//
//    // GET /api/admin/positions/{id}
//    @GetMapping("/{id}")
//    public EmployeePosition getPositionById(@PathVariable Long id) {
//        return employeePositionServiceImpl.findPosition(id);
//    }
//
//    // POST /api/admin/positions
//    @PostMapping
//    public EmployeePosition createEmployeePosition(@RequestBody String title) {
//        return employeePositionServiceImpl.createPosition(title);
//    }
//
//    // PUT /api/admin/positions/{id}
//    @PutMapping("/{id}")
//    public EmployeePosition updateEmployeePosition(@PathVariable Long id, @RequestBody String title) {
//        return employeePositionServiceImpl.updatePosition(id, title);
//    }
//
//    // DELETE /api/admin/positions/{id}
//    @DeleteMapping("/{id}")
//    public void deleteEmployeePosition(@PathVariable Long id) {
//        employeePositionServiceImpl.deletePosition(id);
//    }
//}
