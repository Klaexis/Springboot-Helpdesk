package com.helpdesk.service.mapper;

import com.helpdesk.model.Employee;
import com.helpdesk.model.response.EmployeeProfileResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeProfileResponseDTO toProfileDTO(Employee employee) {
        if (employee == null) return null;

        EmployeeProfileResponseDTO dto = new EmployeeProfileResponseDTO();
        dto.setId(employee.getId());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setEmployeeAge(employee.getEmployeeAge());
        dto.setEmployeeAddress(employee.getEmployeeAddress());
        dto.setEmployeeContactNumber(employee.getEmployeeContactNumber());
        dto.setEmployeeEmail(employee.getEmployeeEmail());
        dto.setPositionTitle(employee.getEmployeePosition() != null ? employee.getEmployeePosition().getPositionTitle() : null);
        dto.setEmploymentStatus(employee.getEmploymentStatus() != null ? employee.getEmploymentStatus().name() : null);

        return dto;
    }
}
