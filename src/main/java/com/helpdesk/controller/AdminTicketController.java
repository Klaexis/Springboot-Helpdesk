package com.helpdesk.controller;

import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.AdminTicketSearchRequestDTO;
import com.helpdesk.model.request.TicketAddRemarkRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.PageResponseDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.service.AdminTicketService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminTicketController {
    private final AdminTicketService adminTicketService;

    public AdminTicketController(AdminTicketService adminTicketService) {
        this.adminTicketService = adminTicketService;
    }

    // GET /admin/{adminId}/tickets
    @GetMapping("/{adminId}/tickets")
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminTicketService.getAllTickets(
                adminId
        ));
    }

    // GET /admin/{adminId}/tickets/pages?page=0&size=5&sort=createdat,asc
    // sortBy = createdAt, updatedAt, status, title
    @GetMapping("/{adminId}/tickets/pages")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> getAllTicketsPaginated(
            @PathVariable Long adminId,
            Pageable pageable
    ) {
        Page<TicketResponseDTO> result = adminTicketService.getAllTicketsPaginated(
                adminId, pageable);

        return ResponseEntity.ok(
            new PageResponseDTO<>(
                "Page loaded successfully.",
                result
            )
        );
    }

    // GET /admin/{adminId}/tickets/find/{ticketId}
    @GetMapping("/{adminId}/tickets/find/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Long adminId,
                                                       @PathVariable Long ticketId) {
        return ResponseEntity.ok(adminTicketService.getTicket(
                adminId,
                ticketId
        ));
    }

    // PATCH /admin/{adminId}/tickets/assign/{ticketId}/assignTo/{employeeId}
    @PatchMapping("/{adminId}/tickets/assign/{ticketId}/assignTo/{employeeId}")
    public ResponseEntity<TicketResponseDTO> assignTicket(@PathVariable Long adminId,
                                                          @PathVariable Long ticketId,
                                                          @PathVariable Long employeeId) {
        return ResponseEntity.ok(adminTicketService.assignTicket(
                ticketId,
                adminId,
                employeeId
        ));
    }

    // PATCH /admin/{adminId}/tickets/update/{ticketId}
    @PatchMapping("/{adminId}/tickets/update/{ticketId}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable Long adminId,
                                                          @RequestBody TicketUpdateRequestDTO updatedTicket,
                                                          @PathVariable Long ticketId) {
        return ResponseEntity.ok(adminTicketService.updateTicket(
                ticketId,
                updatedTicket,
                adminId
        ));
    }

    // PATCH /admin/{adminId}/tickets/update/{ticketId}/status
    @PatchMapping("/{adminId}/tickets/update/{ticketId}/status")
    public ResponseEntity<TicketResponseDTO> updateTicketStatus(@PathVariable Long adminId,
                                                                @RequestBody TicketStatus newStatus,
                                                                @PathVariable Long ticketId) {
        return ResponseEntity.ok(adminTicketService.updateTicketStatus(
                ticketId,
                newStatus,
                adminId
        ));
    }

    // PATCH /admin/{adminId}/tickets/addRemarks/{ticketId}
    @PatchMapping("/{adminId}/tickets/addRemarks/{ticketId}")
    public ResponseEntity<TicketResponseDTO> addTicketRemark(@PathVariable Long adminId,
                                                             @PathVariable Long ticketId,
                                                             @RequestBody TicketAddRemarkRequestDTO request) {
        return ResponseEntity.ok(adminTicketService.addTicketRemark(
                ticketId,
                adminId,
                request.getRemark(),
                request.getNewStatus()
        ));
    }

    @DeleteMapping("/{adminId}/tickets/delete/{ticketId}")
    public ResponseEntity<TicketResponseDTO> deleteTicket(@PathVariable Long adminId,
                                                          @PathVariable Long ticketId) {
        adminTicketService.deleteTicket(adminId, ticketId);
        return ResponseEntity.noContent().build();
    }

    // GET /admin/{adminId}/tickets/search?title=bug&assignee=John&status=IN_PROGRESS&page=0&size=5&sort=title,asc
    // sortBy = title, status, assignee
    @GetMapping("/{adminId}/tickets/search")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> searchTickets(
            @PathVariable Long adminId,
            AdminTicketSearchRequestDTO searchRequest,
            Pageable pageable
    ) {
        Page<TicketResponseDTO> result = adminTicketService.searchTickets(
                adminId,
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
