package com.foodify.server.modules.admin.driver.repository;

import com.foodify.server.modules.identity.domain.Driver;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class DriverSpecifications {

    private DriverSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Driver> withFilters(String query, Boolean paid) {
        return (root, criteriaQuery, cb) -> {
            Predicate predicate = cb.conjunction();

            predicate = cb.and(predicate, cb.isTrue(root.get("enabled")));

            if (query != null && !query.trim().isEmpty()) {
                String likePattern = "%" + query.toLowerCase() + "%";

                Predicate queryPredicate = cb.or(
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(root.get("phone"), "%" + query + "%")
                );

                try {
                    Long id = Long.parseLong(query);
                    queryPredicate = cb.or(queryPredicate, cb.equal(root.get("id"), id));
                } catch (NumberFormatException ignored) {
                    // Query is not a number, continue with name/phone search only
                }

                predicate = cb.and(predicate, queryPredicate);
            }

            if (paid != null) {
                BigDecimal zero = BigDecimal.ZERO;

                Predicate hasOutstandingAmounts = cb.or(
                        cb.greaterThan(root.get("cashOnHand"), zero),
                        cb.greaterThan(root.get("unpaidEarnings"), zero),
                        cb.greaterThan(root.get("outstandingDailyFees"), zero)
                );

                if (paid) {
                    predicate = cb.and(predicate, cb.not(hasOutstandingAmounts));
                } else {
                    predicate = cb.and(predicate, hasOutstandingAmounts);
                }
            }

            return predicate;
        };
    }
}