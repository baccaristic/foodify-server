package com.foodify.server.modules.admin.driver.repository;

import com.foodify.server.modules.delivery.domain.DriverDeposit;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.identity.domain.Driver;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecifications {

    public static Specification<Driver> withFilters(String query, DriverDepositStatus depositStatus) {
        return (root, criteriaQuery, cb) -> {
            Predicate predicate = cb.conjunction();

            predicate = cb.and(predicate, cb.isTrue(root.get("enabled")));

            if (query != null && !query.trim().isEmpty()) {
                String likePattern = "%" + query.toLowerCase() + "%";
                Predicate queryPredicate = cb.or(
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(root.get("id").as(String.class), "%" + query + "%"),
                        cb.like(root.get("phone"), "%" + query + "%")
                );
                predicate = cb.and(predicate, queryPredicate);
            }

            if (depositStatus != null) {
                Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
                Root<DriverDeposit> depositRoot = subquery.from(DriverDeposit.class);

                subquery.select(cb.max(depositRoot.get("createdAt")))
                        .where(cb.equal(depositRoot.get("driver"), root));

                Join<Driver, DriverDeposit> depositJoin = root.join("deposits", JoinType.LEFT);

                Predicate depositPredicate = cb.and(
                        cb.equal(depositJoin.get("status"), depositStatus),
                        cb.equal(depositJoin.get("createdAt"), subquery)
                );

                predicate = cb.and(predicate, depositPredicate);
            }

            return predicate;
        };
    }
}