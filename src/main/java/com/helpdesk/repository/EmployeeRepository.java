package com.helpdesk.repository;

import com.helpdesk.model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.assignedTickets WHERE e.employeeId = :id")
    Employee findByIdWithTickets(@Param("id") Long id);

    @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.assignedTickets")
    List<Employee> findAllWithTickets();
}