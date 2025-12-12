package com.helpdesk.repository.specification;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.TicketStatus;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecification {
    public static Specification<Ticket> hasTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("ticketTitle")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Ticket> hasAssignee(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("ticketAssignee").get("employeeName")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<Ticket> hasStatus(TicketStatus status) {
        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("ticketStatus"), status);
    }

    public static Specification<Ticket> isAssignedTo(Long employeeId) {
        return (root, query, cb) ->
                employeeId == null
                        ? null
                        : cb.equal(root.get("ticketAssignee").get("employeeId"), employeeId);
    }
}
