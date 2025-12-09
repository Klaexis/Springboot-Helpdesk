package com.helpdesk.service.impl;

import com.helpdesk.model.Employee;
import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketRemark;
import com.helpdesk.model.TicketStatus;
import com.helpdesk.model.request.TicketUpdateRequestDTO;
import com.helpdesk.model.response.TicketResponseDTO;
import com.helpdesk.repository.EmployeeRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.service.AdminTicketService;
import com.helpdesk.service.mapper.TicketMapper;
import com.helpdesk.service.util.EmployeeValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminTicketServiceImpl implements AdminTicketService {
    private final TicketRepository ticketRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeValidationHelper employeeValidationHelper;

    @Autowired
    public AdminTicketServiceImpl(TicketRepository ticketRepository,
                                  EmployeeRepository employeeRepository,
                                  EmployeeValidationHelper employeeValidationHelper) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidationHelper = employeeValidationHelper;
    }

    private Employee validateAdmin(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        employeeValidationHelper.validateAdmin(admin);
        employeeValidationHelper.validateActive(admin);

        return admin;
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    private Ticket getTicketOrThrow(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    private void handleTicketClosure(Ticket ticket) {
        if (ticket.getTicketStatus() == TicketStatus.CLOSED && ticket.getTicketAssignee() != null) {
            Employee assignee = ticket.getTicketAssignee();
            assignee.getAssignedTickets().remove(ticket); // remove ticket from employee
            ticket.setTicketAssignee(null); // optional: clear assignee
            employeeRepository.save(assignee); // persist change
        }
    }

    @Override
    public TicketResponseDTO assignTicket(Long ticketId,
                                          Long adminId,
                                          Long employeeId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);
        Employee employee = getEmployeeOrThrow(employeeId);

        ticket.setTicketAssignee(employee);
        ticket.setTicketUpdatedBy(admin);
        ticket.setTicketStatus(TicketStatus.IN_PROGRESS);

        if (!employee.getAssignedTickets().contains(ticket)) {
            employee.getAssignedTickets().add(ticket);
        }

        employeeRepository.save(employee);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO getTicket(Long adminId,
                                       Long ticketId) {
        validateAdmin(adminId);

        return TicketMapper.toTicketDTO(getTicketOrThrow(ticketId));
    }

    @Override
    public List<TicketResponseDTO> getAllTickets(Long adminId) {
        validateAdmin(adminId);

        return ticketRepository.findAll()
                .stream()
                .map(TicketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDTO updateTicket(Long ticketId,
                                          TicketUpdateRequestDTO updatedTicket,
                                          Long adminId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        Employee assignee = null;
        if (updatedTicket.getTicketAssigneeId() != null) {
            assignee = getEmployeeOrThrow(updatedTicket.getTicketAssigneeId());
        }

        TicketMapper.updateEntityFromDTO(updatedTicket, ticket, admin, assignee);
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO updateTicketStatus(Long ticketId,
                                                TicketStatus newStatus,
                                                Long adminId) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        ticket.setTicketStatus(newStatus);
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDTO addTicketRemark(Long ticketId,
                                             Long adminId,
                                             String remark,
                                             TicketStatus newStatus) {
        Ticket ticket = getTicketOrThrow(ticketId);
        Employee admin = validateAdmin(adminId);

        TicketRemark ticketRemark = new TicketRemark(remark, admin, ticket);
        ticket.getTicketRemarks().add(ticketRemark);

        if (newStatus != null) {
            ticket.setTicketStatus(newStatus);
        }
        ticket.setTicketUpdatedBy(admin);

        handleTicketClosure(ticket);

        return TicketMapper.toTicketDTO(ticketRepository.save(ticket));
    }
}
