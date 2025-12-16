package com.helpdesk.controller;

import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.util.DatabaseInitializationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class DatabaseInitializationController {

    private final DatabaseInitializationHelper databaseInitializationHelper;

    public DatabaseInitializationController(DatabaseInitializationHelper databaseInitializationHelper) {
        this.databaseInitializationHelper = databaseInitializationHelper;
    }
    // POST /database/load
    @PostMapping("/load")
    public ResponseEntity<String> loadData() {
        databaseInitializationHelper.loadData();
        return ResponseEntity.ok("Data Loaded Successfully");
    }
}
