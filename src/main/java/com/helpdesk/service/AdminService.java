package com.helpdesk.service;

import com.helpdesk.model.request.AdminCreateRequestDTO;
import com.helpdesk.model.request.AdminUpdateRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    AdminResponseDTO findEmployee(Long adminId, Long employeeId);

    Page<AdminResponseDTO> getAllEmployeesPaginated(Long adminId, int page, int size);

    List<AdminResponseDTO> getAllEmployees(Long adminId);

    AdminResponseDTO createEmployee(Long adminId, AdminCreateRequestDTO dto);

    AdminResponseDTO updateEmployee(Long adminId, Long employeeId, AdminUpdateRequestDTO dto);

    void deleteEmployee(Long adminId, Long employeeId);

    AdminResponseDTO assignPositionToEmployee(Long adminId, Long employeeId, String positionTitle);
}
