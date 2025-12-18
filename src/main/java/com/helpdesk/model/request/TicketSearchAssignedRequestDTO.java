package com.helpdesk.model.request;

import com.helpdesk.model.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TicketSearchAssignedRequestDTO {
    private String title;
    private LocalDate createdat;
    private LocalDate updatedat;
    private TicketStatus status;
}
