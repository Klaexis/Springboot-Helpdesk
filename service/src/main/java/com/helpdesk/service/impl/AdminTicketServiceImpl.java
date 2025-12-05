package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.AdminTicketService;

import com.helpdesk.service.EmployeeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminTicketServiceImpl implements AdminTicketService {
    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeeValidationService employeeValidationService;

    public AdminTicketServiceImpl(TicketRepository ticketRepository,
                                  EmployeeRepository employeeRepository,
                                  EmployeeValidationService employeeValidationService) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationService = employeeValidationService;
    }

    public Ticket assignTicket(Long ticketId,
                               Long adminId,
                               Long employeeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        employeeValidationService.validateAdmin(admin);
        employeeValidationService.validateActive(admin);

        Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeValidationService.validateActive(employee);

        ticket.setTicketAssignee(employee);
        ticket.setTicketUpdatedBy(admin);
        ticket.setTicketStatus(TicketStatus.IN_PROGRESS);

        return ticketRepository.save(ticket);
    }

    public Ticket getTicket(Long adminId, Long ticketId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationService.validateAdmin(admin);
        employeeValidationService.validateActive(admin);

        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<Ticket> getAllTickets(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationService.validateAdmin(admin);
        employeeValidationService.validateActive(admin);

        return ticketRepository.findAll();
    }

    public Ticket updateTicket(Long ticketId,
                               Ticket updatedTicket,
                               Long adminId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        employeeValidationService.validateAdmin(admin);
        employeeValidationService.validateActive(admin);

        ticket.setTicketTitle(updatedTicket.getTicketTitle());
        ticket.setTicketBody(updatedTicket.getTicketBody());
        ticket.setTicketAssignee(updatedTicket.getTicketAssignee());
        ticket.setTicketStatus(updatedTicket.getTicketStatus());
        ticket.setTicketRemarks(updatedTicket.getTicketRemarks());
        ticket.setTicketUpdatedBy(admin);

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId,
                                     TicketStatus newStatus,
                                     Long adminId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        employeeValidationService.validateAdmin(admin);
        employeeValidationService.validateActive(admin);

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(admin);

        return ticketRepository.save(ticket);
    }
}
