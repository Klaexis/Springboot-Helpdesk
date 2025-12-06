package com.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_remarks")
public class TicketRemark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long remarkId;

    private String message;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    @JsonBackReference
    private Ticket ticket;

    // Constructors
    public TicketRemark() {}

    public TicketRemark(String message, Employee createdBy, Ticket ticket) {
        this.message = message;
        this.createdBy = createdBy;
        this.ticket = ticket;
    }

    // Getters
    public Long getRemarkId() {
        return remarkId;
    }

    public String getMessage() {
        return message;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Ticket getTicket() {
        return ticket;
    }

    // Setters
    public void setRemarkId(Long remarkId) {
        this.remarkId = remarkId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
