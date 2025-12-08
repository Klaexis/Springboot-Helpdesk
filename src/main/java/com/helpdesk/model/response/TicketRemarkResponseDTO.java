package com.helpdesk.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketRemarkResponseDTO {
    private Long remarkId;
    private String message;
    private TicketEmployeeResponseDTO createdBy;
    private LocalDateTime createdAt;
}
