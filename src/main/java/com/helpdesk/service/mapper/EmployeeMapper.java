package com.helpdesk.service.mapper;

import com.helpdesk.model.Employee;
import com.helpdesk.model.request.AdminRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.model.response.EmployeePositionResponseDTO;

public class EmployeeMapper {

    public static AdminResponseDTO toDTO(Employee employee) {
        EmployeePositionResponseDTO positionDTO = null;
        if (employee.getEmployeePosition() != null) {
            positionDTO = new EmployeePositionResponseDTO(
                    employee.getEmployeePosition().getPositionId(),
                    employee.getEmployeePosition().getPositionTitle()
            );
        }

        return new AdminResponseDTO(
                employee.getEmployeeId(),
                employee.getEmployeeName(),
                employee.getEmployeeAge(),
                employee.getEmployeeAddress(),
                employee.getEmployeeContactNumber(),
                employee.getEmployeeEmail(),
                positionDTO,
                employee.getEmploymentStatus()
        );
    }

    public static Employee fromDTO(AdminRequestDTO dto) {
        Employee employee = new Employee();
        employee.setEmployeeName(dto.getEmployeeName());
        employee.setEmployeeAge(dto.getEmployeeAge());
        employee.setEmployeeAddress(dto.getEmployeeAddress());
        employee.setEmployeeContactNumber(dto.getEmployeeContactNumber());
        employee.setEmployeeEmail(dto.getEmployeeEmail());
        employee.setEmploymentStatus(dto.getEmploymentStatus());

        // service will fetch by title or ID of employee position
        return employee;
    }

    public static void updateEmployeeFromDTO(Employee employee, AdminRequestDTO dto) {
        if (dto.getEmployeeName() != null) employee.setEmployeeName(dto.getEmployeeName());
        if (dto.getEmployeeAge() != null) employee.setEmployeeAge(dto.getEmployeeAge());
        if (dto.getEmployeeAddress() != null) employee.setEmployeeAddress(dto.getEmployeeAddress());
        if (dto.getEmployeeContactNumber() != null) employee.setEmployeeContactNumber(dto.getEmployeeContactNumber());
        if (dto.getEmployeeEmail() != null) employee.setEmployeeEmail(dto.getEmployeeEmail());
        if (dto.getEmploymentStatus() != null) employee.setEmploymentStatus(dto.getEmploymentStatus());
        // Position will be handled in service
    }
}
