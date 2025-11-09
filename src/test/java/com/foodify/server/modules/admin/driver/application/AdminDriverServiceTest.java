package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto;
import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.admin.driver.dto.DriverListItemDto;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRatingRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDriverRepository;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.delivery.domain.DriverDepositStatus;
import com.foodify.server.modules.identity.domain.Driver;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDriverServiceTest {

    @Mock
    private AdminDriverRepository adminDriverRepository;

    @Mock
    private AdminDeliveryRepository adminDeliveryRepository;

    @Mock
    private AdminDeliveryRatingRepository adminDeliveryRatingRepository;

    private AdminDriverService adminDriverService;

    private Driver testDriver;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        adminDriverService = new AdminDriverService(
                adminDriverRepository,
                adminDeliveryRepository,
                adminDeliveryRatingRepository
        );

        testDriver = new Driver();
        testDriver.setId(1L);
        testDriver.setName("Test Driver");
        testDriver.setPhone("1234567890");

        startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
    }

    @Test
    void getDriverList_shouldReturnPagedDrivers() {
        // Given
        String query = "test";
        DriverDepositStatus depositStatus = DriverDepositStatus.CONFIRMED;
        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        List<Driver> drivers = Arrays.asList(testDriver);
        Page<Driver> driversPage = new PageImpl<>(drivers, pageable, 1);

        when(adminDriverRepository.findWithFilters(query, depositStatus, pageable))
                .thenReturn(driversPage);

        // When
        Page<DriverListItemDto> result = adminDriverService.getDriverList(query, depositStatus, page, size);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testDriver.getId(), result.getContent().get(0).getId());
        assertEquals(testDriver.getName(), result.getContent().get(0).getName());
        verify(adminDriverRepository).findWithFilters(query, depositStatus, pageable);
    }

    @Test
    void getDriverRating_shouldReturnAverageRating() {
        // Given
        Long driverId = 1L;
        Double expectedRating = 4.5;

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);
        when(adminDeliveryRepository.getDriverRating(driverId, startDate, endDate))
                .thenReturn(expectedRating);

        // When
        Double result = adminDriverService.getDriverRating(driverId, startDate, endDate);

        // Then
        assertEquals(expectedRating, result);
        verify(adminDriverRepository).existsById(driverId);
        verify(adminDeliveryRepository).getDriverRating(driverId, startDate, endDate);
    }

    @Test
    void getDriverRating_shouldThrowExceptionWhenDriverNotFound() {
        // Given
        Long driverId = 999L;
        when(adminDriverRepository.existsById(driverId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> 
                adminDriverService.getDriverRating(driverId, startDate, endDate));
        verify(adminDriverRepository).existsById(driverId);
        verify(adminDeliveryRepository, never()).getDriverRating(any(), any(), any());
    }

    @Test
    void getDriverRating_shouldThrowExceptionWhenDriverIdIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
                adminDriverService.getDriverRating(null, startDate, endDate));
    }

    @Test
    void getDriverOnTimePercentage_shouldReturnPercentage() {
        // Given
        Long driverId = 1L;
        Double expectedPercentage = 85.5;

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);
        when(adminDeliveryRepository.getDriverOnTimePercentage(driverId, startDate, endDate))
                .thenReturn(expectedPercentage);

        // When
        Double result = adminDriverService.getDriverOnTimePercentage(driverId, startDate, endDate);

        // Then
        assertEquals(expectedPercentage, result);
        verify(adminDeliveryRepository).getDriverOnTimePercentage(driverId, startDate, endDate);
    }

    @Test
    void getDriverAverageDeliveryTime_shouldReturnAverageTime() {
        // Given
        Long driverId = 1L;
        Double expectedTime = 25.5; // minutes

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);
        when(adminDeliveryRepository.getDriverAverageDeliveryTime(driverId, startDate, endDate))
                .thenReturn(expectedTime);

        // When
        Double result = adminDriverService.getDriverAverageDeliveryTime(driverId, startDate, endDate);

        // Then
        assertEquals(expectedTime, result);
        verify(adminDeliveryRepository).getDriverAverageDeliveryTime(driverId, startDate, endDate);
    }

    @Test
    void getDriverDailyRatings_shouldReturnDailyRatings() {
        // Given
        Long driverId = 1L;
        List<DailyRatingDto> expectedRatings = Arrays.asList(
                new DailyRatingDto(LocalDate.of(2024, 1, 1), 4.5),
                new DailyRatingDto(LocalDate.of(2024, 1, 2), 4.8)
        );

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);
        when(adminDeliveryRepository.getDriverAverageRatingByDay(driverId, startDate, endDate))
                .thenReturn(expectedRatings);

        // When
        List<DailyRatingDto> result = adminDriverService.getDriverDailyRatings(driverId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(4.5, result.get(0).getAvgRating());
        verify(adminDeliveryRepository).getDriverAverageRatingByDay(driverId, startDate, endDate);
    }

    @Test
    void getDriverDailyOnTimePercentage_shouldReturnDailyData() {
        // Given
        Long driverId = 1L;
        List<DailyOnTimePercentageDto> expectedData = Arrays.asList(
                new DailyOnTimePercentageDto(LocalDate.of(2024, 1, 1), 90.0),
                new DailyOnTimePercentageDto(LocalDate.of(2024, 1, 2), 85.0)
        );

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);
        when(adminDeliveryRepository.getDriverOnTimePercentageByDay(driverId, startDate, endDate))
                .thenReturn(expectedData);

        // When
        List<DailyOnTimePercentageDto> result = adminDriverService.getDriverDailyOnTimePercentage(
                driverId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(90.0, result.get(0).getOnTimePercentage());
        verify(adminDeliveryRepository).getDriverOnTimePercentageByDay(driverId, startDate, endDate);
    }

    @Test
    void getDriverRatingsWithComments_shouldReturnPagedRatings() {
        // Given
        Long driverId = 1L;
        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        DeliveryRating rating = new DeliveryRating();
        rating.setDeliveryId(1L);
        rating.setComments("Great service!");

        List<DeliveryRating> ratings = Arrays.asList(rating);
        Page<DeliveryRating> ratingsPage = new PageImpl<>(ratings, pageable, 1);

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);
        when(adminDeliveryRatingRepository.findRatingsWithCommentsByDriverId(
                driverId, startDate, endDate, pageable))
                .thenReturn(ratingsPage);

        // When
        Page<DeliveryRating> result = adminDriverService.getDriverRatingsWithComments(
                driverId, startDate, endDate, page, size);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Great service!", result.getContent().get(0).getComments());
        verify(adminDeliveryRatingRepository).findRatingsWithCommentsByDriverId(
                driverId, startDate, endDate, pageable);
    }

    @Test
    void getDriverRatingDistribution_shouldReturnDistribution() {
        // Given
        Long driverId = 1L;
        List<Long> expectedDistribution = Arrays.asList(5L, 10L, 15L, 30L, 40L); // 1-5 stars

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);
        when(adminDeliveryRatingRepository.getDriverRatingDistribution(driverId, startDate, endDate))
                .thenReturn(expectedDistribution);

        // When
        List<Long> result = adminDriverService.getDriverRatingDistribution(driverId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(40L, result.get(4)); // 5-star count
        verify(adminDeliveryRatingRepository).getDriverRatingDistribution(driverId, startDate, endDate);
    }

    @Test
    void getDriverById_shouldReturnDriver() {
        // Given
        Long driverId = 1L;
        when(adminDriverRepository.findById(driverId)).thenReturn(Optional.of(testDriver));

        // When
        Driver result = adminDriverService.getDriverById(driverId);

        // Then
        assertNotNull(result);
        assertEquals(testDriver.getId(), result.getId());
        assertEquals(testDriver.getName(), result.getName());
        verify(adminDriverRepository).findById(driverId);
    }

    @Test
    void getDriverById_shouldThrowExceptionWhenNotFound() {
        // Given
        Long driverId = 999L;
        when(adminDriverRepository.findById(driverId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> 
                adminDriverService.getDriverById(driverId));
        verify(adminDriverRepository).findById(driverId);
    }

    @Test
    void validateDateRange_shouldThrowExceptionWhenStartDateAfterEndDate() {
        // Given
        Long driverId = 1L;
        LocalDateTime invalidStartDate = LocalDateTime.of(2024, 12, 31, 0, 0);
        LocalDateTime invalidEndDate = LocalDateTime.of(2024, 1, 1, 0, 0);

        when(adminDriverRepository.existsById(driverId)).thenReturn(true);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
                adminDriverService.getDriverRating(driverId, invalidStartDate, invalidEndDate));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Start date must be before or equal to end date"));
    }
}
