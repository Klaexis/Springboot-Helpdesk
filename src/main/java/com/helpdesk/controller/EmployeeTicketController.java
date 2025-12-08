package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketAddRemarkRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
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
    public ResponseEntity<TicketResponseDTO> fileTicket(@RequestBody Ticket ticket,
                                                        @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeTicketService.fileTicket(
                ticket,
                employeeId
        ));
    }

    // GET /employee/{employeeId}/tickets/get/assignedTickets
    @GetMapping("/get/assignedTickets")
    public ResponseEntity<List<TicketResponseDTO>> viewAssignedTickets(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeTicketService.viewAssignedTickets(
                employeeId
        ));
    }

    // PATCH /employee/{employeeId}/tickets/update/{ticketId}
    @PatchMapping("/update/{ticketId}")
    public ResponseEntity<TicketResponseDTO> updateOwnTicket(@PathVariable Long employeeId,
                                                             @RequestBody TicketUpdateRequestDTO ticket,
                                                             @PathVariable Long ticketId) {
        return ResponseEntity.ok(employeeTicketService.updateOwnTicket(
                ticketId,
                ticket,
                employeeId
        ));
    }

    // PATCH /employee/{employeeId}/tickets/updateStatus/{ticketId}
    @PatchMapping("/updateStatus/{ticketId}")
    public ResponseEntity<TicketResponseDTO> updateOwnTicketStatus(@PathVariable Long employeeId,
                                                        @RequestBody TicketStatus status,
                                                        @PathVariable Long ticketId) {
        return ResponseEntity.ok(employeeTicketService.updateOwnTicketStatus(
                ticketId,
                status,
                employeeId
        ));
    }

    // PATCH /employee/{employeeId}/tickets/addRemarks/{ticketId}
    @PatchMapping("/addRemarks/{ticketId}")
    public ResponseEntity<TicketResponseDTO> addTicketRemark(@PathVariable Long employeeId,
                                                  @PathVariable Long ticketId,
                                                  @RequestBody TicketAddRemarkRequestDTO request) {
        return ResponseEntity.ok(employeeTicketService.addRemarkToAssignedTicket(
                ticketId, employeeId, request.getRemark(), request.getNewStatus()));
    }

    // GET /employee/{employeeId}/tickets/get/filedTickets
    @GetMapping("/get/filedTickets")
    public ResponseEntity<List<TicketResponseDTO>> getAllFiledTickets(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeTicketService.getAllFiledTickets(
                employeeId
        ));
    }

    // GET /employee/{employeeId}/tickets/get/filedTicket/{ticketId}
    @GetMapping("/get/filedTicket/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getFiledTicket(@PathVariable Long employeeId, @PathVariable Long ticketId) {
        return ResponseEntity.ok(employeeTicketService.getFiledTicket(
                employeeId,
                ticketId
        ));
    }
}
