package com.helpdesk.model.request;

import com.helpdesk.model.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class TicketUpdateRequestDTO {
    private Optional<String> ticketTitle = Optional.empty();
    private Optional<String> ticketBody = Optional.empty();
    private Optional<Long> ticketAssigneeId = Optional.empty();
    private Optional<TicketStatus> ticketStatus = Optional.empty();
    private Optional<String> remarkToAdd = Optional.empty();
}
