package com.helpdesk.service.mapper;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.request.TicketCreateRequestDTO;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketEmployeeResponseDTO;
import com.helpdesk.model.response.TicketRemarkResponseDTO;
import com.helpdesk.model.response.TicketResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TicketMapper {
    public static Ticket toEntity(TicketCreateRequestDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setTicketTitle(dto.getTicketTitle());
        ticket.setTicketBody(dto.getTicketBody());
        return ticket;
    }

    public static TicketEmployeeResponseDTO toEmployeeDTO(com.helpdesk.model.Employee employee) {
        if (employee == null) return null;

        TicketEmployeeResponseDTO dto = new TicketEmployeeResponseDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setEmployeeName(employee.getEmployeeName());

        return dto;
    }

    public static TicketRemarkResponseDTO toRemarkDTO(TicketRemark remark) {
        if (remark == null) return null;

        TicketRemarkResponseDTO dto = new TicketRemarkResponseDTO();
        dto.setRemarkId(remark.getRemarkId());
        dto.setMessage(remark.getMessage());
        dto.setCreatedBy(toEmployeeDTO(remark.getCreatedBy()));
        dto.setCreatedAt(remark.getCreatedAt());

        return dto;
    }

    public static TicketResponseDTO toTicketDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setTicketId(ticket.getTicketId());
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
                .map(TicketMapper::toRemarkDTO)
                .collect(Collectors.toList());
        dto.setTicketRemarks(remarks);

        return dto;
    }

    public static void updateEntity(TicketUpdateRequestDTO dto, Ticket ticket, Employee updater, Employee assignee) {
        if (dto.getTicketTitle() != null) ticket.setTicketTitle(dto.getTicketTitle());
        if (dto.getTicketBody() != null) ticket.setTicketBody(dto.getTicketBody());
        if (assignee != null) ticket.setTicketAssignee(assignee);
        if (dto.getTicketStatus() != null) ticket.setTicketStatus(dto.getTicketStatus());
        if (dto.getRemarkToAdd() != null) {
            ticket.getTicketRemarks().add(new TicketRemark(dto.getRemarkToAdd(), updater, ticket));
        }
    }
}
