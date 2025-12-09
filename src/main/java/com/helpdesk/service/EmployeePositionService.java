package com.helpdesk.service;

import com.helpdesk.model.EmployeePosition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeePositionService {
    EmployeePosition findPosition(Long adminId, Long positionId);

    Page<EmployeePosition> getAllPositionsPaginated(Long adminId, int page, int size);

    List<EmployeePosition> getAllPositions(Long adminId);

    EmployeePosition createPosition(Long adminId, String positionTitle);

    EmployeePosition updatePosition(Long adminId, Long positionId, String positionTitle);

    void deletePosition(Long adminId, Long positionId);
}
