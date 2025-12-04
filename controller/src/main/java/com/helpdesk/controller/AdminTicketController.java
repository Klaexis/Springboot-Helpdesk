package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.service.AdminTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/{adminId}/tickets")
public class AdminTicketController {
    @Autowired
    private final AdminTicketService adminTicketService;

    public AdminTicketController(AdminTicketService adminTicketService) {
        this.adminTicketService = adminTicketService;
    }

    // PUT /admin/{adminId}/tickets/assign/{ticketId}/assignTo/{employeeId}
    @PutMapping("assign/{ticketId}/assignTo/{employeeId}")
    public ResponseEntity<Ticket> assignTicket(
            @PathVariable Long ticketId,
            @PathVariable Long adminId,
            @PathVariable Long employeeId) {

        Ticket updatedTicket = adminTicketService.assignTicket(ticketId, adminId, employeeId);
        return ResponseEntity.ok(updatedTicket);
    }

    // GET /admin/{adminId}/tickets/find/{ticketId}
    @GetMapping("/find/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long adminId, @PathVariable Long ticketId) {
        Ticket ticket = adminTicketService.getTicket(adminId, ticketId);
        return ResponseEntity.ok(ticket);
    }

    // GET /admin/{adminId}/tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(@PathVariable Long adminId) {
        List<Ticket> tickets = adminTicketService.getAllTickets(adminId);
        return ResponseEntity.ok(tickets);
    }

    // PUT /admin/{adminId}/tickets/update/{ticketId}
    @PutMapping("update/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long ticketId,
            @RequestBody Ticket updatedTicket,
            @PathVariable Long adminId) {

        Ticket ticket = adminTicketService.updateTicket(ticketId, updatedTicket, adminId);
        return ResponseEntity.ok(ticket);
    }

    // PUT /admin/{adminId}/tickets/update/{ticketId}/status?newStatus=STATUS
    @PutMapping("/update/{ticketId}/status")
    public ResponseEntity<Ticket> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam TicketStatus newStatus,
            @PathVariable Long adminId) {

        Ticket updatedTicket = adminTicketService.updateTicketStatus(ticketId, newStatus, adminId);
        return ResponseEntity.ok(updatedTicket);
    }
}
