package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import java.util.List;

public interface EmployeeTicketService {
    Ticket fileTicket(Ticket ticket, String createdBy);

    List<Ticket> viewAssignedTickets(Long employeeId);

    Ticket updateOwnTicket(Long ticketNumber, Ticket updatedTicket, String employee);

    Ticket updateOwnTicketStatus(Long ticketNumber, TicketStatus newStatus, String employee);

    List<Ticket> getFiledTickets();

    Ticket getFiledTicket(Long ticketNumber);
}
