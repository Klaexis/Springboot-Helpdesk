package com.helpdesk.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketNumber;

    private String ticketTitle;
    private String ticketBody;
    private String ticketAssignee;

    @CreationTimestamp
    private LocalDateTime ticketCreatedDate;

    private String ticketCreatedBy;

    @UpdateTimestamp
    private LocalDateTime ticketUpdatedDate;

    private String ticketUpdatedBy;
    private String ticketRemarks;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    // Constructors
    public Ticket() {}

    public Ticket(Long ticketNumber,
                  String ticketTitle,
                  String ticketBody,
                  LocalDateTime ticketCreatedDate,
                  String ticketCreatedBy) {
        this.ticketNumber = ticketNumber;
        this.ticketTitle = ticketTitle;
        this.ticketBody = ticketBody;
        this.ticketCreatedDate = ticketCreatedDate;
        this.ticketCreatedBy = ticketCreatedBy;
    }

    // Getters
    public Long getTicketNumber() {
        return ticketNumber;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public String getTicketBody() {
        return ticketBody;
    }

    public String getTicketAssignee() {
        return ticketAssignee;
    }

    public LocalDateTime getTicketCreatedDate() {
        return ticketCreatedDate;
    }

    public String getTicketCreatedBy() {
        return ticketCreatedBy;
    }

    public LocalDateTime getTicketUpdatedDate() {
        return ticketUpdatedDate;
    }

    public String getTicketUpdatedBy() {
        return ticketUpdatedBy;
    }

    public String getTicketRemarks() {
        return ticketRemarks;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    // Setters
    public void setTicketNumber(Long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public void setTicketBody(String ticketBody) {
        this.ticketBody = ticketBody;
    }

    public void setTicketAssignee(String ticketAssignee) {
        this.ticketAssignee = ticketAssignee;
    }

    public void setTicketCreatedDate(LocalDateTime ticketCreatedDate) {
        this.ticketCreatedDate = ticketCreatedDate;
    }

    public void setTicketCreatedBy(String ticketCreatedBy) {
        this.ticketCreatedBy = ticketCreatedBy;
    }

    public void setTicketUpdatedDate(LocalDateTime ticketUpdatedDate) {
        this.ticketUpdatedDate = ticketUpdatedDate;
    }

    public void setTicketUpdatedBy(String ticketUpdatedBy) {
        this.ticketUpdatedBy = ticketUpdatedBy;
    }

    public void setTicketRemarks(String ticketRemarks) {
        this.ticketRemarks = ticketRemarks;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

}