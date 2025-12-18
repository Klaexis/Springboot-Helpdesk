package com.helpdesk.service.impl;

import com.helpdesk.exception.AdminNotFoundException;
import com.helpdesk.exception.EmployeePositionNotFoundException;
import com.helpdesk.exception.EmptyPageException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.request.EmployeePositionRequestDTO;
import com.helpdesk.model.response.EmployeePositionResponseDTO;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.EmployeePositionService;

import com.helpdesk.service.mapper.EmployeePositionMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeePositionServiceImpl implements EmployeePositionService {
    private final EmployeePositionRepository positionRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    private final EmployeePositionMapper positionMapper;

    public EmployeePositionServiceImpl(EmployeePositionRepository positionRepository,
                                       EmployeeRepository employeeRepository,
                                       EmployeeValidationHelper employeeValidationHelper,
                                       EmployeePositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
        this.positionMapper = positionMapper;
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
    public EmployeePositionResponseDTO findPosition(Long adminId,
                                                    Long positionId) {
        validateAdmin(adminId);

        return positionMapper.toDTO(getPositionOrThrow(positionId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeePositionResponseDTO> getAllPositions(Long adminId) {
        validateAdmin(adminId);

        return positionRepository.findAll()
                .stream()
                .map(positionMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeePositionResponseDTO> getAllPositionsPaginated(Long adminId,
                                                                      Pageable pageable) {
        validateAdmin(adminId);

        Sort sortedFields;

        if (pageable.getSort().isUnsorted()) {
            sortedFields = Sort.by("positionTitle").ascending();
        } else {
            List<Sort.Order> orders = new ArrayList<>();

            for (Sort.Order order : pageable.getSort()) {
                String mappedField = switch (order.getProperty().toLowerCase()) {
                    case "position" -> "positionTitle";
                    default -> throw new IllegalArgumentException(
                            "Invalid sort field: " + order.getProperty()
                    );
                };

                orders.add(new Sort.Order(order.getDirection(), mappedField));
            }

            sortedFields = Sort.by(orders);
        }

        Pageable mappedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortedFields
        );

        Page<EmployeePosition> positions =
                positionRepository.findAll(mappedPageable);

        if (positions.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "No positions found"
            );
        }

        return positions.map(positionMapper::toDTO);
    }

    @Override
    public EmployeePositionResponseDTO createPosition(Long adminId,
                                                      EmployeePositionRequestDTO  createdPosition) {
        validateAdmin(adminId);

        if (createdPosition.getPositionTitle() == null || createdPosition.getPositionTitle().isBlank()) {
            throw new IllegalArgumentException("Position title cannot be null or blank");
        }

        EmployeePosition position = positionMapper.toEntity(createdPosition);
        return positionMapper.toDTO(positionRepository.save(position));
    }

    @Override
    public EmployeePositionResponseDTO updatePosition(Long adminId,
                                                      Long positionId,
                                                      EmployeePositionRequestDTO updatedPosition) {
        validateAdmin(adminId);

        if (updatedPosition.getPositionTitle() == null || updatedPosition.getPositionTitle().isBlank()) {
            throw new IllegalArgumentException("Position title cannot be null or blank");
        }

        EmployeePosition position = getPositionOrThrow(positionId);
        positionMapper.updateEntity(updatedPosition, position);

        return positionMapper.toDTO(positionRepository.save(position));
    }

    @Override
    public void deletePosition(Long adminId,
                               Long positionId) {
        validateAdmin(adminId);

        positionRepository.deleteById(positionId);
    }
}
