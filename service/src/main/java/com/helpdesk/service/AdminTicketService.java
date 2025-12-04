package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import java.util.List;

public interface AdminTicketService {
    Ticket assignTicket(Long ticketId, Long adminId, Long employeeId);

    Ticket getTicket(Long ticketId);

    List<Ticket> getAllTickets();

    Ticket updateTicket(Long ticketId, Ticket updatedTicket, Long adminId);

    Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus, Long adminId);
}
