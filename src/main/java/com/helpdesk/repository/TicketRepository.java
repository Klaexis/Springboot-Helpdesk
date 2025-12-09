package com.helpdesk.repository;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTicketAssigneeEmployeeId(Long assigneeId);
    List<Ticket> findByTicketStatus(TicketStatus status);

    // Pagination
    Page<Ticket> findAll(Pageable pageable);

    // Paginated by assignee
    Page<Ticket> findByTicketAssigneeEmployeeId(Long assigneeId, Pageable pageable);

    // Paginated by status
    Page<Ticket> findByTicketStatus(TicketStatus status, Pageable pageable);
}