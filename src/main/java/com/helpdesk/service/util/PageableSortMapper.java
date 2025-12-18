package com.helpdesk.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PageableSortMapper {

    public Pageable map(
            Pageable pageable,
            String defaultSortField,
            Map<String, String> allowedSortFields
    ) {
        Sort sort;

        if (pageable.getSort().isUnsorted()) {
            sort = Sort.by(defaultSortField).ascending();
        } else {
            List<Sort.Order> orders = new ArrayList<>();

            for (Sort.Order order : pageable.getSort()) {
                String mappedField = allowedSortFields.get(
                        order.getProperty().toLowerCase()
                );

                if (mappedField == null) {
                    throw new IllegalArgumentException(
                            "Invalid sort field: " + order.getProperty()
                    );
                }

                orders.add(
                        new Sort.Order(order.getDirection(), mappedField)
                );
            }

            sort = Sort.by(orders);
        }

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );
    }
}