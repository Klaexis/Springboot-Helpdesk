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
    private final EmployeePositionRepository positionRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    @Autowired
    public EmployeePositionServiceImpl(EmployeePositionRepository positionRepository,
                                       EmployeeRepository employeeRepository,
                                       EmployeeValidationHelper employeeValidationHelper) {
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    private Employee validateAdmin(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    private EmployeePosition getPositionOrThrow(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }

    public EmployeePosition findPosition(Long adminId,
                                         Long positionId) {
        validateAdmin(adminId);

        return getPositionOrThrow(positionId);
    }

    public List<EmployeePosition> getAllPositions(Long adminId) {
        validateAdmin(adminId);

        return positionRepository.findAll();
    }

    public EmployeePosition createPosition(Long adminId,
                                           String positionTitle) {
        validateAdmin(adminId);

        EmployeePosition position = new EmployeePosition();
        position.setPositionTitle(positionTitle);

        return positionRepository.save(position);
    }

    public EmployeePosition updatePosition(Long adminId,
                                           Long positionId,
                                           String positionTitle) {
        validateAdmin(adminId);
        EmployeePosition position = getPositionOrThrow(positionId);

        position.setPositionTitle(positionTitle);

        return positionRepository.save(position);
    }

    public void deletePosition(Long adminId,
                               Long positionId) {
        validateAdmin(adminId);

        positionRepository.deleteById(positionId);
    }
}
