package com.helpdesk.service.impl;

import com.helpdesk.exception.EmptyPageException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.AdminTicketSearchRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.specification.TicketSpecification;
import com.helpdesk.service.AdminTicketService;
import com.helpdesk.service.ValidationService;
import com.helpdesk.service.mapper.TicketMapper;

import com.helpdesk.service.util.PageableSortMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminTicketServiceImpl implements AdminTicketService {
    private final TicketRepository ticketRepository;

    private final EmployeeRepository employeeRepository;

    private final TicketMapper ticketMapper;

    private final TicketSpecification ticketSpecification;

    private final PageableSortMapper pageableSortMapper;

    private final ValidationService validationService;

    public AdminTicketServiceImpl(TicketRepository ticketRepository,
                                  EmployeeRepository employeeRepository,
                                  TicketMapper ticketMapper,
                                  TicketSpecification ticketSpecification,
                                  PageableSortMapper pageableSortMapper,
                                  ValidationService validationService) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.ticketMapper = ticketMapper;
        this.ticketSpecification = ticketSpecification;
        this.pageableSortMapper = pageableSortMapper;
        this.validationService = validationService;
    }

    private static final Map<String, String> TICKET_SORT_FIELDS = Map.of(
            "createdat","ticketCreatedDate",
            "updatedat", "ticketUpdatedDate",
            "status", "ticketStatus",
            "title", "ticketTitle"
    );

    private static final String DEFAULT_TICKET_SORT = "ticketCreatedDate";

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
    public TicketResponseDTO assignTicket(Long ticketId,
                                          Long adminId,
                                          Long employeeId) {
        Ticket ticket = validationService.getTicketOrThrow(ticketId);
        Employee admin = validationService.validateAdmin(adminId);
        Employee employee = validationService.getEmployeeOrThrow(employeeId);

        ticket.setTicketAssignee(employee);
        ticket.setTicketUpdatedBy(admin);
        ticket.setTicketStatus(TicketStatus.IN_PROGRESS);

        if (!employee.getAssignedTickets().contains(ticket)) {
            employee.getAssignedTickets().add(ticket);
        }

        employeeRepository.save(employee);

        return ticketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    @Override
    public TicketResponseDTO getTicket(Long adminId,
                                       Long ticketId) {
        validationService.validateAdmin(adminId);

        return ticketMapper.toTicketDTO(validationService.getTicketOrThrow(ticketId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketResponseDTO> getAllTickets(Long adminId) {
        validationService.validateAdmin(adminId);

        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> getAllTicketsPaginated(Long adminId,
                                                          Pageable pageable) {
        validationService.validateAdmin(adminId);

        Page<Ticket> tickets =
                ticketRepository.findAll(
                        pageableSortMapper.map(
                            pageable,
                            DEFAULT_TICKET_SORT,
                            TICKET_SORT_FIELDS
                        )
                );

        if (tickets.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "Contains no tickets"
            );
        }

        return tickets.map(ticketMapper::toTicketDTO);
    }

    @Override
    public TicketResponseDTO updateTicket(Long ticketId,
                                          TicketUpdateRequestDTO updatedTicket,
                                          Long adminId) {
        Ticket ticket = validationService.getTicketOrThrow(ticketId);
        Employee admin = validationService.validateAdmin(adminId);

        ticketMapper.updateEntity(updatedTicket, ticket);
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return ticketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO updateTicketStatus(Long ticketId,
                                                TicketStatus newStatus,
                                                Long adminId) {
        Ticket ticket = validationService.getTicketOrThrow(ticketId);
        Employee admin = validationService.validateAdmin(adminId);

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return ticketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO addTicketRemark(Long ticketId,
                                             Long adminId,
                                             String remark,
                                             TicketStatus newStatus) {
        Ticket ticket = validationService.getTicketOrThrow(ticketId);
        Employee admin = validationService.validateAdmin(adminId);

        TicketRemark ticketRemark = new TicketRemark(remark, admin, ticket);
        ticket.getTicketRemarks().add(ticketRemark);

        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return ticketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public void deleteTicket(Long adminId, Long ticketId) {
        validationService.validateAdmin(adminId);
        Ticket ticket = validationService.getTicketOrThrow(ticketId);

        Employee assignee = ticket.getTicketAssignee();
        if (assignee != null) {
            assignee.getAssignedTickets().remove(ticket);
            ticket.setTicketAssignee(null);

            // force UPDATE tickets set assignee = null
            ticketRepository.saveAndFlush(ticket);
        }

        ticketRepository.delete(ticket);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> searchTickets(Long adminId,
                                                 AdminTicketSearchRequestDTO adminTicketSearchRequestDTO,
                                                 Pageable pageable) {
        validationService.validateAdmin(adminId);

        Specification<Ticket> spec = Specification.allOf(
                ticketSpecification.hasTitle(adminTicketSearchRequestDTO.getTitle()),
                ticketSpecification.hasAssignee(adminTicketSearchRequestDTO.getAssignee()),
                ticketSpecification.hasStatus(adminTicketSearchRequestDTO.getStatus())
        );

        Page<Ticket> tickets = ticketRepository.findAll(
                spec,
                pageableSortMapper.map(
                        pageable,
                        DEFAULT_TICKET_SORT,
                        TICKET_SORT_FIELDS
                )
        );

        if (tickets.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "No tickets match the search criteria"
            );
        }

        return tickets.map(ticketMapper::toTicketDTO);
    }
}
