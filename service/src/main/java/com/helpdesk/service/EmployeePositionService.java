package com.helpdesk.service;

import com.helpdesk.model.EmployeePosition;

import java.util.List;

public interface EmployeePositionService {
    EmployeePosition findPosition(Long id);

    List<EmployeePosition> getAllPositions();

    EmployeePosition createPosition(String title);

    EmployeePosition updatePosition(Long id, String title);

    void deletePosition(Long id);
}
