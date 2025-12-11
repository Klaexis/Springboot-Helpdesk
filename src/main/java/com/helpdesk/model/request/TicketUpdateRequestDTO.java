package com.helpdesk.model.request;

import com.helpdesk.model.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketUpdateRequestDTO {
    private String ticketTitle;
    private String ticketBody;
    private TicketStatus ticketStatus;
}
