package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.EmployeePositionService;

import com.helpdesk.service.util.EmployeeValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeePositionServiceImpl implements EmployeePositionService {
    @Autowired
    private final EmployeePositionRepository positionRepository;

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeeValidationHelper employeeValidationHelper;

    public EmployeePositionServiceImpl(EmployeePositionRepository positionRepository,
                                       EmployeeRepository employeeRepository,
                                       EmployeeValidationHelper employeeValidationHelper) {
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    public EmployeePosition findPosition(Long adminId,
                                         Long positionId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }

    public List<EmployeePosition> getAllPositions(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return positionRepository.findAll();
    }

    public EmployeePosition createPosition(Long adminId,
                                           String positionTitle) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        EmployeePosition position = new EmployeePosition();
        position.setPositionTitle(positionTitle);
        return positionRepository.save(position);
    }

    public EmployeePosition updatePosition(Long adminId,
                                           Long positionId,
                                           String positionTitle) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        EmployeePosition position = positionRepository.findById(positionId).orElseThrow(() -> new RuntimeException("Position not found"));
        position.setPositionTitle(positionTitle);
        return positionRepository.save(position);
    }

    public void deletePosition(Long adminId,
                               Long positionId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        positionRepository.deleteById(positionId);
    }
}
