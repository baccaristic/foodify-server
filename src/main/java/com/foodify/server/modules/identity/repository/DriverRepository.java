package com.foodify.server.modules.identity.repository;

import com.foodify.server.modules.identity.domain.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    long countByAvailableTrue();
    
    @Query("""
            SELECT d FROM Driver d
            WHERE (:query IS NULL OR 
                   LOWER(CONCAT(d.id, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR 
                   LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR 
                   d.phone LIKE CONCAT('%', :query, '%'))
            AND (:isOnline IS NULL OR d.available = :isOnline)
            AND (:paymentStatus IS NULL OR 
                 (:paymentStatus = true AND d.outstandingDailyFees = 0) OR
                 (:paymentStatus = false AND d.outstandingDailyFees > 0))
            """)
    Page<Driver> findDriversWithFilters(
            @Param("query") String query,
            @Param("paymentStatus") Boolean paymentStatus,
            @Param("isOnline") Boolean isOnline,
            Pageable pageable
    );
    
    @Query("SELECT d FROM Driver d LEFT JOIN FETCH d.deliveries WHERE d.id = :id")
    Optional<Driver> findByIdWithDeliveries(@Param("id") Long id);
}
