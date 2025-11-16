package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.DailyEarningDto;
import com.foodify.server.modules.admin.driver.dto.DailyOnTimePercentageDto;
import com.foodify.server.modules.admin.driver.dto.DriverEarningsByPaymentMethodDto;
import com.foodify.server.modules.admin.driver.repository.AdminDeliveryRepository;
import com.foodify.server.modules.admin.driver.repository.AdminDriverDepositRepository;
import com.foodify.server.modules.common.util.DateRangeValidator;
import lombok.RequiredArgsConstructor;
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
public class DriverPerformanceService {

    private static final double DEFAULT_DRIVER_COMMISSION_RATE = 0.12;

    private final AdminDeliveryRepository adminDeliveryRepository;
    private final AdminDriverDepositRepository adminDriverDepositRepository;
    private final DriverQueryService driverQueryService;

    @Transactional(readOnly = true)
    public Double getDriverOnTimePercentage(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        driverQueryService.validateDriverExists(driverId);
        DateRangeValidator.validate(startDate, endDate);
        return adminDeliveryRepository.getDriverOnTimePercentage(driverId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Double getDriverAverageDeliveryTime(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        driverQueryService.validateDriverExists(driverId);
        DateRangeValidator.validate(startDate, endDate);
        return adminDeliveryRepository.getDriverAverageDeliveryTime(driverId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<DailyOnTimePercentageDto> getDriverDailyOnTimePercentage(Long driverId, LocalDateTime startDate, 
                                                                         LocalDateTime endDate) {
        driverQueryService.validateDriverExists(driverId);
        DateRangeValidator.validate(startDate, endDate);
        
        List<DailyOnTimePercentageDto> actualPercentages = adminDeliveryRepository
                .getDriverOnTimePercentageByDay(driverId, startDate, endDate);

        return fillMissingDates(
                actualPercentages,
                startDate,
                endDate,
                DailyOnTimePercentageDto::getDate,
                date -> new DailyOnTimePercentageDto(date, 0.0)
        );
    }

    @Transactional(readOnly = true)
    public List<DailyEarningDto> getDriverDailyEarning(Long driverId, LocalDateTime startDate, LocalDateTime endDate) {
        driverQueryService.validateDriverExists(driverId);
        DateRangeValidator.validate(startDate, endDate);
        
        List<DailyEarningDto> actualEarnings = adminDriverDepositRepository
                .getDriverEarningByDay(driverId, startDate, endDate);

        return fillMissingDates(
                actualEarnings,
                startDate,
                endDate,
                DailyEarningDto::getDate,
                date -> new DailyEarningDto(date, 0.0)
        );
    }

    @Transactional(readOnly = true)
    public DriverEarningsByPaymentMethodDto getDriverEarningsByPaymentMethod(Long driverId, LocalDate date) {
        driverQueryService.validateDriverExists(driverId);

        Double cashEarnings = adminDeliveryRepository.getEarningByDriverIdAndDeliveredAtDateAndPaymentMethod(
                driverId, date, "CASH");
        Double cardEarnings = adminDeliveryRepository.getEarningByDriverIdAndDeliveredAtDateAndPaymentMethod(
                driverId, date, "CARD");

        Double cash = cashEarnings != null ? cashEarnings : 0.0;
        Double card = cardEarnings != null ? cardEarnings : 0.0;
        Double total = cash + card;
        Double totalCommission = total * DEFAULT_DRIVER_COMMISSION_RATE;

        return DriverEarningsByPaymentMethodDto.builder()
                .totalCommission(totalCommission)
                .cashEarnings(cash)
                .cardEarnings(card)
                .totalEarnings(total)
                .build();
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
