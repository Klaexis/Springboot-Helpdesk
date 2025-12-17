package com.helpdesk.service.impl;

import com.helpdesk.exception.AdminNotFoundException;
import com.helpdesk.exception.EmployeePositionNotFoundException;
import com.helpdesk.exception.EmptyPageException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.EmployeePositionService;

import com.helpdesk.service.util.EmployeeValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeePositionServiceImpl implements EmployeePositionService {
    private final EmployeePositionRepository positionRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    public EmployeePositionServiceImpl(EmployeePositionRepository positionRepository,
                                       EmployeeRepository employeeRepository,
                                       EmployeeValidationHelper employeeValidationHelper) {
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    private Employee validateAdmin(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    private EmployeePosition getPositionOrThrow(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new EmployeePositionNotFoundException(positionId));
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeePosition findPosition(Long adminId,
                                         Long positionId) {
        validateAdmin(adminId);

        return getPositionOrThrow(positionId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeePosition> getAllPositions(Long adminId) {
        validateAdmin(adminId);

        return positionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeePosition> getAllPositionsPaginated(Long adminId,
                                                           int page,
                                                           int size,
                                                           String sortBy,
                                                           String direction) {
        validateAdmin(adminId);

        String sortField = switch (sortBy.toLowerCase()) {
            case "position" -> "positionTitle";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeePosition> positions = positionRepository.findAll(pageable);
        if (positions.isEmpty()) {
            throw new EmptyPageException(page, "No positions found");
        }

        return positions;
    }

    @Override
    public EmployeePosition createPosition(Long adminId,
                                           String positionTitle) {
        validateAdmin(adminId);

        if (positionTitle == null || positionTitle.isBlank()) {
            throw new IllegalArgumentException("Position title cannot be null or blank");
        }

        EmployeePosition position = new EmployeePosition();
        position.setPositionTitle(positionTitle);

        return positionRepository.save(position);
    }

    @Override
    public EmployeePosition updatePosition(Long adminId,
                                           Long positionId,
                                           String positionTitle) {
        validateAdmin(adminId);

        if (positionTitle == null || positionTitle.isBlank()) {
            throw new IllegalArgumentException("Position title cannot be null or blank");
        }

        EmployeePosition position = getPositionOrThrow(positionId);

        position.setPositionTitle(positionTitle);

        return positionRepository.save(position);
    }

    @Override
    public void deletePosition(Long adminId,
                               Long positionId) {
        validateAdmin(adminId);

        positionRepository.deleteById(positionId);
    }
}
