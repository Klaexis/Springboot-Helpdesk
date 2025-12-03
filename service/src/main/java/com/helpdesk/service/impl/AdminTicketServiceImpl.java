package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.AdminTicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminTicketServiceImpl implements AdminTicketService {
    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final EmployeeRepository employeeRepository;

    public AdminTicketServiceImpl(TicketRepository ticketRepository,
                                  EmployeeRepository employeeRepository) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
    }

    public Ticket assignTicket(Long ticketNumber,
                               Long employeeId,
                               String assignedBy) {
        Ticket ticket = ticketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        ticket.setTicketAssignee(employee.getEmployeeName());
        ticket.setTicketUpdatedBy(assignedBy);
        ticket.setTicketStatus(TicketStatus.IN_PROGRESS);

        return ticketRepository.save(ticket);
    }

    public Ticket getTicket(Long ticketNumber) {
        return ticketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket updateTicket(Long id,
                               Ticket updatedTicket,
                               String updatedBy) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setTicketTitle(updatedTicket.getTicketTitle());
        ticket.setTicketBody(updatedTicket.getTicketBody());
        ticket.setTicketAssignee(updatedTicket.getTicketAssignee());
        ticket.setTicketStatus(updatedTicket.getTicketStatus());
        ticket.setTicketRemarks(updatedTicket.getTicketRemarks());
        ticket.setTicketUpdatedBy(updatedBy);

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketNumber,
                                     TicketStatus newStatus,
                                     String updatedBy) {
        Ticket ticket = ticketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(updatedBy);

        return ticketRepository.save(ticket);
    }
}
