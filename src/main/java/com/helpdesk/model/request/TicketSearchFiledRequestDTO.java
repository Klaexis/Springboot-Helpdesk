package com.helpdesk.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TicketSearchFiledRequestDTO {
    private String title;
    private LocalDate createdat;
    private LocalDate updatedat;
}
