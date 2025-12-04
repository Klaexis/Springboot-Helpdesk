package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.service.EmployeeTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/tickets")
public class EmployeeTicketController {
    @Autowired
    private final EmployeeTicketService employeeTicketService;

    public EmployeeTicketController(EmployeeTicketService employeeTicketService) {
        this.employeeTicketService = employeeTicketService;
    }

    // POST /employee/tickets/file/{employeeId}
    @PostMapping("/file/{employeeId}")
    public ResponseEntity<Ticket> fileTicket(@RequestBody Ticket ticket,
                                             @PathVariable Long employeeId) {
        Ticket newTicket = employeeTicketService.fileTicket(ticket, employeeId);
        return ResponseEntity.ok(newTicket);
    }

    // GET /employee/tickets/get/assignedTickets/{employeeId}
    @GetMapping("/get/assignedTickets/{employeeId}")
    public ResponseEntity<List<Ticket>> viewAssignedTickets(@PathVariable Long employeeId) {
        List<Ticket> tickets = employeeTicketService.viewAssignedTickets(employeeId);
        return ResponseEntity.ok(tickets);
    }

    // PUT /employee/tickets/{employeeId}/update/{ticketId}
    @PutMapping("/{employeeId}/update/{ticketId}")
    public ResponseEntity<Ticket> updateOwnTicket(@PathVariable Long ticketId,
                                                  @RequestBody Ticket ticket,
                                                  @PathVariable Long employeeId) {
        Ticket updatedTicket = employeeTicketService.updateOwnTicket(ticketId, ticket, employeeId);

        return ResponseEntity.ok(updatedTicket);
    }

    // PUT /employee/tickets/{employeeId}/updateStatus/{ticketId}
    @PutMapping("/{employeeId}/updateStatus/{ticketId}")
    public ResponseEntity<Ticket> updateOwnTicketStatus(@PathVariable Long ticketId,
                                                        @RequestBody TicketStatus status,
                                                        @PathVariable Long employeeId) {
        Ticket updatedTicketStatus = employeeTicketService.updateOwnTicketStatus(ticketId, status, employeeId);

        return ResponseEntity.ok(updatedTicketStatus);
    }

    // GET /employee/tickets/get/filedTickets
    @GetMapping("/get/filedTickets")
    public ResponseEntity<List<Ticket>> getAllFiledTickets() {
        List<Ticket> filedTickets = employeeTicketService.getAllFiledTickets();

        return ResponseEntity.ok(filedTickets);
    }

    // GET /employee/tickets/get/filedTicket/{ticketId}
    @GetMapping("/get/filedTicket/{ticketId}")
    public ResponseEntity<Ticket> getFiledTicket(@PathVariable Long ticketId) {
        Ticket filedTicket = employeeTicketService.getFiledTicket(ticketId);

        return ResponseEntity.ok(filedTicket);
    }
}
