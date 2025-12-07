package com.helpdesk.model.response;

import com.helpdesk.model.EmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminResponseDTO {
    private Long employeeId;
    private String employeeName;
    private Integer employeeAge;
    private String employeeAddress;
    private String employeeContactNumber;
    private String employeeEmail;
    private EmployeePositionResponseDTO employeePosition;
    private EmploymentStatus employmentStatus;
}