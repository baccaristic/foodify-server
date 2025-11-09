package com.foodify.server.modules.admin.driver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.identity.domain.Driver;

public interface AdminDriverRepository extends JpaRepository<Driver, Long> {

    @Query(value = """
            SELECT d.*
            FROM driver d
            WHERE d.enabled = true
            AND (:query IS NULL OR
                 LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
                 CAST(d.id AS VARCHAR) LIKE CONCAT('%', :query, '%') OR
                 d.phone LIKE CONCAT('%', :query, '%'))
            AND (:depositStatus IS NULL OR 
                 CAST(:depositStatus AS VARCHAR) = (SELECT CAST(dep.status AS VARCHAR)
                                    FROM driver_deposit dep 
                                    WHERE dep.driver_id = d.id 
                                    ORDER BY dep.created_at DESC 
                                    LIMIT 1))
            ORDER BY d.id DESC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT d.id)
            FROM driver d
            WHERE d.enabled = true
            AND (:query IS NULL OR
                 LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
                 CAST(d.id AS VARCHAR) LIKE CONCAT('%', :query, '%') OR
                 d.phone LIKE CONCAT('%', :query, '%'))
            AND (:depositStatus IS NULL OR 
                 CAST(:depositStatus AS VARCHAR) = (SELECT CAST(dep.status AS VARCHAR)
                                    FROM driver_deposit dep 
                                    WHERE dep.driver_id = d.id 
                                    ORDER BY dep.created_at DESC 
                                    LIMIT 1))
            """,
            nativeQuery = true)
    Page<Driver> findWithFilters(
            @Param("query") String query,
            @Param("depositStatus") DriverDepositStatus depositStatus,
            Pageable pageable);
}
