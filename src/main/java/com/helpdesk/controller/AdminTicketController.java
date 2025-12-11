package com.helpdesk.controller;

import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketAddRemarkRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.PageResponseDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.service.AdminTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminTicketService.getAllTickets(
                adminId
        ));
    }

    // GET /admin/{adminId}/tickets/pages?page=0&size=5&sortBy=createdAt&direction=asc
    @GetMapping("/pages")
    public ResponseEntity<PageResponseDTO<Page<TicketResponseDTO>>> getAllTicketsPaginated(
            @PathVariable Long adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<TicketResponseDTO> result = adminTicketService.getAllTicketsPaginated(
                adminId, page, size, sortBy, direction);

        return ResponseEntity.ok(
            new PageResponseDTO<>(
                "Page loaded successfully.",
                result
            )
        );
    }

    // GET /admin/{adminId}/tickets/find/{ticketId}
    @GetMapping("/find/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Long adminId,
                                                       @PathVariable Long ticketId) {
        return ResponseEntity.ok(adminTicketService.getTicket(
                adminId,
                ticketId
        ));
    }

    // PATCH /admin/{adminId}/tickets/assign/{ticketId}/assignTo/{employeeId}
    @PatchMapping("assign/{ticketId}/assignTo/{employeeId}")
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
    @PatchMapping("update/{ticketId}")
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
    @PatchMapping("/update/{ticketId}/status")
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
    @PatchMapping("/addRemarks/{ticketId}")
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
}
