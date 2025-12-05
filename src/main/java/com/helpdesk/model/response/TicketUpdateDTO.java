package com.helpdesk.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.helpdesk.model.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TicketUpdateDTO {
    private Optional<String> ticketTitle = Optional.empty();
    private Optional<String> ticketBody = Optional.empty();
    private Optional<Long> ticketAssigneeId = Optional.empty();
    private Optional<TicketStatus> ticketStatus = Optional.empty();
    private Optional<String> ticketRemarks = Optional.empty();

}
