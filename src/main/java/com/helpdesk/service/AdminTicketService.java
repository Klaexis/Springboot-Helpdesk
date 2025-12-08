package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;

import java.util.List;

public interface AdminTicketService {
    TicketResponseDTO assignTicket(Long ticketId, Long adminId, Long employeeId);

    TicketResponseDTO getTicket(Long adminId, Long ticketId);

    List<TicketResponseDTO> getAllTickets(Long adminId);

    TicketResponseDTO updateTicket(Long ticketId, TicketUpdateRequestDTO updatedTicket, Long adminId);

    TicketResponseDTO updateTicketStatus(Long ticketId, TicketStatus newStatus, Long adminId);

    TicketResponseDTO addTicketRemark(Long ticketId, Long adminId, String remark, TicketStatus newStatus);
}
