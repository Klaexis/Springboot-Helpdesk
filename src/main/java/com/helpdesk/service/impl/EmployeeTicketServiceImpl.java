package com.helpdesk.service.impl;

import com.helpdesk.exception.TicketAccessException;
import com.helpdesk.exception.EmptyPageException;
import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketCreateRequestDTO;
import com.helpdesk.model.request.TicketSearchAssignedRequestDTO;
import com.helpdesk.model.request.TicketSearchFiledRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.specification.TicketSpecification;
import com.helpdesk.service.EmployeeTicketService;
import com.helpdesk.service.ValidationService;
import com.helpdesk.service.mapper.TicketMapper;

import com.helpdesk.service.util.PageableSortMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EmployeeTicketServiceImpl implements EmployeeTicketService {
    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    private final TicketSpecification ticketSpecification;

    private final PageableSortMapper pageableSortMapper;

    private final ValidationService validationService;

    @Autowired
    public EmployeeTicketServiceImpl(TicketRepository ticketRepository,
                                     TicketMapper ticketMapper,
                                     TicketSpecification ticketSpecification,
                                     PageableSortMapper pageableSortMapper,
                                     ValidationService validationService) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketSpecification = ticketSpecification;
        this.pageableSortMapper = pageableSortMapper;
        this.validationService = validationService;
    }

    private static final Map<String, String> TICKET_ASSIGNED_SORT_FIELDS = Map.of(
            "createdat","ticketCreatedDate",
            "updatedat", "ticketUpdatedDate",
            "status", "ticketStatus",
            "title", "ticketTitle"
    );

    private static final Map<String, String> TICKET_FILED_SORT_FIELDS = Map.of(
            "createdat","ticketCreatedDate",
            "updatedat", "ticketUpdatedDate",
            "title", "ticketTitle"
    );

    private static final String DEFAULT_TICKET_SORT = "ticketCreatedDate";

    @Override
    public TicketResponseDTO fileTicket(TicketCreateRequestDTO ticket,
                                        Long employeeId) {
        Employee employee = validationService.validateEmployee(employeeId);

        Ticket newTicket = ticketMapper.toEntity(ticket);
        newTicket.setTicketCreatedBy(employee);

        boolean hasTitle = ticket.getTicketTitle() != null && !ticket.getTicketTitle().isBlank();
        boolean hasBody = ticket.getTicketBody() != null && !ticket.getTicketBody().isBlank();

        if (hasTitle && hasBody) {
            newTicket.setTicketStatus(TicketStatus.FILED);
        } else {
            newTicket.setTicketStatus(TicketStatus.DRAFT);
        }

        return ticketMapper.toTicketDTO(ticketRepository.save(newTicket));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketResponseDTO> viewAssignedTickets(Long employeeId) {
        validationService.validateEmployee(employeeId);

        return ticketRepository.findByTicketAssigneeId(employeeId)
                .stream()
                .map(ticketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> viewAssignedTicketsPaginated(Long employeeId,
                                                                Pageable pageable) {
        validationService.validateEmployee(employeeId);

        Page<Ticket> tickets =
                ticketRepository.findByTicketAssigneeId(
                        employeeId,
                        pageableSortMapper.map(
                                pageable,
                                DEFAULT_TICKET_SORT,
                                TICKET_ASSIGNED_SORT_FIELDS
                        )
                );

        if (tickets.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "No tickets found"
            );
        }

        return tickets.map(ticketMapper::toTicketDTO);
    }

    @Override
    public TicketResponseDTO updateOwnTicket(Long ticketId,
                                             TicketUpdateRequestDTO updatedTicket,
                                             Long employeeId) {
        Ticket ticket = validationService.getTicketOrThrow(ticketId);
        Employee employee = validationService.validateEmployee(employeeId);

        if (!ticket.getTicketCreatedBy().getId().equals(employeeId)) {
            throw new TicketAccessException("You can only update your own tickets");
        }

        ticketMapper.updateEntity(updatedTicket, ticket);
        ticket.setTicketUpdatedBy(employee);

        validationService.handleTicketClosure(ticket);

        return ticketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO updateOwnTicketStatus(Long ticketId,
                                                   TicketStatus newStatus,
                                                   Long employeeId) {
        Ticket ticket = validationService.getTicketOrThrow(ticketId);
        Employee employee = validationService.validateEmployee(employeeId);

        Employee assignee = ticket.getTicketAssignee();
        if (assignee == null || !assignee.getId().equals(employeeId)) {
            throw new TicketAccessException("You can only update your assigned tickets");
        }

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee);

        validationService.handleTicketClosure(ticket);

        return ticketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO addRemarkToAssignedTicket(Long ticketId,
                                                       Long employeeId,
                                                       String remark,
                                                       TicketStatus newStatus) {
        Ticket ticket = validationService.getTicketOrThrow(ticketId);
        Employee employee = validationService.validateEmployee(employeeId);

        Employee assignee = ticket.getTicketAssignee();
        if (assignee == null || !assignee.getId().equals(employeeId)) {
            throw new TicketAccessException("You can only add remarks to tickets assigned to you");
        }

        ticket.getTicketRemarks().add(new TicketRemark(remark, employee, ticket));
        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }

        ticket.setTicketUpdatedBy(employee);

        validationService.handleTicketClosure(ticket);

        return ticketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketResponseDTO> getAllFiledTickets(Long employeeId) {
        validationService.validateEmployee(employeeId);

        List<Ticket> filedTickets = ticketRepository.findByTicketStatus(TicketStatus.FILED);

        return filedTickets.stream()
                .map(ticketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> getAllFiledTicketsPaginated(Long employeeId,
                                                               Pageable pageable) {
        validationService.validateEmployee(employeeId);

        Page<Ticket> tickets =
                ticketRepository.findByTicketStatus(
                        TicketStatus.FILED,
                        pageableSortMapper.map(
                                pageable,
                                DEFAULT_TICKET_SORT,
                                TICKET_FILED_SORT_FIELDS
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

    @Transactional(readOnly = true)
    @Override
    public TicketResponseDTO getFiledTicket(Long employeeId,
                                            Long ticketId) {
        validationService.validateEmployee(employeeId);
        Ticket ticket = validationService.getTicketOrThrow(ticketId);

        if (ticket.getTicketStatus() != TicketStatus.FILED) {
            throw new IllegalArgumentException("Ticket is not in FILED status");
        }

        return ticketMapper.toTicketDTO(ticket);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> searchAssignedTickets(Long employeeId,
                                                         TicketSearchAssignedRequestDTO ticketSearchAssignedRequestDTO,
                                                         Pageable pageable) {
        validationService.validateEmployee(employeeId);

        Specification<Ticket> spec = Specification.allOf(
                ticketSpecification.assignedToEmployee(employeeId),
                ticketSpecification.hasTitle(ticketSearchAssignedRequestDTO.getTitle()),
                ticketSpecification.hasCreatedDate(ticketSearchAssignedRequestDTO.getCreatedat()),
                ticketSpecification.hasUpdatedDate(ticketSearchAssignedRequestDTO.getUpdatedat()),
                ticketSpecification.hasStatus(ticketSearchAssignedRequestDTO.getStatus())
        );

        Page<Ticket> tickets = ticketRepository.findAll(
                spec,
                pageableSortMapper.map(
                        pageable,
                        DEFAULT_TICKET_SORT,
                        TICKET_ASSIGNED_SORT_FIELDS
                )
        );

        if (tickets.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "No assigned tickets match the search criteria"
            );
        }

        return tickets.map(ticketMapper::toTicketDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TicketResponseDTO> searchFiledTickets(Long employeeId,
                                                      TicketSearchFiledRequestDTO searchFiledRequestDTO,
                                                      Pageable pageable) {
        validationService.validateEmployee(employeeId);

        Specification<Ticket> spec = Specification.allOf(
                ticketSpecification.hasStatus(TicketStatus.FILED),
                ticketSpecification.hasTitle(searchFiledRequestDTO.getTitle()),
                ticketSpecification.hasCreatedDate(searchFiledRequestDTO.getCreatedat()),
                ticketSpecification.hasUpdatedDate(searchFiledRequestDTO.getUpdatedat())
        );

        Page<Ticket> tickets = ticketRepository.findAll(
                spec,
                pageableSortMapper.map(
                        pageable,
                        DEFAULT_TICKET_SORT,
                        TICKET_FILED_SORT_FIELDS
                )
        );

        if (tickets.isEmpty()) {
            throw new EmptyPageException(
                    pageable.getPageNumber(),
                    "No filed tickets match the search criteria"
            );
        }

        return tickets.map(ticketMapper::toTicketDTO);
    }
}
