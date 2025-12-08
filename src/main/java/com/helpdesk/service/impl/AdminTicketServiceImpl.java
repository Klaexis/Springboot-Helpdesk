package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.AdminTicketService;
import com.helpdesk.service.util.EmployeeValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    private Ticket getTicketOrThrow(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket assignTicket(Long ticketId,
                               Long adminId,
                               Long employeeId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        ticket.setTicketAssignee(employee);
        ticket.setTicketUpdatedBy(admin);
        ticket.setTicketStatus(TicketStatus.IN_PROGRESS);

        return ticketRepository.save(ticket);
    }

    public Ticket getTicket(Long adminId,
                            Long ticketId) {
        validateAdmin(adminId);

        return getTicketOrThrow(ticketId);
    }

    public List<Ticket> getAllTickets(Long adminId) {
        validateAdmin(adminId);

        return ticketRepository.findAll();
    }

    public Ticket updateTicket(Long ticketId,
                               TicketUpdateRequestDTO updatedTicket,
                               Long adminId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        updatedTicket.getTicketTitle().ifPresent(ticket::setTicketTitle);
        updatedTicket.getTicketBody().ifPresent(ticket::setTicketBody);

        updatedTicket.getTicketAssigneeId().ifPresent(assigneeId -> {
            Employee assignee = employeeRepository.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            ticket.setTicketAssignee(assignee);
        });

        updatedTicket.getTicketStatus().ifPresent(ticket::setTicketStatus);

        updatedTicket.getRemarkToAdd().ifPresent(text -> {
            TicketRemark remark = new TicketRemark(text, admin, ticket );
            ticket.getTicketRemarks().add(remark);
        });

        ticket.setTicketUpdatedBy(admin);

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(Long ticketId,
                                     TicketStatus newStatus,
                                     Long adminId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(admin);

        return ticketRepository.save(ticket);
    }

    public Ticket addTicketRemark(Long ticketId,
                                  Long adminId,
                                  String remark,
                                  TicketStatus newStatus) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        TicketRemark ticketRemark = new TicketRemark();
        ticketRemark.setMessage(remark);
        ticketRemark.setCreatedBy(admin);
        ticketRemark.setTicket(ticket);

        ticket.getTicketRemarks().add(ticketRemark);

        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }

        ticket.setTicketUpdatedBy(admin);

        return ticketRepository.save(ticket);
    }
}
