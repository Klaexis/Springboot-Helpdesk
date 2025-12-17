package com.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_position")
public class EmployeePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String positionTitle;

    // Constructors
    public EmployeePosition() {}

    public EmployeePosition(String position) {
        this.positionTitle = position;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    // Setters
    public void setId(Long positionId) {
        this.id = positionId;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }
}
