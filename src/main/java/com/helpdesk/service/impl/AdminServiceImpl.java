package com.helpdesk.service.impl;

import com.helpdesk.exception.EmployeeNotFoundException;
import com.helpdesk.exception.EmployeePositionNotFoundException;
import com.helpdesk.exception.EmptyPageException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.request.AdminCreateRequestDTO;
import com.helpdesk.model.request.AdminSearchRequestDTO;
import com.helpdesk.model.request.AdminUpdateRequestDTO;
import com.helpdesk.model.request.AssignPositionRequestDTO;
import com.helpdesk.model.response.AdminResponseDTO;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.specification.EmployeeSpecification;
import com.helpdesk.service.AdminService;
import com.helpdesk.service.ValidationService;
import com.helpdesk.service.mapper.AdminMapper;
import com.helpdesk.service.util.PageableSortMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    private final EmployeeRepository employeeRepository;

    private final EmployeePositionRepository positionRepository;

    private final TicketRepository ticketRepository;

    private final AdminMapper adminMapper;

    private final EmployeeSpecification employeeSpecification;

    private final PageableSortMapper  pageableSortMapper;

    private final ValidationService validationService;

    public AdminServiceImpl(EmployeeRepository employeeRepository,
                            EmployeePositionRepository positionRepository,
                            TicketRepository ticketRepository,
                            AdminMapper adminMapper,
                            EmployeeSpecification employeeSpecification,
                            PageableSortMapper pageableSortMapper,
                            ValidationService validationService) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.ticketRepository = ticketRepository;
        this.adminMapper = adminMapper;
        this.employeeSpecification = employeeSpecification;
        this.pageableSortMapper = pageableSortMapper;
        this.validationService = validationService;
    }

    private static final Map<String, String> EMPLOYEE_SORT_FIELDS = Map.of(
            "name", "employeeName",
            "position", "employeePosition.positionTitle",
            "status", "employmentStatus"
    );

    private static final String DEFAULT_EMPLOYEE_SORT = "employeeName";

    @Transactional(readOnly = true)
    @Override
    public AdminResponseDTO findEmployee(Long adminId,
                                         Long employeeId) {
        validationService.validateAdmin(adminId);

        Employee employee = employeeRepository.findByIdWithTickets(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        return adminMapper.toDTO(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminResponseDTO> getAllEmployees(Long adminId) {
        validationService.validateAdmin(adminId);

        return employeeRepository.findAll()
                .stream()
                .map(adminMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminResponseDTO> getAllEmployeesPaginated(Long adminId,
                                                           Pageable pageable) {
        validationService.validateAdmin(adminId);

        Page<Employee> employees =
                employeeRepository.findAll(
                        pageableSortMapper.map(
                                pageable,
                                DEFAULT_EMPLOYEE_SORT,
                                EMPLOYEE_SORT_FIELDS
                        )
                );

        if (employees.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "Contains no employees"
            );
        }

        return employees.map(adminMapper::toDTO);
    }

    @Override
    public AdminResponseDTO createEmployee(Long adminId,
                                           @Valid AdminCreateRequestDTO createdEmployee) {
        validationService.validateAdmin(adminId);
        Employee employee = adminMapper.toEntity(createdEmployee);

        if (createdEmployee.getPositionTitle() != null && !createdEmployee.getPositionTitle().isBlank()) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(createdEmployee.getPositionTitle());

            if (position == null) {
                throw new EmployeePositionNotFoundException("Position not found");
            }

            employee.setEmployeePosition(position);
        }

        return adminMapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    public AdminResponseDTO updateEmployee(Long adminId,
                                           Long employeeId,
                                           AdminUpdateRequestDTO updatedEmployee) {
        validationService.validateAdmin(adminId);
        Employee employee = validationService.getEmployeeOrThrow(employeeId);

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

        adminMapper.updateEntity(updatedEmployee, employee);

        return adminMapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long adminId,
                               Long employeeId) {
        validationService.validateAdmin(adminId);
        Employee employee = validationService.getEmployeeOrThrow(employeeId);

        employee.getAssignedTickets().forEach(ticket -> ticket.setTicketAssignee(null));
        ticketRepository.saveAll(employee.getAssignedTickets());

        employeeRepository.deleteById(employeeId);
    }

    @Override
    public AdminResponseDTO assignPositionToEmployee(Long adminId,
                                                     Long employeeId,
                                                     AssignPositionRequestDTO positionTitle) {
        validationService.validateAdmin(adminId);
        Employee employee = validationService.getEmployeeOrThrow(employeeId);

        EmployeePosition position =
                positionRepository.findByPositionTitle(positionTitle.getPositionTitle());

        if (position == null) {
            throw new EmployeePositionNotFoundException("Position not found");
        }

        employee.setEmployeePosition(position);
        return adminMapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public AdminResponseDTO unassignPositionFromEmployee(Long adminId,
                                                         Long employeeId) {
        validationService.validateAdmin(adminId);

        Employee employee = validationService.getEmployeeOrThrow(employeeId);

        if (employee.getEmployeePosition() == null) {
            throw new IllegalStateException("Employee has no assigned position");
        }

        employee.setEmployeePosition(null);
        return adminMapper.toDTO(employeeRepository.save(employee));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminResponseDTO> searchEmployees(
            Long adminId,
            AdminSearchRequestDTO adminSearchRequestDTO,
            Pageable pageable
    ) {
        validationService.validateAdmin(adminId);

        // allOf = AND, anyOf = OR
        Specification<Employee> spec = Specification.allOf(
                employeeSpecification.hasName(adminSearchRequestDTO.getName()),
                employeeSpecification.hasPosition(adminSearchRequestDTO.getPosition()),
                employeeSpecification.hasStatus(adminSearchRequestDTO.getStatus())
        );

        Page<Employee> employees =
                employeeRepository.findAll(
                        spec,
                        pageableSortMapper.map(
                                pageable,
                                DEFAULT_EMPLOYEE_SORT,
                                EMPLOYEE_SORT_FIELDS
                        )
                );

        if (employees.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "No employees match the search criteria"
            );
        }

        return employees.map(adminMapper::toDTO);
    }
}
