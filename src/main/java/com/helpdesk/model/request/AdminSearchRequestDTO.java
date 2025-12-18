package com.helpdesk.model.request;

import com.helpdesk.model.EmploymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminSearchRequestDTO {
    private String name;
    private String position;
    private EmploymentStatus status;
}
