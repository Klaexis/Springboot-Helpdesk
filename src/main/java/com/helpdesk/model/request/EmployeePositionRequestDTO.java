package com.helpdesk.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeePositionRequestDTO {
    @NotBlank(message = "Title is required")
    private String positionTitle;
}
