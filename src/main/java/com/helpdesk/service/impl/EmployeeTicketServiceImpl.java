package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.EmployeeTicketService;
import com.helpdesk.service.util.EmployeeValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeTicketServiceImpl implements EmployeeTicketService {
    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeeValidationHelper employeeValidationHelper;

    public EmployeeTicketServiceImpl(TicketRepository ticketRepository,
                                     EmployeeRepository employeeRepository,
                                     EmployeeValidationHelper employeeValidationHelper) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    public Ticket fileTicket(Ticket ticket,
                             Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        ticket.setTicketStatus(TicketStatus.FILED);
        ticket.setTicketCreatedBy(employee);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> viewAssignedTickets(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        return ticketRepository.findByTicketAssigneeEmployeeId(employeeId);
    }

    public Ticket updateOwnTicket(Long ticketId,
                                  TicketUpdateRequestDTO updatedTicket,
                                  Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        if (!ticket.getTicketCreatedBy().getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("You can only update your own tickets");
        }

        updatedTicket.getTicketTitle().ifPresent(ticket::setTicketTitle);
        updatedTicket.getTicketBody().ifPresent(ticket::setTicketBody);
        updatedTicket.getTicketStatus().ifPresent(ticket::setTicketStatus);

        updatedTicket.getRemarkToAdd().ifPresent(text -> {
            TicketRemark remark = new TicketRemark(text, employee, ticket);
            ticket.getTicketRemarks().add(remark);
        });

        ticket.setTicketUpdatedBy(employee);

        return ticketRepository.save(ticket);
    }

    public Ticket updateOwnTicketStatus(Long ticketId,
                                        TicketStatus newStatus,
                                        Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        if (!ticket.getTicketAssignee().getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("You can only update your own tickets");
        }

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee);

        return ticketRepository.save(ticket);
    }

    public Ticket addRemarkToAssignedTicket(Long ticketId,
                                            Long employeeId,
                                            String remark,
                                            TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        if (!ticket.getTicketAssignee().getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("You can only add remarks to tickets assigned to you");
        }

        TicketRemark ticketRemark = new TicketRemark();
        ticketRemark.setMessage(remark);
        ticketRemark.setCreatedBy(employee);
        ticketRemark.setTicket(ticket);

        ticket.getTicketRemarks().add(ticketRemark);

        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }

        ticket.setTicketUpdatedBy(employee);

        return ticketRepository.save(ticket);
    }


    public List<Ticket> getAllFiledTickets(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        return ticketRepository.findByTicketStatus(TicketStatus.FILED);
    }

    public Ticket getFiledTicket(Long employeeId,
                                 Long ticketId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getTicketStatus() != TicketStatus.FILED) {
            throw new RuntimeException("Ticket is not in FILED status");
        }

        return ticket;
    }
}
