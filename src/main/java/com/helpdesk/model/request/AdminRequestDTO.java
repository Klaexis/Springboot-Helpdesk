package com.helpdesk.model.request;

import com.helpdesk.model.EmploymentStatus;
import com.helpdesk.model.response.EmployeePositionResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequestDTO {
    private String employeeName;
    private Integer employeeAge;
    private String employeeAddress;
    private String employeeContactNumber;
    private String employeeEmail;
    private EmployeePositionResponseDTO employeePosition;
    private EmploymentStatus employmentStatus;
}
