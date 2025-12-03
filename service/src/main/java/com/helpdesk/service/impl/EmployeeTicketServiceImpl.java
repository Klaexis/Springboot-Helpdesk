package com.helpdesk.service.impl;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.repository.TicketRepository;

import com.helpdesk.service.EmployeeTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeTicketServiceImpl implements EmployeeTicketService {
    @Autowired
    private final TicketRepository ticketRepository;

    public EmployeeTicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket fileTicket(Ticket ticket, String createdBy) {
        ticket.setTicketStatus(TicketStatus.FILED);
        ticket.setTicketCreatedBy(createdBy);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> viewAssignedTickets(Long employeeId) {
        return ticketRepository.findByTicketAssigneeId(employeeId);
    }

    public Ticket updateOwnTicket(Long ticketNumber, Ticket updatedTicket, String employee) {
        Ticket ticket = ticketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

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

    public Ticket updateOwnTicketStatus(Long ticketNumber, TicketStatus newStatus, String employee) {
        Ticket ticket = ticketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getTicketAssignee().equals(employee)) {
            throw new RuntimeException("You can only update your own tickets");
        }

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(employee);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getFiledTickets() {
        return ticketRepository.findByTicketStatus(TicketStatus.FILED);
    }

    public Ticket getFiledTicket(Long ticketNumber) {
        Ticket ticket = ticketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getTicketStatus() != TicketStatus.FILED) {
            throw new RuntimeException("Ticket is not in FILED status");
        }

        return ticket;
    }
}
