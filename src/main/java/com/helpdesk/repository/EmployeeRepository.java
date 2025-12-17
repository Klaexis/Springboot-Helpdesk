package com.helpdesk.repository;

import com.helpdesk.model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.assignedTickets WHERE e.id = :id")
    Employee findByIdWithTickets(@Param("id") Long id);
}