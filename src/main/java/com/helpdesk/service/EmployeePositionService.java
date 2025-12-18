package com.helpdesk.service;

import com.helpdesk.model.request.EmployeePositionRequestDTO;
import com.helpdesk.model.response.EmployeePositionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeePositionService {
    EmployeePositionResponseDTO findPosition(Long adminId, Long positionId);

    Page<EmployeePositionResponseDTO> getAllPositionsPaginated(Long adminId, Pageable pageable);

    List<EmployeePositionResponseDTO> getAllPositions(Long adminId);

    EmployeePositionResponseDTO createPosition(Long adminId, EmployeePositionRequestDTO createdPosition);

    EmployeePositionResponseDTO updatePosition(Long adminId, Long positionId, EmployeePositionRequestDTO updatedPosition);

    void deletePosition(Long adminId, Long positionId);
}
