package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;

import java.util.List;

public interface AdminTicketService {
    Ticket assignTicket(Long ticketId, Long adminId, Long employeeId);

    Ticket getTicket(Long adminId, Long ticketId);

    List<Ticket> getAllTickets(Long adminId);

    Ticket updateTicket(Long ticketId, TicketUpdateRequestDTO updatedTicket, Long adminId);

    Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus, Long adminId);

    Ticket addTicketRemark(Long ticketId, Long adminId, String remark, TicketStatus newStatus);
}
