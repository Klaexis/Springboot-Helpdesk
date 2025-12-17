package com.helpdesk.service.mapper;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.request.TicketCreateRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketEmployeeResponseDTO;
import com.helpdesk.model.response.TicketRemarkResponseDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapper {
    public Ticket toEntity(TicketCreateRequestDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setTicketTitle(dto.getTicketTitle());
        ticket.setTicketBody(dto.getTicketBody());
        return ticket;
    }

    public TicketEmployeeResponseDTO toEmployeeDTO(com.helpdesk.model.Employee employee) {
        if (employee == null) return null;

        TicketEmployeeResponseDTO dto = new TicketEmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setEmployeeName(employee.getEmployeeName());

        return dto;
    }

    public TicketRemarkResponseDTO toRemarkDTO(TicketRemark remark) {
        if (remark == null) return null;

        TicketRemarkResponseDTO dto = new TicketRemarkResponseDTO();
        dto.setId(remark.getId());
        dto.setMessage(remark.getMessage());
        dto.setCreatedBy(toEmployeeDTO(remark.getCreatedBy()));
        dto.setCreatedAt(remark.getCreatedAt());

        return dto;
    }

    public TicketResponseDTO toTicketDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setTicketTitle(ticket.getTicketTitle());
        dto.setTicketBody(ticket.getTicketBody());
        dto.setTicketAssignee(toEmployeeDTO(ticket.getTicketAssignee()));
        dto.setTicketCreatedBy(toEmployeeDTO(ticket.getTicketCreatedBy()));
        dto.setTicketCreatedDate(ticket.getTicketCreatedDate());
        dto.setTicketUpdatedBy(toEmployeeDTO(ticket.getTicketUpdatedBy()));
        dto.setTicketUpdatedDate(ticket.getTicketUpdatedDate());
        dto.setTicketStatus(ticket.getTicketStatus());

        List<TicketRemarkResponseDTO> remarks = ticket.getTicketRemarks()
                .stream()
                .map(this::toRemarkDTO)
                .collect(Collectors.toList());
        dto.setTicketRemarks(remarks);

        return dto;
    }

    public void updateEntity(TicketUpdateRequestDTO dto, Ticket ticket) {
        if (dto.getTicketTitle() != null) ticket.setTicketTitle(dto.getTicketTitle());
        if (dto.getTicketBody() != null) ticket.setTicketBody(dto.getTicketBody());
        if (dto.getTicketStatus() != null) ticket.setTicketStatus(dto.getTicketStatus());
    }
}
