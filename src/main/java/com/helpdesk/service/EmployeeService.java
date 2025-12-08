package com.helpdesk.service;

import com.helpdesk.model.response.EmployeeProfileResponseDTO;

public interface EmployeeService {
    EmployeeProfileResponseDTO viewOwnProfile(Long employeeId);
}
