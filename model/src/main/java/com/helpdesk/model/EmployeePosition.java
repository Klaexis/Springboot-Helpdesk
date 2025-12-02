package com.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_position")
public class EmployeePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;

    private String positionTitle;

    // Constructors
    public EmployeePosition() {}

    public EmployeePosition(String position) {
        this.positionTitle = position;
    }

    // Getters
    public Long getPositionId() {
        return positionId;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    // Setters
    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }
}
