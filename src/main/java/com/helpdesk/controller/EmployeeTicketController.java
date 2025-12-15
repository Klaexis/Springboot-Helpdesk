package com.helpdesk.controller;

import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketAddRemarkRequestDTO;
import com.helpdesk.model.request.TicketCreateRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.PageResponseDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.service.EmployeeTicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<TicketResponseDTO> fileTicket(@RequestBody TicketCreateRequestDTO ticket,
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

    // GET /employee/{employeeId}/tickets/get/assignedTickets/pages?page=0&size=5&sortBy=createdAt&direction=asc
    // sortBy = createdAt, updatedAt, status, title
    @GetMapping("/get/assignedTickets/pages")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> viewAssignedTicketsPaginated(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<TicketResponseDTO> result = employeeTicketService.viewAssignedTicketsPaginated(
                employeeId,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Page loaded successfully.",
                        result
                )
        );
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

    // GET /employee/{employeeId}/tickets/get/filedTickets/pages?page=0&size=5&sortBy=createdAt&direction=asc
    // sortBy = createdAt, updatedAt, title
    @GetMapping("/get/filedTickets/pages")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> getAllFiledTicketsPaginated(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<TicketResponseDTO> result = employeeTicketService.getAllFiledTicketsPaginated(
                employeeId,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Page loaded successfully.",
                        result
                )
        );
    }

    // GET /employee/{employeeId}/tickets/get/filedTicket/{ticketId}
    @GetMapping("/get/filedTicket/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getFiledTicket(@PathVariable Long employeeId, @PathVariable Long ticketId) {
        return ResponseEntity.ok(employeeTicketService.getFiledTicket(
                employeeId,
                ticketId
        ));
    }

    // GET /employee/{employeeId}/tickets/search/assigned?page=0&size=5&title=abc&status=FILED&sortBy=title&direction=asc
    // sortBy = title, createdDate, updatedDate, status
    @GetMapping("/search/assigned")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> searchAssignedTickets(
            @PathVariable Long employeeId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate createdDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate updatedDate,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<TicketResponseDTO> result = employeeTicketService.searchAssignedTickets(
                employeeId,
                title,
                createdDate,
                updatedDate,
                status,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Search completed successfully.",
                        result
                )
        );
    }

    // GET /employee/{employeeId}/tickets/search/filed?page=0&size=5&title=abc&sortBy=title&direction=asc
    // sortBy = title, createdDate, updatedDate
    @GetMapping("/search/filed")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> searchFiledTickets(
            @PathVariable Long employeeId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate createdDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate updatedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<TicketResponseDTO> result = employeeTicketService.searchFiledTickets(
                employeeId,
                title,
                createdDate,
                updatedDate,
                page,
                size,
                sortBy,
                direction
        );

        return ResponseEntity.ok(
                new PageResponseDTO<>(
                        "Search completed successfully.",
                        result
                )
        );
    }
}
