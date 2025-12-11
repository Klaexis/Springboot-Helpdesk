package com.helpdesk.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminUpdateRequestDTO {
    private String employeeName;
    private Integer employeeAge;
    private String employeeAddress;
    private String employeeContactNumber;
    private String employeeEmail;
    private String positionTitle;
    private String employmentStatus;
}
