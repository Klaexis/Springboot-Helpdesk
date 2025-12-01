package com.helpdesk.repository;

import com.helpdesk.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketRepository {

    private final List<Ticket> tickets = new ArrayList<>();
    private Long idCounter = 1L;

    public Ticket save(Ticket ticket) {
        ticket.setId(idCounter++);
        tickets.add(ticket);
        return ticket;
    }

    public List<Ticket> findAll() {
        return tickets;
    }
}