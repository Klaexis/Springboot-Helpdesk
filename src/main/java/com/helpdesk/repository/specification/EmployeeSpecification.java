package com.helpdesk.repository.specification;

import com.helpdesk.model.Employee;
import com.helpdesk.model.EmploymentStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {
    public static Specification<Employee> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank() ? null : cb.like(cb.lower(root.get("employeeName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Employee> hasPosition(String positionTitle) {
        return (root, query, cb) -> {
            if (positionTitle == null || positionTitle.isBlank()) return null;
            Join<Object, Object> positionJoin = root.join("employeePosition", JoinType.LEFT);
            return cb.equal(positionJoin.get("positionTitle"), positionTitle);
        };
    }

    public static Specification<Employee> hasStatus(EmploymentStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("employmentStatus"), status);
    }
}
