package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.request.AdminRequestDTO;
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
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public AdminResponseDTO findEmployee(Long adminId,
                                         Long employeeId) {
        validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);
        return AdminMapper.toDTO(employee);
    }

    public List<AdminResponseDTO> getAllEmployees(Long adminId) {
        validateAdmin(adminId);

        return employeeRepository.findAll()
                .stream()
                .map(AdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AdminResponseDTO createEmployee(Long adminId,
                                           AdminRequestDTO dto) {
        validateAdmin(adminId);
        Employee employee = AdminMapper.fromDTO(dto);

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

        return AdminMapper.toDTO(employeeRepository.save(employee));
    }

    public AdminResponseDTO updateEmployee(Long adminId,
                                           Long employeeId,
                                           AdminRequestDTO dto) {
        validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        AdminMapper.updateEntityFromDTO(dto, employee);

        if (dto.getPositionTitle() != null) {
            EmployeePosition position =
                    positionRepository.findByPositionTitle(dto.getPositionTitle());

            if (position == null) {
                throw new RuntimeException("Position not found");
            }

            employee.setEmployeePosition(position);
        }

        return AdminMapper.toDTO(employeeRepository.save(employee));
    }

    public void deleteEmployee(Long adminId,
                               Long employeeId) {
        validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        employee.getAssignedTickets().forEach(ticket -> ticket.setTicketAssignee(null));
        ticketRepository.saveAll(employee.getAssignedTickets());

        employeeRepository.deleteById(employeeId);
    }

    public AdminResponseDTO assignPositionToEmployee(Long adminId,
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
        return AdminMapper.toDTO(employeeRepository.save(employee));
    }
}
