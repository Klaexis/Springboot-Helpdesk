package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import java.util.List;

public interface AdminTicketService {
    Ticket assignTicket(Long ticketId, Long adminId, Long employeeId);

    Ticket getTicket(Long adminId, Long ticketId);

    List<Ticket> getAllTickets(Long adminId);

    Ticket updateTicket(Long ticketId, Ticket updatedTicket, Long adminId);

    Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus, Long adminId);
}
