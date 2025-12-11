package com.helpdesk.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminCreateRequestDTO {
    @NotBlank(message = "Name is required")
    private String employeeName;

    @NotNull(message = "Age is required")
    private Integer employeeAge;

    private String employeeAddress;
    private String employeeContactNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String employeeEmail;

    private String positionTitle;

    @NotBlank(message = "Employment status is required")
    private String employmentStatus;
}
