package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.service.AdminTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tickets")
public class AdminTicketController {

    private final AdminTicketService adminTicketService;

    @Autowired
    public AdminTicketController(AdminTicketService adminTicketService) {
        this.adminTicketService = adminTicketService;
    }

    // PUT /admin/tickets/{ticketId}/assign
    @PutMapping("/{ticketId}/assign")
    public ResponseEntity<Ticket> assignTicket(
            @PathVariable Long ticketId,
            @RequestParam Long adminId,
            @RequestParam Long employeeId) {

        Ticket updatedTicket = adminTicketService.assignTicket(ticketId, adminId, employeeId);
        return ResponseEntity.ok(updatedTicket);
    }

    // GET /admin/tickets/{ticketId}
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long ticketId) {
        Ticket ticket = adminTicketService.getTicket(ticketId);
        return ResponseEntity.ok(ticket);
    }

    // GET /admin/tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = adminTicketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    // PUT /admin/tickets/{ticketId}?adminId=ADMIN_ID
    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long ticketId,
            @RequestBody Ticket updatedTicket,
            @RequestParam Long adminId) {

        Ticket ticket = adminTicketService.updateTicket(ticketId, updatedTicket, adminId);
        return ResponseEntity.ok(ticket);
    }

    // PUT /admin/tickets/{ticketId}/status?newStatus=STATUS&adminId=ADMIN_ID
    @PutMapping("/{ticketId}/status")
    public ResponseEntity<Ticket> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam TicketStatus newStatus,
            @RequestParam Long adminId) {

        Ticket updatedTicket = adminTicketService.updateTicketStatus(ticketId, newStatus, adminId);
        return ResponseEntity.ok(updatedTicket);
    }
}
