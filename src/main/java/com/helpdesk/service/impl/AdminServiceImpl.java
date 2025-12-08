package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.request.AdminRequestDTO;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.AdminService;
import com.helpdesk.service.mapper.EmployeeMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final EmployeeRepository employeeRepository;

    private final EmployeePositionRepository positionRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    @Autowired
    public AdminServiceImpl(EmployeeRepository employeeRepository,
                            EmployeePositionRepository positionRepository,
                            EmployeeValidationHelper employeeValidationHelper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    private Employee validateAdmin(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee findEmployee(Long adminId,
                                 Long employeeId) {
        validateAdmin(adminId);
        return getEmployeeOrThrow(employeeId);
    }

    public List<Employee> getAllEmployees(Long adminId) {
        validateAdmin(adminId);

        return employeeRepository.findAll();
    }

    public Employee createEmployee(Long adminId,
                                   AdminRequestDTO dto) {
        validateAdmin(adminId);

        Employee employee = EmployeeMapper.fromDTO(dto);

        if (employee.getEmploymentStatus() == null) {
            throw new RuntimeException("Employment status is required.");
        }

        if (dto.getPositionTitle() != null && !dto.getPositionTitle().isBlank()) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(dto.getPositionTitle());

            if (position == null) {
                throw new RuntimeException("Position not found");
            }

            employee.setEmployeePosition(position);
        }

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long adminId,
                                   Long employeeId,
                                   AdminRequestDTO dto) {
        validateAdmin(adminId);

        Employee employee = getEmployeeOrThrow(employeeId);

        EmployeeMapper.updateEntityFromDTO(dto, employee);

        if (dto.getPositionTitle() != null) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(dto.getPositionTitle());

            if (position == null) {
                throw new RuntimeException("Position not found");
            }

            employee.setEmployeePosition(position);
        }

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long adminId,
                               Long employeeId) {
        validateAdmin(adminId);
        getEmployeeOrThrow(employeeId);
        employeeRepository.deleteById(employeeId);
    }

    public Employee assignPositionToEmployee(Long adminId,
                                             Long employeeId,
                                             String positionTitle) {
        validateAdmin(adminId);

        Employee employee = getEmployeeOrThrow(employeeId);

        EmployeePosition position =
                positionRepository.findByPositionTitle(positionTitle);

        if (position == null) {
            throw new RuntimeException("Position not found");
        }

        employee.setEmployeePosition(position);
        return employeeRepository.save(employee);
    }
}
