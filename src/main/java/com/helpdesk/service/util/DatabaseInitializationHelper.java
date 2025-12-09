package com.helpdesk.service.util;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.EmploymentStatus;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializationHelper {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeePositionRepository employeePositionRepository;

    public void loadData() {
        EmployeePosition adminPosition = new EmployeePosition("Admin");
        EmployeePosition userPosition = new EmployeePosition("Employee");
        EmployeePosition engrPosition = new EmployeePosition("Software Engineer");
        EmployeePosition qaPosition = new EmployeePosition("QA Engineer");
        EmployeePosition hrPosition = new EmployeePosition("Human Resource");
        EmployeePosition marketingPosition = new EmployeePosition("Marketing");

        employeePositionRepository.save(adminPosition);
        employeePositionRepository.save(userPosition);
        employeePositionRepository.save(engrPosition);
        employeePositionRepository.save(qaPosition);
        employeePositionRepository.save(hrPosition);
        employeePositionRepository.save(marketingPosition);

        // Create employees
        Employee admin = new Employee("Admin Name", 30, "Address", "1234567890", "admin@example.com");
        admin.setEmployeePosition(adminPosition);
        admin.setEmploymentStatus(EmploymentStatus.ACTIVE);

        Employee emp = new Employee(
            "User Name",
            25,
            "Address",
            "0987654321",
            "user@example.com"
        );
        emp.setEmployeePosition(userPosition);
        emp.setEmploymentStatus(EmploymentStatus.ACTIVE);

        Employee se = new Employee(
            "SE Name",
            35,
            "Address",
            "12312312312",
            "se@example.com"
        );
        se.setEmployeePosition(engrPosition);
        se.setEmploymentStatus(EmploymentStatus.ACTIVE);

        Employee qa = new Employee(
            "QA Name",
            20,
            "Address",
            "123123123",
            "qa@example.com"
        );
        qa.setEmployeePosition(qaPosition);
        qa.setEmploymentStatus(EmploymentStatus.ACTIVE);

        Employee hr = new Employee(
                "HR Name",
                40,
                "Address",
                "123123123213",
                "hr@example.com"
        );
        hr.setEmployeePosition(hrPosition);
        hr.setEmploymentStatus(EmploymentStatus.ACTIVE);

        Employee mkt = new Employee(
                "MKT Name",
                45,
                "Address",
                "658678678968",
                "mkt@example.com"
        );
        mkt.setEmployeePosition(marketingPosition);
        mkt.setEmploymentStatus(EmploymentStatus.ACTIVE);

        employeeRepository.save(admin);
        employeeRepository.save(emp);
        employeeRepository.save(se);
        employeeRepository.save(qa);
        employeeRepository.save(hr);
        employeeRepository.save(mkt);
    }
}
