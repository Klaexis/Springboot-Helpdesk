package com.helpdesk.service.mapper;

import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.request.EmployeePositionRequestDTO;
import com.helpdesk.model.response.EmployeePositionResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeePositionMapper {
    public EmployeePositionResponseDTO toDTO(EmployeePosition position) {
        if (position == null) return null;

        EmployeePositionResponseDTO dto = new EmployeePositionResponseDTO();
        dto.setId(position.getId());
        dto.setPositionTitle(position.getPositionTitle());
        return dto;
    }

    // Convert RequestDTO -> Entity
    public EmployeePosition toEntity(EmployeePositionRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        EmployeePosition position = new EmployeePosition();
        position.setPositionTitle(requestDTO.getPositionTitle());
        return position;
    }

    // Update Entity from RequestDTO
    public void updateEntity(EmployeePositionRequestDTO requestDTO, EmployeePosition position) {
        if (requestDTO.getPositionTitle() != null && !requestDTO.getPositionTitle().isBlank()) {
            position.setPositionTitle(requestDTO.getPositionTitle());
        }
    }
}
