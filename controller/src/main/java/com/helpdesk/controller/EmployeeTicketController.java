package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.EmployeeTicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/{employeeId}/tickets")
public class EmployeeTicketController {
    @Autowired
    private final EmployeeTicketService employeeTicketService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeTicketController(EmployeeTicketService employeeTicketService) {
        this.employeeTicketService = employeeTicketService;
    }

    // POST /employee/{employeeId}/tickets/file
    @PostMapping("/file")
    public ResponseEntity<Ticket> fileTicket(@RequestBody Ticket ticket,
                                             @PathVariable Long employeeId) {
        Ticket newTicket = employeeTicketService.fileTicket(ticket, employeeId);
        return ResponseEntity.ok(newTicket);
    }

    // GET /employee/{employeeId}/tickets/get/assignedTickets
    @GetMapping("/get/assignedTickets")
    public ResponseEntity<List<Ticket>> viewAssignedTickets(@PathVariable Long employeeId) {
        List<Ticket> tickets = employeeTicketService.viewAssignedTickets(employeeId);
        return ResponseEntity.ok(tickets);
    }

    // PUT /employee/{employeeId}/tickets/update/{ticketId}
    @PutMapping("/update/{ticketId}")
    public ResponseEntity<Ticket> updateOwnTicket(@PathVariable Long ticketId,
                                                  @RequestBody Ticket ticket,
                                                  @PathVariable Long employeeId) {
        Ticket updatedTicket = employeeTicketService.updateOwnTicket(ticketId, ticket, employeeId);

        return ResponseEntity.ok(updatedTicket);
    }

    // PUT /employee/{employeeId}/tickets/updateStatus/{ticketId}
    @PutMapping("/updateStatus/{ticketId}")
    public ResponseEntity<Ticket> updateOwnTicketStatus(@PathVariable Long ticketId,
                                                        @RequestBody TicketStatus status,
                                                        @PathVariable Long employeeId) {
        Ticket updatedTicketStatus = employeeTicketService.updateOwnTicketStatus(ticketId, status, employeeId);

        return ResponseEntity.ok(updatedTicketStatus);
    }

    // POST /employee/{employeeId}/tickets/addRemarks/{ticketId}?newStatus=STATUS
    @PostMapping("/addRemarks/{ticketId}")
    public ResponseEntity<Ticket> addTicketRemark(@PathVariable Long ticketId,
                                                  @PathVariable Long employeeId,
                                                  @RequestBody String remark,
                                                  @RequestParam(required = false) TicketStatus newStatus) {
        Ticket ticketRemarks = employeeTicketService.addRemarkToAssignedTicket(ticketId, employeeId, remark, newStatus);
        return ResponseEntity.ok(ticketRemarks);
    }

    // GET /employee/{employeeId}/tickets/get/filedTickets
    @GetMapping("/get/filedTickets")
    public ResponseEntity<List<Ticket>> getAllFiledTickets(@PathVariable Long employeeId) {
        List<Ticket> filedTickets = employeeTicketService.getAllFiledTickets(employeeId);

        return ResponseEntity.ok(filedTickets);
    }

    // GET /employee/{employeeId}/tickets/get/filedTicket/{ticketId}
    @GetMapping("/get/filedTicket/{ticketId}")
    public ResponseEntity<Ticket> getFiledTicket(@PathVariable Long employeeId, @PathVariable Long ticketId) {
        Ticket filedTicket = employeeTicketService.getFiledTicket(employeeId, ticketId);

        return ResponseEntity.ok(filedTicket);
    }
}
