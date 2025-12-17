package com.helpdesk.service;

import com.helpdesk.model.EmploymentStatus;
import com.helpdesk.model.request.AdminCreateRequestDTO;
import com.helpdesk.model.request.AdminUpdateRequestDTO;
import com.helpdesk.model.request.AssignPositionRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    AdminResponseDTO findEmployee(Long adminId, Long employeeId);

    Page<AdminResponseDTO> getAllEmployeesPaginated(Long adminId, int page, int size, String sortBy, String direction);

    List<AdminResponseDTO> getAllEmployees(Long adminId);

    AdminResponseDTO createEmployee(Long adminId, AdminCreateRequestDTO dto);

    AdminResponseDTO updateEmployee(Long adminId, Long employeeId, AdminUpdateRequestDTO dto);

    void deleteEmployee(Long adminId, Long employeeId);

    AdminResponseDTO assignPositionToEmployee(Long adminId, Long employeeId, AssignPositionRequestDTO positionTitle);

    AdminResponseDTO unassignPositionFromEmployee(Long adminId, Long employeeId);

    Page<AdminResponseDTO> searchEmployees(
            Long adminId,
            String name,
            String positionTitle,
            EmploymentStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
