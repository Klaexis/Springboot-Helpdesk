package com.helpdesk.model.response;

import com.helpdesk.model.TicketStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TicketResponseDTO {
    private Long ticketId;
    private String ticketTitle;
    private String ticketBody;
    private TicketEmployeeResponseDTO ticketAssignee;
    private LocalDateTime ticketCreatedDate;
    private TicketEmployeeResponseDTO ticketCreatedBy;
    private LocalDateTime ticketUpdatedDate;
    private TicketEmployeeResponseDTO ticketUpdatedBy;
    private List<TicketRemarkResponseDTO> ticketRemarks;
    private TicketStatus ticketStatus;
}