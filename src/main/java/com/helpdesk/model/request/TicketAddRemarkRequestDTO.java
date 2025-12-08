package com.helpdesk.model.request;

import com.helpdesk.model.TicketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketAddRemarkRequestDTO {
    private String remark;
    private TicketStatus newStatus;
}