package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import java.util.List;

public interface EmployeeTicketService {
    Ticket fileTicket(Ticket ticket, Long employeeId);

    List<Ticket> viewAssignedTickets(Long employeeId);

    Ticket updateOwnTicket(Long ticketId, Ticket updatedTicket, Long employeeId);

    Ticket updateOwnTicketStatus(Long ticketId, TicketStatus newStatus, Long employeeId);

    List<Ticket> getAllFiledTickets(Long employeeId);

    Ticket getFiledTicket(Long employeeId, Long ticketId);

    Ticket addRemarkToAssignedTicket(Long ticketId, Long employeeId, String remark, TicketStatus newStatus);
}
