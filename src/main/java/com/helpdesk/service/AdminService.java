package com.helpdesk.service;

import com.helpdesk.model.request.AdminRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    AdminResponseDTO findEmployee(Long adminId, Long employeeId);

    Page<AdminResponseDTO> getAllEmployeesPaginated(Long adminId, int page, int size);

    List<AdminResponseDTO> getAllEmployees(Long adminId);

    AdminResponseDTO createEmployee(Long adminId, AdminRequestDTO dto);

    AdminResponseDTO updateEmployee(Long adminId, Long employeeId, AdminRequestDTO dto);

    void deleteEmployee(Long adminId, Long employeeId);

    AdminResponseDTO assignPositionToEmployee(Long adminId, Long employeeId, String positionTitle);
}
