package com.helpdesk.controller;

import com.helpdesk.service.DatabaseInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class DatabaseInitializationController {

    @Autowired
    private DatabaseInitializationService databaseInitializationService;

    // POST /database/load
    @PostMapping("/load")
    public ResponseEntity<String> loadData() {
        databaseInitializationService.loadData();
        return ResponseEntity.ok("Data Loaded Successfully");
    }
}
