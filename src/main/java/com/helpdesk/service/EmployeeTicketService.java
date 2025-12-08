package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;

import java.util.List;

public interface EmployeeTicketService {
    TicketResponseDTO fileTicket(Ticket ticket, Long employeeId);

    List<TicketResponseDTO> viewAssignedTickets(Long employeeId);

    TicketResponseDTO updateOwnTicket(Long ticketId, TicketUpdateRequestDTO updatedTicket, Long employeeId);

    TicketResponseDTO updateOwnTicketStatus(Long ticketId, TicketStatus newStatus, Long employeeId);

    List<TicketResponseDTO> getAllFiledTickets(Long employeeId);

    TicketResponseDTO getFiledTicket(Long employeeId, Long ticketId);

    TicketResponseDTO addRemarkToAssignedTicket(Long ticketId, Long employeeId, String remark, TicketStatus newStatus);
}
