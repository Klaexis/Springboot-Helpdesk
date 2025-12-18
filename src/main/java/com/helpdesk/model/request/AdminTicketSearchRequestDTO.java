package com.helpdesk.model.request;

import com.helpdesk.model.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminTicketSearchRequestDTO {
    private String title;
    private String assignee;
    private TicketStatus status;
}
