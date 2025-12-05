package com.helpdesk.service.util;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.repository.TicketRepository;
import org.springframework.stereotype.Component;

@Component
public class TicketServiceHelper {

    private final TicketRepository ticketRepository;

    public TicketServiceHelper(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket addRemarkAndStatus(Ticket ticket, String remark, TicketStatus newStatus, Employee updater) {
        // Append remark
        String existingRemarks = ticket.getTicketRemarks();
        if (existingRemarks == null || existingRemarks.isEmpty()) {
            ticket.setTicketRemarks(remark);
        } else {
            ticket.setTicketRemarks(existingRemarks + "\n" + remark);
        }

        // Update status if provided
        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }

        // Set who updated the ticket
        ticket.setTicketUpdatedBy(updater);

        return ticketRepository.save(ticket);
    }
}
