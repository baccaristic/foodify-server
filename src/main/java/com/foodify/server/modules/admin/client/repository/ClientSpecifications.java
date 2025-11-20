package com.foodify.server.modules.admin.client.repository;

import com.foodify.server.modules.identity.domain.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ClientSpecifications {

    private ClientSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Client> withFilters(
            String query,
            BigDecimal minPoints,
            BigDecimal maxPoints,
            Boolean active
    ) {
        return (root, criteriaQuery, cb) -> {
            Predicate predicate = cb.conjunction();

            predicate = applySearchQuery(query, root, cb, predicate);
            predicate = applyPointsFilters(minPoints, maxPoints, root, cb, predicate);
            predicate = applyActiveFilter(active, root, cb, predicate);

            return predicate;
        };
    }

    private static Predicate applySearchQuery(String query, Root<Client> root, CriteriaBuilder cb, Predicate predicate) {
        if (query == null || query.trim().isEmpty()) {
            return predicate;
        }

        String likePattern = "%" + query.toLowerCase() + "%";
        Predicate queryPredicate = cb.or(
                cb.like(cb.lower(root.get("name")), likePattern),
                cb.like(cb.lower(root.get("email")), likePattern),
                cb.like(cb.lower(root.get("phoneNumber")), likePattern)
        );

        try {
            Long id = Long.parseLong(query);
            queryPredicate = cb.or(queryPredicate, cb.equal(root.get("id"), id));
        } catch (NumberFormatException ignored) {
            // Not a number, continue with string search
        }

        return cb.and(predicate, queryPredicate);
    }

    private static Predicate applyPointsFilters(BigDecimal minPoints, BigDecimal maxPoints, 
                                                Root<Client> root, CriteriaBuilder cb, Predicate predicate) {
        Predicate result = predicate;
        
        if (minPoints != null) {
            result = cb.and(result, cb.greaterThanOrEqualTo(root.get("loyaltyPointsBalance"), minPoints));
        }

        if (maxPoints != null) {
            result = cb.and(result, cb.lessThanOrEqualTo(root.get("loyaltyPointsBalance"), maxPoints));
        }

        return result;
    }

    private static Predicate applyActiveFilter(Boolean active, Root<Client> root, 
                                              CriteriaBuilder cb, Predicate predicate) {
        if (active != null) {
            return cb.and(predicate, cb.equal(root.get("enabled"), active));
        }
        return predicate;
    }
}