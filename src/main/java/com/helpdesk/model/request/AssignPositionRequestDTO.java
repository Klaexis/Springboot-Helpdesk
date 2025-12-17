package com.helpdesk.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssignPositionRequestDTO {
    @NotBlank(message = "Position title is required")
    private String positionTitle;
}
