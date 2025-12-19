package com.helpdesk.service;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmployeePosition;
import com.helpdesk.model.Ticket;

public interface ValidationService {
    Employee validateAdmin(Long adminId);

    Employee validateEmployee(Long employeeId);

    Employee getEmployeeOrThrow(Long employeeId);

    Ticket getTicketOrThrow(Long ticketId);

    EmployeePosition getPositionOrThrow(Long positionId);

    void handleTicketClosure(Ticket ticket);
}
