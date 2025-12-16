package com.helpdesk.repository;

import com.helpdesk.model.EmployeePosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {
    EmployeePosition findByPositionTitle(String positionTitle);
}