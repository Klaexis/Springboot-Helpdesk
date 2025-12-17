package com.helpdesk.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminResponseDTO {
    private Long id;
    private String employeeName;
    private Integer employeeAge;
    private String employeeAddress;
    private String employeeContactNumber;
    private String employeeEmail;
    private String positionTitle;
    private String employmentStatus;
    private List<TicketAssignedResponseDTO> assignedTickets;
}