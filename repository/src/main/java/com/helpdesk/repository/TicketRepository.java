package com.helpdesk.repository;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTicketAssigneeId(Long assigneeId);
    List<Ticket> findByTicketStatus(TicketStatus status);
}