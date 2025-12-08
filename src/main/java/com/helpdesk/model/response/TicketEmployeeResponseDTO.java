package com.helpdesk.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketEmployeeResponseDTO {
    private Long employeeId;
    private String employeeName;
}
