package com.helpdesk.service.impl;

import com.helpdesk.exception.*;
import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.request.AdminCreateRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.AdminService;
import com.helpdesk.service.mapper.AdminMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final EmployeeRepository employeeRepository;

    private final EmployeePositionRepository positionRepository;

    private final TicketRepository ticketRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    @Autowired
    public AdminServiceImpl(EmployeeRepository employeeRepository,
                            EmployeePositionRepository positionRepository,
                            TicketRepository ticketRepository,
                            EmployeeValidationHelper employeeValidationHelper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.ticketRepository = ticketRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    private Employee validateAdmin(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    @Override
    public AdminResponseDTO findEmployee(Long adminId,
                                         Long employeeId) {
        validateAdmin(adminId);

        Employee employee = employeeRepository.findByIdWithTickets(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        return AdminMapper.toDTO(employee);
    }

    @Override
    public List<AdminResponseDTO> getAllEmployees(Long adminId) {
        validateAdmin(adminId);

        return employeeRepository.findAll()
                .stream()
                .map(AdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AdminResponseDTO> getAllEmployeesPaginated(Long adminId,
                                                           int page,
                                                           int size) {
        validateAdmin(adminId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeRepository.findAll(pageable);

        if (employees.isEmpty()) {
            throw new EmptyPageException(page, "Contains no employees");
        }

        return employees.map(AdminMapper::toDTO);
    }

    @Override
    public AdminResponseDTO createEmployee(Long adminId,
                                           AdminCreateRequestDTO dto) {
        validateAdmin(adminId);
        Employee employee = AdminMapper.toEntity(dto);

        if (employee.getEmploymentStatus() == null) {
            throw new InvalidEmployeeFormException("Employment status is required.");
        }

        if (employee.getEmployeeName() == null) {
            throw new InvalidEmployeeFormException("Employee name is required.");
        }

        if (dto.getPositionTitle() != null && !dto.getPositionTitle().isBlank()) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(dto.getPositionTitle());

            if (position == null) {
                throw new EmployeePositionNotFoundException("Position not found");
            }

            employee.setEmployeePosition(position);
        }

        return AdminMapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    public AdminResponseDTO updateEmployee(Long adminId,
                                           Long employeeId,
                                           AdminCreateRequestDTO dto) {
        validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        AdminMapper.updateEntity(dto, employee);

        if (dto.getPositionTitle() != null && !dto.getPositionTitle().isBlank()) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(dto.getPositionTitle());

            if (position == null) {
                throw new EmployeePositionNotFoundException("Position not found");
            }

            employee.setEmployeePosition(position);
        }

        return AdminMapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long adminId,
                               Long employeeId) {
        validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        employee.getAssignedTickets().forEach(ticket -> ticket.setTicketAssignee(null));
        ticketRepository.saveAll(employee.getAssignedTickets());

        employeeRepository.deleteById(employeeId);
    }

    @Override
    public AdminResponseDTO assignPositionToEmployee(Long adminId,
                                             Long employeeId,
                                             String positionTitle) {
        validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        EmployeePosition position =
                positionRepository.findByPositionTitle(positionTitle);

        if (position == null) {
            throw new EmployeePositionNotFoundException("Position not found");
        }

        employee.setEmployeePosition(position);
        return AdminMapper.toDTO(employeeRepository.save(employee));
    }
}
