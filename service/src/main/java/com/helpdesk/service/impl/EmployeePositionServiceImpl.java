package com.helpdesk.service.impl;

import com.helpdesk.model.EmployeePosition;
import com.helpdesk.repository.EmployeePositionRepository;
import com.helpdesk.service.EmployeePositionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeePositionServiceImpl implements EmployeePositionService {
    @Autowired
    private final EmployeePositionRepository positionRepository;

    public EmployeePositionServiceImpl(EmployeePositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public EmployeePosition findPosition(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }

    public List<EmployeePosition> getAllPositions() {
        return positionRepository.findAll();
    }

    public EmployeePosition createPosition(String title) {
        EmployeePosition position = new EmployeePosition();
        position.setPositionTitle(title);
        return positionRepository.save(position);
    }

    public EmployeePosition updatePosition(Long id, String title) {
        EmployeePosition position = positionRepository.findById(id).orElseThrow(() -> new RuntimeException("Position not found"));
        position.setPositionTitle(title);
        return positionRepository.save(position);
    }

    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }
}
