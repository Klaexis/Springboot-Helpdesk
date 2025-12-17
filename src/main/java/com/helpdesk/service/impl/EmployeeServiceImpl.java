package com.helpdesk.service.impl;

import com.helpdesk.exception.EmployeeNotFoundException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.response.EmployeeProfileResponseDTO;
import com.helpdesk.repository.EmployeeRepository;

import com.helpdesk.service.EmployeeService;
import com.helpdesk.service.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeProfileResponseDTO viewOwnProfile(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        return employeeMapper.toProfileDTO(employee);
    }
}
