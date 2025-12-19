package com.helpdesk.service.impl;

import com.helpdesk.exception.AdminNotFoundException;
import com.helpdesk.exception.EmployeeNotFoundException;
import com.helpdesk.exception.EmployeePositionNotFoundException;
import com.helpdesk.exception.TicketNotFoundException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.Ticket;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.ValidationService;
import com.helpdesk.service.util.EmployeeValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ValidationServiceImpl implements ValidationService {
    private final EmployeeRepository employeeRepository;

    private final TicketRepository ticketRepository;

    private final EmployeePositionRepository positionRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    public ValidationServiceImpl(EmployeeRepository employeeRepository,
                             TicketRepository ticketRepository,
                             EmployeePositionRepository positionRepository,
                             EmployeeValidationHelper employeeValidationHelper) {
        this.employeeRepository = employeeRepository;
        this.ticketRepository = ticketRepository;
        this.positionRepository = positionRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    public Employee validateAdmin(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    public Employee validateEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        employeeValidationHelper.validateActive(employee);
        return employee;
    }

    public Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    public Ticket getTicketOrThrow(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    public EmployeePosition getPositionOrThrow(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new EmployeePositionNotFoundException(positionId));
    }

}
