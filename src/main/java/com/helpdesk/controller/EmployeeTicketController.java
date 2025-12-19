package com.helpdesk.controller;

import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.*;
import com.helpdesk.model.response.PageResponseDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.service.EmployeeTicketService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeTicketController {
    private final EmployeeTicketService employeeTicketService;

    public EmployeeTicketController(EmployeeTicketService employeeTicketService) {
        this.employeeTicketService = employeeTicketService;
    }

    // POST /employee/{employeeId}/tickets/file
    @PostMapping("/{employeeId}/tickets/file")
    public ResponseEntity<TicketResponseDTO> fileTicket(@RequestBody TicketCreateRequestDTO ticket,
                                                        @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeTicketService.fileTicket(
                ticket,
                employeeId
        ));
    }

    // GET /employee/{employeeId}/tickets/get/assignedTickets
    @GetMapping("/{employeeId}/tickets/get/assignedTickets")
    public ResponseEntity<List<TicketResponseDTO>> viewAssignedTickets(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeTicketService.viewAssignedTickets(
                employeeId
        ));
    }

    // GET /employee/{employeeId}/tickets/get/assignedTickets/pages?page=0&size=5&sort=createdat,asc
    // sortBy = createdAt, updatedAt, status, title
    @GetMapping("/{employeeId}/tickets/get/assignedTickets/pages")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> viewAssignedTicketsPaginated(
            @PathVariable Long employeeId,
            Pageable pageable) {
        Page<TicketResponseDTO> result = employeeTicketService.viewAssignedTicketsPaginated(
                employeeId,
                pageable
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Page loaded successfully.",
                        result
                )
        );
    }

    // PATCH /employee/{employeeId}/tickets/update/{ticketId}
    @PatchMapping("/{employeeId}/tickets/update/{ticketId}")
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
    @PatchMapping("/{employeeId}/tickets/updateStatus/{ticketId}")
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
    @PatchMapping("/{employeeId}/tickets/addRemarks/{ticketId}")
    public ResponseEntity<TicketResponseDTO> addTicketRemark(@PathVariable Long employeeId,
                                                             @PathVariable Long ticketId,
                                                             @RequestBody TicketAddRemarkRequestDTO request) {
        return ResponseEntity.ok(employeeTicketService.addRemarkToAssignedTicket(
                ticketId, employeeId, request.getRemark(), request.getNewStatus()));
    }

    // GET /employee/{employeeId}/tickets/get/filedTickets
    @GetMapping("/{employeeId}/tickets/get/filedTickets")
    public ResponseEntity<List<TicketResponseDTO>> getAllFiledTickets(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeTicketService.getAllFiledTickets(
                employeeId
        ));
    }

    // GET /employee/{employeeId}/tickets/get/filedTickets/pages?page=0&size=5&sort=createdat,asc
    // sortBy = createdAt, updatedAt, title
    @GetMapping("/{employeeId}/tickets/get/filedTickets/pages")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> getAllFiledTicketsPaginated(
            @PathVariable Long employeeId,
            Pageable pageable) {
        Page<TicketResponseDTO> result = employeeTicketService.getAllFiledTicketsPaginated(
                employeeId,
                pageable
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Page loaded successfully.",
                        result
                )
        );
    }

    // GET /employee/{employeeId}/tickets/get/filedTicket/{ticketId}
    @GetMapping("/{employeeId}/tickets/get/filedTicket/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getFiledTicket(@PathVariable Long employeeId, @PathVariable Long ticketId) {
        return ResponseEntity.ok(employeeTicketService.getFiledTicket(
                employeeId,
                ticketId
        ));
    }

    // GET /employee/{employeeId}/tickets/search/assigned?page=0&size=5&title=abc&status=FILED&sort=title,asc
    // sortBy = title, createdDate, updatedDate, status
    @GetMapping("/{employeeId}/tickets/search/assigned")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> searchAssignedTickets(
            @PathVariable Long employeeId,
            TicketSearchAssignedRequestDTO searchRequest,
            Pageable pageable
    ) {
        Page<TicketResponseDTO> result = employeeTicketService.searchAssignedTickets(
                employeeId,
                searchRequest,
                pageable
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Search completed successfully.",
                        result
                )
        );
    }

    // GET /employee/{employeeId}/tickets/search/filed?page=0&size=5&title=abc&sort=title,asc
    // sortBy = title, createdDate, updatedDate
    @GetMapping("/{employeeId}/tickets/search/filed")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> searchFiledTickets(
            @PathVariable Long employeeId,
            TicketSearchFiledRequestDTO searchRequest,
            Pageable pageable
    ) {
        Page<TicketResponseDTO> result = employeeTicketService.searchFiledTickets(
                employeeId,
                searchRequest,
                pageable
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Search completed successfully.",
                        result
                )
        );
    }
}
