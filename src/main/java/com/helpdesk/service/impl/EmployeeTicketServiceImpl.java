package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.EmployeeTicketService;
import com.helpdesk.service.mapper.TicketMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeValidationHelper.validateActive(employee);

        return employee;
    }

    private Ticket getTicketOrThrow(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public TicketResponseDTO fileTicket(Ticket ticket,
                                        Long employeeId) {
        Employee employee = validateEmployee(employeeId);

        ticket.setTicketStatus(TicketStatus.FILED);
        ticket.setTicketCreatedBy(employee);
        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    public List<TicketResponseDTO> viewAssignedTickets(Long employeeId) {
        validateEmployee(employeeId);

        return ticketRepository.findByTicketAssigneeEmployeeId(employeeId)
                .stream()
                .map(TicketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    public TicketResponseDTO updateOwnTicket(Long ticketId,
                                             TicketUpdateRequestDTO updatedTicket,
                                             Long employeeId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee employee = validateEmployee(employeeId);

        if (!ticket.getTicketCreatedBy().getEmployeeId().equals(employeeId))
            throw new RuntimeException("You can only update your own tickets");

        TicketMapper.updateEntityFromDTO(updatedTicket, ticket, employee, null);
        ticket.setTicketUpdatedBy(employee);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    public TicketResponseDTO updateOwnTicketStatus(Long ticketId,
                                                   TicketStatus newStatus,
                                                   Long employeeId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee employee = validateEmployee(employeeId);

        if (!ticket.getTicketAssignee().getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("You can only update your own tickets");
        }

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    public TicketResponseDTO addRemarkToAssignedTicket(Long ticketId,
                                                       Long employeeId,
                                                       String remark,
                                                       TicketStatus newStatus) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee employee = validateEmployee(employeeId);

        if (!ticket.getTicketAssignee().getEmployeeId().equals(employeeId))
            throw new RuntimeException("You can only add remarks to tickets assigned to you");

        ticket.getTicketRemarks().add(new TicketRemark(remark, employee, ticket));
        if (newStatus != null) ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }


    public List<TicketResponseDTO> getAllFiledTickets(Long employeeId) {
        validateEmployee(employeeId);

        List<Ticket> filedTickets = ticketRepository.findByTicketStatus(TicketStatus.FILED);

        return filedTickets.stream()
                .map(TicketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    public TicketResponseDTO getFiledTicket(Long employeeId,
                                 Long ticketId) {
        validateEmployee(employeeId);
        Ticket ticket = getTicketOrThrow(ticketId);

        if (ticket.getTicketStatus() != TicketStatus.FILED) {
            throw new RuntimeException("Ticket is not in FILED status");
        }

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }
}
