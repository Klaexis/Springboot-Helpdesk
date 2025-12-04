package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.EmployeeTicketService;
import com.helpdesk.service.EmployeeValidationService;

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
    private final EmployeeValidationService employeeValidationService;

    public EmployeeTicketServiceImpl(TicketRepository ticketRepository,
                                     EmployeeRepository employeeRepository,
                                     EmployeeValidationService employeeValidationService) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationService = employeeValidationService;
    }

    public Ticket fileTicket(Ticket ticket, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationService.validateActive(employee);

        ticket.setTicketStatus(TicketStatus.FILED);
        ticket.setTicketCreatedBy(employee.getEmployeeName());
        return ticketRepository.save(ticket);
    }

    public List<Ticket> viewAssignedTickets(Long employeeId) {
        return ticketRepository.findByTicketAssigneeId(employeeId);
    }

    public Ticket updateOwnTicket(Long ticketId, Ticket updatedTicket, Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationService.validateActive(employee);

        if (!ticket.getTicketCreatedBy().equals(employee.getEmployeeName())) {
            throw new RuntimeException("You can only update your own tickets");
        }

        ticket.setTicketTitle(updatedTicket.getTicketTitle());
        ticket.setTicketBody(updatedTicket.getTicketBody());
        ticket.setTicketRemarks(updatedTicket.getTicketRemarks());
        ticket.setTicketStatus(updatedTicket.getTicketStatus());
        ticket.setTicketUpdatedBy(employee.getEmployeeName());

        return ticketRepository.save(ticket);
    }

    public Ticket updateOwnTicketStatus(Long ticketId, TicketStatus newStatus, Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationService.validateActive(employee);

        if (!ticket.getTicketAssignee().equals(employee.getEmployeeName())) {
            throw new RuntimeException("You can only update your own tickets");
        }

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee.getEmployeeName());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getFiledTickets() {
        return ticketRepository.findByTicketStatus(TicketStatus.FILED);
    }

    public Ticket getFiledTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getTicketStatus() != TicketStatus.FILED) {
            throw new RuntimeException("Ticket is not in FILED status");
        }

        return ticket;
    }
}
