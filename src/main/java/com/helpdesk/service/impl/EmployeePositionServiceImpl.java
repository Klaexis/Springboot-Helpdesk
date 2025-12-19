package com.helpdesk.service.impl;

import com.helpdesk.exception.EmptyPageException;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.request.EmployeePositionRequestDTO;
import com.helpdesk.model.response.EmployeePositionResponseDTO;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.service.EmployeePositionService;

import com.helpdesk.service.ValidationService;
import com.helpdesk.service.mapper.EmployeePositionMapper;
import com.helpdesk.service.util.PageableSortMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EmployeePositionServiceImpl implements EmployeePositionService {
    private final EmployeePositionRepository positionRepository;

    private final EmployeePositionMapper positionMapper;

    private final PageableSortMapper pageableSortMapper;

    private final ValidationService validationService;

    public EmployeePositionServiceImpl(EmployeePositionRepository positionRepository,
                                       EmployeePositionMapper positionMapper,
                                       PageableSortMapper pageableSortMapper,
                                       ValidationService validationService) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
        this.pageableSortMapper = pageableSortMapper;
        this.validationService = validationService;
    }

    private static final Map<String, String> POSITION_SORT_FIELDS = Map.of(
            "position","positionTitle"
    );

    private static final String DEFAULT_POSITION_SORT = "positionTitle";

    @Transactional(readOnly = true)
    @Override
    public EmployeePositionResponseDTO findPosition(Long adminId,
                                                    Long positionId) {
        validationService.validateAdmin(adminId);

        return positionMapper.toDTO(validationService.getPositionOrThrow(positionId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeePositionResponseDTO> getAllPositions(Long adminId) {
        validationService.validateAdmin(adminId);

        return positionRepository.findAll()
                .stream()
                .map(positionMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeePositionResponseDTO> getAllPositionsPaginated(Long adminId,
                                                                      Pageable pageable) {
        validationService.validateAdmin(adminId);

        Page<EmployeePosition> positions =
                positionRepository.findAll(
                        pageableSortMapper.map(
                                pageable,
                                DEFAULT_POSITION_SORT,
                                POSITION_SORT_FIELDS
                        )
                );

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
        validationService.validateAdmin(adminId);

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
        validationService.validateAdmin(adminId);

        if (updatedPosition.getPositionTitle() == null || updatedPosition.getPositionTitle().isBlank()) {
            throw new IllegalArgumentException("Position title cannot be null or blank");
        }

        EmployeePosition position = validationService.getPositionOrThrow(positionId);
        positionMapper.updateEntity(updatedPosition, position);

        return positionMapper.toDTO(positionRepository.save(position));
    }

    @Override
    public void deletePosition(Long adminId,
                               Long positionId) {
        validationService.validateAdmin(adminId);

        positionRepository.deleteById(positionId);
    }
}
