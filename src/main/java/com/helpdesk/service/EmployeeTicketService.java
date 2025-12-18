package com.helpdesk.service;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketCreateRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTicketService {
    TicketResponseDTO fileTicket(TicketCreateRequestDTO ticket, Long employeeId);

    List<TicketResponseDTO> viewAssignedTickets(Long employeeId);

    Page<TicketResponseDTO> viewAssignedTicketsPaginated(Long employeeId, Pageable pageable);

    TicketResponseDTO updateOwnTicket(Long ticketId, TicketUpdateRequestDTO updatedTicket, Long employeeId);

    TicketResponseDTO updateOwnTicketStatus(Long ticketId, TicketStatus newStatus, Long employeeId);

    List<TicketResponseDTO> getAllFiledTickets(Long employeeId);

    Page<TicketResponseDTO> getAllFiledTicketsPaginated(Long employeeId, Pageable pageable);

    TicketResponseDTO getFiledTicket(Long employeeId, Long ticketId);

    TicketResponseDTO addRemarkToAssignedTicket(Long ticketId, Long employeeId, String remark, TicketStatus newStatus);

    Page<TicketResponseDTO> searchAssignedTickets(
            Long employeeId,
            String title,
            LocalDate createdDate,
            LocalDate updatedDate,
            TicketStatus status,
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<TicketResponseDTO> searchFiledTickets(
            Long employeeId,
            String title,
            LocalDate createdDate,
            LocalDate updatedDate,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
