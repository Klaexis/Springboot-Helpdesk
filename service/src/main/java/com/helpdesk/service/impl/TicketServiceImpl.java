package com.helpdesk.service.impl;

import com.helpdesk.model.Ticket;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository repository;

    public TicketServiceImpl(TicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        return repository.save(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return repository.findAll();
    }
}
