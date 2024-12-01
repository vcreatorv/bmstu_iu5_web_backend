package com.valer.provider_service.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.valer.provider_service.models.ConnectionRequest;
import com.valer.provider_service.models.User;

import jakarta.persistence.criteria.Predicate;


public class ConnectionRequestSpecifications {

    public static Specification<ConnectionRequest> filterByParams(User client, LocalDateTime startDatetime, LocalDateTime endDatetime, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (client != null) {
                predicates.add(criteriaBuilder.equal(root.get("client"), client));
            }

            if (startDatetime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("formationDatetime"), startDatetime));
            }

            if (endDatetime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("formationDatetime"), endDatetime));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            predicates.add(root.get("status").in("DELETED", "DRAFT").not());

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
