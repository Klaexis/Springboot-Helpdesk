package com.helpdesk.service.impl;

import com.helpdesk.exception.*;
import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.EmploymentStatus;
import com.helpdesk.model.request.AdminCreateRequestDTO;
import com.helpdesk.model.request.AdminUpdateRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.specification.EmployeeSpecification;
import com.helpdesk.service.AdminService;
import com.helpdesk.service.mapper.AdminMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public List<AdminResponseDTO> getAllEmployees(Long adminId) {
        validateAdmin(adminId);

        return employeeRepository.findAll()
                .stream()
                .map(AdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminResponseDTO> getAllEmployeesPaginated(Long adminId,
                                                           int page,
                                                           int size,
                                                           String sortBy,
                                                           String direction) {
        validateAdmin(adminId);

        String sortField = switch (sortBy.toLowerCase()) {
            case "name" -> "employeeName";
            case "position" -> "employeePosition.positionTitle";
            case "status" -> "employmentStatus";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employees = employeeRepository.findAll(pageable);

        if (employees.isEmpty()) {
            throw new EmptyPageException(page, "Contains no employees");
        }

        return employees.map(AdminMapper::toDTO);
    }

    @Override
    public AdminResponseDTO createEmployee(Long adminId,
                                           @Valid AdminCreateRequestDTO createdEmployee) {
        validateAdmin(adminId);
        Employee employee = AdminMapper.toEntity(createdEmployee);

        if (createdEmployee.getPositionTitle() != null && !createdEmployee.getPositionTitle().isBlank()) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(createdEmployee.getPositionTitle());

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
                                           AdminUpdateRequestDTO updatedEmployee) {
        validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        if (updatedEmployee.getEmployeeName() != null && updatedEmployee.getEmployeeName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        if (updatedEmployee.getEmployeeAge() != null) {
            throw new IllegalArgumentException("Age cannot be null");
        }

        if (updatedEmployee.getEmployeeEmail() != null && updatedEmployee.getEmployeeEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (updatedEmployee.getEmploymentStatus() != null && updatedEmployee.getEmploymentStatus().isBlank()) {
            throw new IllegalArgumentException("Employment status cannot be blank");
        }

        if (updatedEmployee.getPositionTitle() != null && updatedEmployee.getPositionTitle().isBlank()) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(updatedEmployee.getPositionTitle());

            if (position == null) {
                throw new EmployeePositionNotFoundException("Position not found");
            }

            employee.setEmployeePosition(position);
        }

        AdminMapper.updateEntity(updatedEmployee, employee);

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

    @Transactional(readOnly = true)
    @Override
    public Page<AdminResponseDTO> searchEmployees(
            Long adminId,
            String name,
            String positionTitle,
            EmploymentStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        validateAdmin(adminId);

        // allOf = AND, anyOf = OR
        Specification<Employee> spec = Specification.allOf(
                EmployeeSpecification.hasName(name),
                EmployeeSpecification.hasPosition(positionTitle),
                EmployeeSpecification.hasStatus(status)
        );

        // Sorting
        String sortField = switch (sortBy.toLowerCase()) {
            case "name" -> "employeeName";
            case "position" -> "employeePosition.positionTitle";
            case "status" -> "employmentStatus";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employees = employeeRepository.findAll(spec, pageable);

        if (employees.isEmpty()) {
            throw new EmptyPageException(page, "No employees match the search criteria");
        }

        return employees.map(AdminMapper::toDTO);
    }
}
