package com.foodify.server.modules.admin.driver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodify.server.modules.admin.driver.dto.DriverDetailsDto;
import com.foodify.server.modules.identity.domain.Driver;

public interface AdminDriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {
    
    @Query("""
        SELECT new com.foodify.server.modules.admin.driver.dto.DriverDetailsDto(
            d.id,
            d.email,
            d.name,
            d.available,
            d.phone,
            d.cashOnHand,
            d.unpaidEarnings,
            d.outstandingDailyFees,
            d.outstandingDailyFeeDays,
            d.lastDailyFeeDate
        )
        FROM Driver d
        WHERE d.id = :id
    """)
    Optional<DriverDetailsDto> findDriverDetailsById(@Param("id") Long id);
    
}
