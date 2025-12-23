package com.helpdesk.repository.specification;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TicketSpecification {
    public Specification<Ticket> hasTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("ticketTitle")), "%" + title.toLowerCase() + "%");
    }

    public Specification<Ticket> hasAssignee(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("ticketAssignee").get("employeeName")),
                        "%" + name.toLowerCase() + "%");
    }

    public Specification<Ticket> hasStatus(TicketStatus status) {
        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("ticketStatus"), status);
    }

    public Specification<Ticket> hasCreatedDate(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) return null;

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            return cb.and(
                    cb.greaterThanOrEqualTo(
                            root.get("ticketCreatedDate"), startOfDay
                    ),
                    cb.lessThan(
                            root.get("ticketCreatedDate"), endOfDay
                    )
            );
        };
    }

    public Specification<Ticket> hasUpdatedDate(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) return null;

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            return cb.and(
                    cb.greaterThanOrEqualTo(
                            root.get("ticketUpdatedDate"), startOfDay
                    ),
                    cb.lessThan(
                            root.get("ticketUpdatedDate"), endOfDay
                    )
            );
        };
    }

    public Specification<Ticket> assignedToEmployee(Long employeeId) {
        return (root, query, cb) ->
                cb.equal(root.get("ticketAssignee").get("id"), employeeId);
    }
}
