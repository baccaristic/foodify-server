package com.foodify.server.modules.admin.restaurant.repository;

import com.foodify.server.modules.restaurants.domain.Restaurant;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RestaurantSpecification {

    public static Specification<Restaurant> search(String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (query == null || query.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + query.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            // Search by name in different languages
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nameEn")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nameFr")), searchPattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nameAr")), searchPattern));
            
            // Search by address
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), searchPattern));
            
            // Search by ID (if query is numeric)
            try {
                Long id = Long.parseLong(query);
                predicates.add(criteriaBuilder.equal(root.get("id"), id));
            } catch (NumberFormatException e) {
                // Not a number, skip ID search
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
