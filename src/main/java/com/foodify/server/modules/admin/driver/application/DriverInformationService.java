package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.DriverDetailsDto;
import com.foodify.server.modules.admin.driver.dto.DriverListItemDto;
import com.foodify.server.modules.admin.driver.dto.DriverStatisticsDto;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDriverRepository;
import com.foodify.server.modules.admin.driver.repository.DriverSpecifications;
import com.foodify.server.modules.admin.driver.helpers.DriverFinancialCalculator;
import com.foodify.server.modules.common.util.DateRangeValidator;
import com.foodify.server.modules.identity.domain.Driver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverInformationService {

    private final AdminDriverRepository adminDriverRepository;
    private final AdminDeliveryRepository adminDeliveryRepository;
    private final DriverFinancialCalculator financialCalculator;

    @Transactional(readOnly = true)
    public Page<DriverListItemDto> getDriverList(String query, Boolean paid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Driver> driversPage = adminDriverRepository.findAll(
                DriverSpecifications.withFilters(query, paid),
                pageable
        );

        return driversPage.map(driver -> DriverListItemDto.builder()
                .id(driver.getId())
                .name(driver.getName())
                .phone(driver.getPhone())
                .paid(financialCalculator.isDriverPaid(driver))
                .build());
    }

    @Transactional(readOnly = true)
    public DriverDetailsDto getDriverById(Long driverId) {
        return adminDriverRepository.findDriverDetailsById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with id: " + driverId));
    }

    @Transactional(readOnly = true)
    public DriverStatisticsDto getDriverStatistics(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDriverExists(driverId);
        DateRangeValidator.validate(startDate, endDate);

        Double rating = adminDeliveryRepository.getDriverRating(driverId, startDate, endDate);
        Double onTimePercentage = adminDeliveryRepository.getDriverOnTimePercentage(driverId, startDate, endDate);
        Double avgDeliveryTime = adminDeliveryRepository.getDriverAverageDeliveryTime(driverId, startDate, endDate);
        Long totalDeliveries = adminDeliveryRepository.getDriverTotalDeliveries(driverId, startDate, endDate);

        return new DriverStatisticsDto(rating, onTimePercentage, avgDeliveryTime, totalDeliveries);
    }

    public void validateDriverExists(Long driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
        if (!adminDriverRepository.existsById(driverId)) {
            throw new EntityNotFoundException("Driver not found with id: " + driverId);
        }
    }
}
