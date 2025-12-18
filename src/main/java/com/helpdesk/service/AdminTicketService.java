package com.helpdesk.service;

import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.AdminTicketSearchRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminTicketService {
    TicketResponseDTO assignTicket(Long ticketId, Long adminId, Long employeeId);

    Page<TicketResponseDTO> getAllTicketsPaginated(Long adminId, Pageable pageable);

    TicketResponseDTO getTicket(Long adminId, Long ticketId);

    List<TicketResponseDTO> getAllTickets(Long adminId);

    TicketResponseDTO updateTicket(Long ticketId, TicketUpdateRequestDTO updatedTicket, Long adminId);

    TicketResponseDTO updateTicketStatus(Long ticketId, TicketStatus newStatus, Long adminId);

    TicketResponseDTO addTicketRemark(Long ticketId, Long adminId, String remark, TicketStatus newStatus);

    void deleteTicket(Long adminId, Long ticketId);

    Page<TicketResponseDTO> searchTickets(Long adminId, AdminTicketSearchRequestDTO adminTicketSearchRequestDTO, Pageable pageable);
}
