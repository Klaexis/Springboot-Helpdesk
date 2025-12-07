package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.AddTicketRemarkRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
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

    // GET /admin/{adminId}/tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(@PathVariable Long adminId) {
        List<Ticket> tickets = adminTicketService.getAllTickets(
                adminId
        );
        return ResponseEntity.ok(tickets);
    }

    // GET /admin/{adminId}/tickets/find/{ticketId}
    @GetMapping("/find/{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long adminId,
                                            @PathVariable Long ticketId) {
        Ticket ticket = adminTicketService.getTicket(
                adminId,
                ticketId
        );
        return ResponseEntity.ok(ticket);
    }

    // PATCH /admin/{adminId}/tickets/assign/{ticketId}/assignTo/{employeeId}
    @PatchMapping("assign/{ticketId}/assignTo/{employeeId}")
    public ResponseEntity<Ticket> assignTicket(@PathVariable Long adminId,
                                               @PathVariable Long ticketId,
                                               @PathVariable Long employeeId) {
        Ticket assignedTicket = adminTicketService.assignTicket(
                ticketId,
                adminId,
                employeeId
        );
        return ResponseEntity.ok(assignedTicket);
    }

    // PATCH /admin/{adminId}/tickets/update/{ticketId}
    @PatchMapping("update/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long adminId,
                                               @RequestBody TicketUpdateRequestDTO updatedTicket,
                                               @PathVariable Long ticketId) {
        Ticket newTicket = adminTicketService.updateTicket(
                ticketId,
                updatedTicket,
                adminId
        );
        return ResponseEntity.ok(newTicket);
    }

    // PATCH /admin/{adminId}/tickets/update/{ticketId}/status
    @PatchMapping("/update/{ticketId}/status")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long adminId,
                                                     @RequestBody TicketStatus newStatus,
                                                     @PathVariable Long ticketId) {
        Ticket updatedTicketStatus = adminTicketService.updateTicketStatus(
                ticketId,
                newStatus,
                adminId
        );
        return ResponseEntity.ok(updatedTicketStatus);
    }

    // PATCH /admin/{adminId}/tickets/addRemarks/{ticketId}
    @PatchMapping("/addRemarks/{ticketId}")
    public ResponseEntity<Ticket> addTicketRemark(@PathVariable Long adminId,
                                                  @PathVariable Long ticketId,
                                                  @RequestBody AddTicketRemarkRequestDTO request) {
        Ticket ticketRemarks = adminTicketService.addTicketRemark(
                ticketId,
                adminId,
                request.getRemark(),
                request.getNewStatus()
        );
        return ResponseEntity.ok(ticketRemarks);
    }
}
