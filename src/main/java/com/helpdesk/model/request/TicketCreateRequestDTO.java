package com.helpdesk.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketCreateRequestDTO {
    private String ticketTitle;
    private String ticketBody;
}
