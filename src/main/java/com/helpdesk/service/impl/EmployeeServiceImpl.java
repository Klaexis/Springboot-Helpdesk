package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.response.EmployeeProfileResponseDTO;
import com.helpdesk.repository.EmployeeRepository;

import com.helpdesk.service.EmployeeService;
import com.helpdesk.service.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeProfileResponseDTO viewOwnProfile(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Simply return mapped DTO
        return EmployeeMapper.toProfileDTO(employee);
    }
}
