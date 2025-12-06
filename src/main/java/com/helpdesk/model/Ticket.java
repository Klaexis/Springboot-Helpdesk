package com.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private String ticketTitle;
    private String ticketBody;

    @ManyToOne
    @JoinColumn(name = "ticket_assignee_id")
    private Employee ticketAssignee;

    @CreationTimestamp
    private LocalDateTime ticketCreatedDate;

    @ManyToOne
    @JoinColumn(name = "ticket_created_by_id")
    private Employee ticketCreatedBy;

    @UpdateTimestamp
    private LocalDateTime ticketUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "ticket_updated_by_id")
    private Employee ticketUpdatedBy;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TicketRemark> ticketRemarks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    // Constructors
    public Ticket() {}

    public Ticket(Long ticketId,
                  String ticketTitle,
                  String ticketBody,
                  LocalDateTime ticketCreatedDate,
                  Employee ticketCreatedBy) {
        this.ticketId = ticketId;
        this.ticketTitle = ticketTitle;
        this.ticketBody = ticketBody;
        this.ticketCreatedDate = ticketCreatedDate;
        this.ticketCreatedBy = ticketCreatedBy;
    }

    // Getters
    public Long getTicketId() {
        return ticketId;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public String getTicketBody() {
        return ticketBody;
    }

    public Employee getTicketAssignee() {
        return ticketAssignee;
    }

    public LocalDateTime getTicketCreatedDate() {
        return ticketCreatedDate;
    }

    public Employee getTicketCreatedBy() {
        return ticketCreatedBy;
    }

    public LocalDateTime getTicketUpdatedDate() {
        return ticketUpdatedDate;
    }

    public Employee getTicketUpdatedBy() {
        return ticketUpdatedBy;
    }

    public List<TicketRemark> getTicketRemarks() {
        return ticketRemarks;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    // Setters
    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public void setTicketBody(String ticketBody) {
        this.ticketBody = ticketBody;
    }

    public void setTicketAssignee(Employee ticketAssignee) {
        this.ticketAssignee = ticketAssignee;
    }

    public void setTicketCreatedDate(LocalDateTime ticketCreatedDate) {
        this.ticketCreatedDate = ticketCreatedDate;
    }

    public void setTicketCreatedBy(Employee ticketCreatedBy) {
        this.ticketCreatedBy = ticketCreatedBy;
    }

    public void setTicketUpdatedDate(LocalDateTime ticketUpdatedDate) {
        this.ticketUpdatedDate = ticketUpdatedDate;
    }

    public void setTicketUpdatedBy(Employee ticketUpdatedBy) {
        this.ticketUpdatedBy = ticketUpdatedBy;
    }

    public void setTicketRemarks(List<TicketRemark> ticketRemarks) {
        this.ticketRemarks = ticketRemarks;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

}