package com.helpdesk.repository;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    List<Ticket> findByTicketAssigneeId(Long assigneeId);
    List<Ticket> findByTicketStatus(TicketStatus status);

    // Paginated by assignee
    Page<Ticket> findByTicketAssigneeId(Long assigneeId, Pageable page);

    // Paginated by status
    Page<Ticket> findByTicketStatus(TicketStatus status, Pageable page);
}