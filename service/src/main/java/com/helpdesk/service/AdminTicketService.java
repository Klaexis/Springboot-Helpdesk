package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import java.util.List;

public interface AdminTicketService {
    Ticket assignTicket(Long ticketNumber, Long employeeId, String assignedBy);

    Ticket getTicket(Long ticketNumber);

    List<Ticket> getAllTickets();

    Ticket updateTicket(Long id, Ticket updatedTicket, String updatedBy);

    Ticket updateTicketStatus(Long ticketNumber, TicketStatus newStatus, String updatedBy);
}
