package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.DailyRatingDto;
import com.foodify.server.modules.admin.driver.dto.DriverRatingCommentDto;
import com.foodify.server.modules.admin.driver.dto.RatingDistributionDto;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRatingRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRepository;
import com.foodify.server.modules.common.util.DateRangeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverRatingService {

    private final AdminDeliveryRepository adminDeliveryRepository;
    private final AdminDeliveryRatingRepository adminDeliveryRatingRepository;
    private final DriverQueryService driverQueryService;

    @Transactional(readOnly = true)
    public Double getDriverRating(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        driverQueryService.validateDriverExists(driverId);
        DateRangeValidator.validate(startDate, endDate);
        return adminDeliveryRepository.getDriverRating(driverId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<DailyRatingDto> getDriverDailyRatings(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        driverQueryService.validateDriverExists(driverId);
        DateRangeValidator.validate(startDate, endDate);
        
        List<DailyRatingDto> actualRatings = adminDeliveryRatingRepository
                .getDriverAverageRatingByDay(driverId, startDate, endDate);

        return fillMissingDates(
                actualRatings,
                startDate,
                endDate,
                DailyRatingDto::getDate,
                date -> new DailyRatingDto(date, 0.0)
        );
    }

    @Transactional(readOnly = true)
    public Page<DriverRatingCommentDto> getDriverRatingsWithComments(Long driverId, LocalDateTime startDate,
                                                                     LocalDateTime endDate, int page, int size) {
        driverQueryService.validateDriverExists(driverId);
        Pageable pageable = PageRequest.of(page, size);
        return adminDeliveryRatingRepository.findRatingsWithCommentsByDriverId(
                driverId, startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public List<RatingDistributionDto> getDriverRatingDistribution(Long driverId, LocalDateTime startDate, 
                                                                   LocalDateTime endDate) {
        driverQueryService.validateDriverExists(driverId);
        return adminDeliveryRatingRepository.getDriverRatingDistribution(driverId, startDate, endDate);
    }

    private <T> List<T> fillMissingDates(List<T> data, LocalDateTime startDate, LocalDateTime endDate,
                                         Function<T, LocalDate> dateExtractor,
                                         Function<LocalDate, T> creator) {
        Map<LocalDate, T> dataMap = data.stream()
                .collect(Collectors.toMap(dateExtractor, item -> item));

        return startDate.toLocalDate().datesUntil(endDate.toLocalDate().plusDays(1))
                .map(date -> dataMap.getOrDefault(date, creator.apply(date)))
                .toList();
    }
}
