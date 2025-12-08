package com.helpdesk.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String employeeName;
    private Integer employeeAge;
    private String employeeAddress;
    private String  employeeContactNumber;
    private String employeeEmail;

    @ManyToOne
    @JoinColumn(name = "employee_title")
    private EmployeePosition employeePosition;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @OneToMany(mappedBy = "ticketAssignee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> assignedTickets = new ArrayList<>();

    // Constructors
    public Employee() {}

    public Employee(String employeeName,
                    Integer employeeAge,
                    String employeeAddress,
                    String employeeContactNumber,
                    String employeeEmail) {
        this.employeeName = employeeName;
        this.employeeAge = employeeAge;
        this.employeeAddress = employeeAddress;
        this.employeeContactNumber = employeeContactNumber;
        this.employeeEmail = employeeEmail;
    }

    // Getters
    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public Integer getEmployeeAge() {
        return employeeAge;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public String getEmployeeContactNumber() {
        return employeeContactNumber;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public EmployeePosition getEmployeePosition() {
        return employeePosition;
    }

    public List<Ticket> getAssignedTickets() {
        return assignedTickets;
    }
    // Setters
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeAge(Integer employeeAge) {
        this.employeeAge = employeeAge;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public void setEmployeeContactNumber(String employeeContactNumber) {
        this.employeeContactNumber = employeeContactNumber;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public void setEmployeePosition(EmployeePosition employeePosition) {
        this.employeePosition = employeePosition;
    }

    public void setAssignedTickets(List<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }
}