package com.helpdesk.service;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.EmploymentStatus;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeePositionRepository employeePositionRepository;

    public void loadData() {
        // Create example positions
        EmployeePosition adminPosition = new EmployeePosition("Admin");
        EmployeePosition userPosition = new EmployeePosition("Employee");

        employeePositionRepository.save(adminPosition);
        employeePositionRepository.save(userPosition);

        // Create employees
        Employee admin = new Employee("Admin Name", 30, "Address", "1234567890", "admin@example.com");
        admin.setEmployeePosition(adminPosition);
        admin.setEmploymentStatus(EmploymentStatus.ACTIVE);

        Employee user = new Employee("User Name", 25, "Address", "0987654321", "user@example.com");
        user.setEmployeePosition(userPosition);
        user.setEmploymentStatus(EmploymentStatus.ACTIVE);

        employeeRepository.save(admin);
        employeeRepository.save(user);
    }
}
