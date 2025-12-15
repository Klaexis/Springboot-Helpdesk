package com.helpdesk.service.impl;

import com.helpdesk.exception.AdminNotFoundException;
import com.helpdesk.exception.EmployeeNotFoundException;
import com.helpdesk.exception.EmptyPageException;
import com.helpdesk.exception.TicketNotFoundException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.specification.TicketSpecification;
import com.helpdesk.service.AdminTicketService;
import com.helpdesk.service.mapper.TicketMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminTicketServiceImpl implements AdminTicketService {
    private final TicketRepository ticketRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    @Autowired
    public AdminTicketServiceImpl(TicketRepository ticketRepository,
                                  EmployeeRepository employeeRepository,
                                  EmployeeValidationHelper employeeValidationHelper) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
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

    private Ticket getTicketOrThrow(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    private void handleTicketClosure(Ticket ticket) {
        if (ticket.getTicketStatus() == TicketStatus.CLOSED && ticket.getTicketAssignee() != null) {
            Employee assignee = ticket.getTicketAssignee();
            assignee.getAssignedTickets().remove(ticket); // remove ticket from employee
            ticket.setTicketAssignee(null); // optional: clear assignee
            employeeRepository.save(assignee); // persist change
        }
    }

    @Override
    public TicketResponseDTO assignTicket(Long ticketId,
                                          Long adminId,
                                          Long employeeId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        ticket.setTicketAssignee(employee);
        ticket.setTicketUpdatedBy(admin);
        ticket.setTicketStatus(TicketStatus.IN_PROGRESS);

        if (!employee.getAssignedTickets().contains(ticket)) {
            employee.getAssignedTickets().add(ticket);
        }

        employeeRepository.save(employee);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO getTicket(Long adminId,
                                       Long ticketId) {
        validateAdmin(adminId);

        return TicketMapper.toTicketDTO(getTicketOrThrow(ticketId));
    }

    @Override
    public List<TicketResponseDTO> getAllTickets(Long adminId) {
        validateAdmin(adminId);

        return ticketRepository.findAll()
                .stream()
                .map(TicketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TicketResponseDTO> getAllTicketsPaginated(Long adminId,
                                                          int page,
                                                          int size,
                                                          String sortBy,
                                                          String direction) {
        validateAdmin(adminId);

        String sortField = switch (sortBy.toLowerCase()) {
            case "createdat" -> "ticketCreatedAt";
            case "updatedat" -> "ticketUpdatedAt";
            case "status" -> "ticketStatus";
            case "title" -> "ticketTitle";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Ticket> tickets = ticketRepository.findAll(pageable);

        if (tickets.isEmpty()) {
            throw new EmptyPageException(page, "Contains no tickets");
        }

        return tickets.map(TicketMapper::toTicketDTO);
    }

    @Override
    public TicketResponseDTO updateTicket(Long ticketId,
                                          TicketUpdateRequestDTO updatedTicket,
                                          Long adminId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        TicketMapper.updateEntity(updatedTicket, ticket);
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO updateTicketStatus(Long ticketId,
                                                TicketStatus newStatus,
                                                Long adminId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO addTicketRemark(Long ticketId,
                                             Long adminId,
                                             String remark,
                                             TicketStatus newStatus) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        TicketRemark ticketRemark = new TicketRemark(remark, admin, ticket);
        ticket.getTicketRemarks().add(ticketRemark);

        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public Page<TicketResponseDTO> searchTickets(Long adminId,
                                                 String title,
                                                 String assignee,
                                                 TicketStatus status,
                                                 int page,
                                                 int size,
                                                 String sortBy,
                                                 String direction) {
        validateAdmin(adminId);

        Specification<Ticket> spec = Specification.allOf(
                TicketSpecification.hasTitle(title),
                TicketSpecification.hasAssignee(assignee),
                TicketSpecification.hasStatus(status)
        );

        String sortField = switch (sortBy.toLowerCase()) {
            case "title"  -> "ticketTitle";
            case "assignee"  -> "ticketAssignee";
            case "status" -> "ticketStatus";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Ticket> tickets = ticketRepository.findAll(spec, pageable);

        if (tickets.isEmpty()) {
            throw new EmptyPageException(page, "No tickets match the search criteria");
        }

        return tickets.map(TicketMapper::toTicketDTO);
    }
}
