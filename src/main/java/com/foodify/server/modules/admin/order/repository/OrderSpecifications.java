package com.foodify.server.modules.admin.order.repository;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class OrderSpecifications {

    private OrderSpecifications() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Order> withFilters(String query, List<OrderStatus> status, Long restaurantId) {
        return (root, criteriaQuery, cb) -> {
            Predicate predicate = cb.conjunction();

            if (query != null && !query.trim().isEmpty()) {
                String likePattern = "%" + query.toLowerCase() + "%";

                Join<Object, Object> restaurant = root.join("restaurant");

                Predicate queryPredicate = cb.like(cb.lower(restaurant.get("name")), likePattern);

                try {
                    Long id = Long.parseLong(query);
                    queryPredicate = cb.or(queryPredicate, cb.equal(root.get("id"), id));
                } catch (NumberFormatException ignored) {
                }

                predicate = cb.and(predicate, queryPredicate);
            }

            if (status != null && !status.isEmpty()) {
                predicate = cb.and(predicate, root.get("status").in(status));
            }

            if (restaurantId != null) {
                Join<Object, Object> restaurant = root.join("restaurant");
                predicate = cb.and(predicate, cb.equal(restaurant.get("id"), restaurantId));
            }

            return predicate;
        };
    }
}