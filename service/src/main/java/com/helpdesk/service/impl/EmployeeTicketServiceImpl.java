package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.EmployeeTicketService;
import com.helpdesk.service.util.EmployeeValidationHelper;

import com.helpdesk.service.util.TicketServiceHelper;
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

    @Autowired
    private final TicketServiceHelper ticketServiceHelper;

    public EmployeeTicketServiceImpl(TicketRepository ticketRepository,
                                     EmployeeRepository employeeRepository,
                                     EmployeeValidationHelper employeeValidationHelper,
                                     TicketServiceHelper ticketServiceHelper) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
        this.ticketServiceHelper = ticketServiceHelper;
    }

    public Ticket fileTicket(Ticket ticket, Long employeeId) {
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

    public Ticket updateOwnTicket(Long ticketId, Ticket updatedTicket, Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        if (!ticket.getTicketCreatedBy().equals(employee)) {
            throw new RuntimeException("You can only update your own tickets");
        }

        ticket.setTicketTitle(updatedTicket.getTicketTitle());
        ticket.setTicketBody(updatedTicket.getTicketBody());
        ticket.setTicketRemarks(updatedTicket.getTicketRemarks());
        ticket.setTicketStatus(updatedTicket.getTicketStatus());
        ticket.setTicketUpdatedBy(employee);

        return ticketRepository.save(ticket);
    }

    public Ticket updateOwnTicketStatus(Long ticketId, TicketStatus newStatus, Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        if (!ticket.getTicketAssignee().equals(employee)) {
            throw new RuntimeException("You can only update your own tickets");
        }

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee);

        return ticketRepository.save(ticket);
    }

    public Ticket addRemarkToAssignedTicket(Long ticketId, Long employeeId, String remark, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        if (!ticket.getTicketAssignee().equals(employee)) {
            throw new RuntimeException("You can only add remarks to tickets assigned to you");
        }

        return ticketServiceHelper.addRemarkAndStatus(ticket, remark, newStatus, employee);
    }


    public List<Ticket> getAllFiledTickets(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationHelper.validateActive(employee);

        return ticketRepository.findByTicketStatus(TicketStatus.FILED);
    }

    public Ticket getFiledTicket(Long employeeId, Long ticketId) {
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
