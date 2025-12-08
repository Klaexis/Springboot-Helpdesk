package com.helpdesk.service.mapper;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.EmploymentStatus;
import com.helpdesk.model.request.AdminRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;

public class AdminMapper {

    /** For GET operations (and after create/update) */
    public static AdminResponseDTO toDTO(Employee employee) {
        if (employee == null) return null;

        AdminResponseDTO dto = new AdminResponseDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setEmployeeAge(employee.getEmployeeAge());
        dto.setEmployeeAddress(employee.getEmployeeAddress());
        dto.setEmployeeContactNumber(employee.getEmployeeContactNumber());
        dto.setEmployeeEmail(employee.getEmployeeEmail());
        dto.setEmploymentStatus(
                employee.getEmploymentStatus() != null ? employee.getEmploymentStatus().name() : null
        );

        EmployeePosition pos = employee.getEmployeePosition();
        dto.setPositionTitle(pos != null ? pos.getPositionTitle() : null);

        return dto;
    }

    /** For CREATE: map basic fields; position is ignored here (set via service) */
    public static Employee fromDTO(AdminRequestDTO dto) {
        if (dto == null) return null;

        Employee employee = new Employee();
        employee.setEmployeeName(dto.getEmployeeName());
        employee.setEmployeeAge(dto.getEmployeeAge());
        employee.setEmployeeAddress(dto.getEmployeeAddress());
        employee.setEmployeeContactNumber(dto.getEmployeeContactNumber());
        employee.setEmployeeEmail(dto.getEmployeeEmail());

        if (dto.getEmploymentStatus() != null) {
            employee.setEmploymentStatus(
                    EmploymentStatus.valueOf(dto.getEmploymentStatus())
            );
        }

        return employee;
    }

    /** For UPDATE: apply only non-null fields (except position) */
    public static void updateEntityFromDTO(AdminRequestDTO dto, Employee employee) {
        if (dto.getEmployeeName() != null) employee.setEmployeeName(dto.getEmployeeName());
        if (dto.getEmployeeAge() != null) employee.setEmployeeAge(dto.getEmployeeAge());
        if (dto.getEmployeeAddress() != null) employee.setEmployeeAddress(dto.getEmployeeAddress());
        if (dto.getEmployeeContactNumber() != null) employee.setEmployeeContactNumber(dto.getEmployeeContactNumber());
        if (dto.getEmployeeEmail() != null) employee.setEmployeeEmail(dto.getEmployeeEmail());

        if (dto.getEmploymentStatus() != null) {
            employee.setEmploymentStatus(
                    EmploymentStatus.valueOf(dto.getEmploymentStatus())
            );
        }
    }
}

