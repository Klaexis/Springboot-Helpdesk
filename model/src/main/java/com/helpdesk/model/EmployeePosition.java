package com.helpdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_position")
public class EmployeePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    // Constructors
    public EmployeePosition() {}

    public EmployeePosition(String position) {
        this.position = position;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
