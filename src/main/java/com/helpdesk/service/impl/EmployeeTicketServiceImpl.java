package com.helpdesk.service.impl;

import com.helpdesk.exception.*;
import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketCreateRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.specification.TicketSpecification;
import com.helpdesk.service.EmployeeTicketService;
import com.helpdesk.service.mapper.TicketMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeTicketServiceImpl implements EmployeeTicketService {
    private final TicketRepository ticketRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    @Autowired
    public EmployeeTicketServiceImpl(TicketRepository ticketRepository,
                                     EmployeeRepository employeeRepository,
                                     EmployeeValidationHelper employeeValidationHelper) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    private Employee validateEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        employeeValidationHelper.validateActive(employee);

        return employee;
    }

    private Ticket getTicketOrThrow(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    private void handleTicketClosure(Ticket ticket) {
        if (ticket.getTicketStatus() == TicketStatus.CLOSED) {
            Employee assignee = ticket.getTicketAssignee();
            if (assignee != null) {
                assignee.getAssignedTickets().remove(ticket);
                ticket.setTicketAssignee(null);
            }
        }
    }

    @Override
    public TicketResponseDTO fileTicket(TicketCreateRequestDTO ticket,
                                        Long employeeId) {
        Employee employee = validateEmployee(employeeId);

        Ticket newTicket = TicketMapper.toEntity(ticket);
        newTicket.setTicketCreatedBy(employee);

        boolean hasTitle = ticket.getTicketTitle() != null && !ticket.getTicketTitle().isBlank();
        boolean hasBody = ticket.getTicketBody() != null && !ticket.getTicketBody().isBlank();

        if (hasTitle && hasBody) {
            newTicket.setTicketStatus(TicketStatus.FILED);
        } else {
            newTicket.setTicketStatus(TicketStatus.DRAFT);
        }

        return TicketMapper.toTicketDTO(ticketRepository.save(newTicket));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketResponseDTO> viewAssignedTickets(Long employeeId) {
        validateEmployee(employeeId);

        return ticketRepository.findByTicketAssigneeEmployeeId(employeeId)
                .stream()
                .map(TicketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> viewAssignedTicketsPaginated(Long employeeId,
                                                                int page,
                                                                int size,
                                                                String sortBy,
                                                                String direction) {
        validateEmployee(employeeId);

        String sortField = switch (sortBy.toLowerCase()) {
            case "createdat" -> "ticketCreatedAt";
            case "updatedat" -> "ticketUpdatedAt";
            case "status"    -> "ticketStatus";
            case "title"     -> "ticketTitle";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Ticket> tickets = ticketRepository.findByTicketAssigneeEmployeeId(employeeId, pageable);

        if (tickets.isEmpty()) {
            throw new EmptyPageException(page, "No tickets found");
        }

        return tickets.map(TicketMapper::toTicketDTO);
    }

    @Override
    public TicketResponseDTO updateOwnTicket(Long ticketId,
                                             TicketUpdateRequestDTO updatedTicket,
                                             Long employeeId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee employee = validateEmployee(employeeId);

        if (!ticket.getTicketCreatedBy().getEmployeeId().equals(employeeId)) {
            throw new TicketAccessException("You can only update your own tickets");
        }

        TicketMapper.updateEntity(updatedTicket, ticket);
        ticket.setTicketUpdatedBy(employee);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO updateOwnTicketStatus(Long ticketId,
                                                   TicketStatus newStatus,
                                                   Long employeeId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee employee = validateEmployee(employeeId);

        Employee assignee = ticket.getTicketAssignee();
        if (assignee == null || !assignee.getEmployeeId().equals(employeeId)) {
            throw new TicketAccessException("You can only update your assigned tickets");
        }

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO addRemarkToAssignedTicket(Long ticketId,
                                                       Long employeeId,
                                                       String remark,
                                                       TicketStatus newStatus) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee employee = validateEmployee(employeeId);

        Employee assignee = ticket.getTicketAssignee();
        if (assignee == null || !assignee.getEmployeeId().equals(employeeId)) {
            throw new TicketAccessException("You can only add remarks to tickets assigned to you");
        }

        ticket.getTicketRemarks().add(new TicketRemark(remark, employee, ticket));
        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }

        ticket.setTicketUpdatedBy(employee);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketResponseDTO> getAllFiledTickets(Long employeeId) {
        validateEmployee(employeeId);

        List<Ticket> filedTickets = ticketRepository.findByTicketStatus(TicketStatus.FILED);

        return filedTickets.stream()
                .map(TicketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> getAllFiledTicketsPaginated(Long employeeId,
                                                               int page,
                                                               int size,
                                                               String sortBy,
                                                               String direction) {
        validateEmployee(employeeId);

        String sortField = switch (sortBy.toLowerCase()) {
            case "createdat" -> "ticketCreatedAt";
            case "updatedat" -> "ticketUpdatedAt";
            case "title"     -> "ticketTitle";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Ticket> tickets = ticketRepository.findByTicketStatus(TicketStatus.FILED, pageable);

        return tickets.map(TicketMapper::toTicketDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public TicketResponseDTO getFiledTicket(Long employeeId,
                                            Long ticketId) {
        validateEmployee(employeeId);
        Ticket ticket = getTicketOrThrow(ticketId);

        if (ticket.getTicketStatus() != TicketStatus.FILED) {
            throw new IllegalArgumentException("Ticket is not in FILED status");
        }

        return TicketMapper.toTicketDTO(ticket);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> searchAssignedTickets(Long employeeId,
                                                         String title,
                                                         LocalDate createdDate,
                                                         LocalDate updatedDate,
                                                         TicketStatus status,
                                                         int page,
                                                         int size,
                                                         String sortBy,
                                                         String direction) {
        validateEmployee(employeeId);

        Specification<Ticket> spec = Specification.allOf(
                TicketSpecification.hasTitle(title),
                TicketSpecification.hasCreatedDate(createdDate),
                TicketSpecification.hasUpdatedDate(updatedDate),
                TicketSpecification.hasStatus(status)
        );

        String sortField = switch (sortBy.toLowerCase()) {
            case "title"  -> "ticketTitle";
            case "createdBy" -> "ticketCreatedBy";
            case "updatedBy" -> "ticketUpdatedBy";
            case "status" -> "ticketStatus";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Ticket> tickets = ticketRepository.findAll(spec, pageable);

        if (tickets.isEmpty()) {
            throw new EmptyPageException(page, "No assigned tickets match the search criteria");
        }

        return tickets.map(TicketMapper::toTicketDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> searchFiledTickets(Long employeeId,
                                                      String title,
                                                      LocalDate createdDate,
                                                      LocalDate updatedDate,
                                                      int page,
                                                      int size,
                                                      String sortBy,
                                                      String direction) {
        validateEmployee(employeeId);

        Specification<Ticket> spec = Specification.allOf(
                TicketSpecification.hasStatus(TicketStatus.FILED),
                TicketSpecification.hasTitle(title),
                TicketSpecification.hasCreatedDate(createdDate),
                TicketSpecification.hasUpdatedDate(updatedDate)
        );

        String sortField = switch (sortBy.toLowerCase()) {
            case "title" -> "ticketTitle";
            case "createdBy" -> "ticketCreatedBy";
            case "updatedBy" -> "ticketUpdatedBy";
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        };

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Ticket> tickets = ticketRepository.findAll(spec, pageable);

        if (tickets.isEmpty()) {
            throw new EmptyPageException(page, "No filed tickets match the search criteria");
        }

        return tickets.map(TicketMapper::toTicketDTO);
    }
}
