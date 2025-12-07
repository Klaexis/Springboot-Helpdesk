package com.helpdesk.model.request;

import com.helpdesk.model.TicketStatus;

public class AddTicketRemarkRequestDTO {
    private String remark;
    private TicketStatus newStatus;

    // Getters
    public String getRemark() { return remark; }

    public TicketStatus getNewStatus() { return newStatus; }

    // Setters
    public void setRemark(String remark) { this.remark = remark; }

    public void setNewStatus(TicketStatus newStatus) { this.newStatus = newStatus; }
}