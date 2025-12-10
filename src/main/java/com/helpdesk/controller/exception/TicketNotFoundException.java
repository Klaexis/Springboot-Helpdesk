package com.helpdesk.controller.exception;

public class TicketNotFoundException extends BaseException {
    public TicketNotFoundException(Long ticketId) {
        super("Ticket with ID " + ticketId + " not found.");
    }
}