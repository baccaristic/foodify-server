package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.dto.analytics.*;
import com.foodify.server.modules.restaurants.repository.RestaurantRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final RestaurantRatingRepository ratingRepository;

    public GeneralOverviewResponse getGeneralOverview(Long restaurantId, AnalyticsPeriod period) {
        LocalDateTime[] currentPeriod = getPeriodBounds(period, 0);
        LocalDateTime[] previousPeriod = getPeriodBounds(period, 1);

        // Revenue
        BigDecimal currentRevenue = orderRepository.getTotalRevenueForPeriod(
                restaurantId, currentPeriod[0], currentPeriod[1]);
        BigDecimal previousRevenue = orderRepository.getTotalRevenueForPeriod(
                restaurantId, previousPeriod[0], previousPeriod[1]);
        GeneralOverviewResponse.RevenueMetric revenueMetric = calculateRevenueMetric(
                currentRevenue, previousRevenue);

        // Orders completed
        Long currentOrders = orderRepository.countDeliveredOrdersForPeriod(
                restaurantId, currentPeriod[0], currentPeriod[1]);
        Long previousOrders = orderRepository.countDeliveredOrdersForPeriod(
                restaurantId, previousPeriod[0], previousPeriod[1]);
        GeneralOverviewResponse.OrdersMetric ordersMetric = calculateOrdersMetric(
                currentOrders, previousOrders);

        // Average preparation time
        BigDecimal currentPrepTime = orderRepository.getAveragePreparationTimeForPeriod(
                restaurantId, currentPeriod[0], currentPeriod[1]);
        BigDecimal previousPrepTime = orderRepository.getAveragePreparationTimeForPeriod(
                restaurantId, previousPeriod[0], previousPeriod[1]);
        GeneralOverviewResponse.PreparationTimeMetric prepTimeMetric = calculatePrepTimeMetric(
                currentPrepTime, previousPrepTime);

        // Customer rating
        RestaurantRatingRepository.RatingAggregate currentRatings = 
                ratingRepository.findAggregateByRestaurantIdAndPeriod(
                        restaurantId, currentPeriod[0], currentPeriod[1]);
        RestaurantRatingRepository.RatingAggregate previousRatings = 
                ratingRepository.findAggregateByRestaurantIdAndPeriod(
                        restaurantId, previousPeriod[0], previousPeriod[1]);
        GeneralOverviewResponse.RatingMetric ratingMetric = calculateRatingMetric(
                currentRatings, previousRatings);

        return new GeneralOverviewResponse(
                period,
                revenueMetric,
                ordersMetric,
                prepTimeMetric,
                ratingMetric
        );
    }

    public SalesTrendResponse getSalesTrend(Long restaurantId, AnalyticsPeriod period) {
        LocalDateTime[] bounds = getPeriodBounds(period, 0);
        
        List<OrderRepository.SalesTrendProjection> projections = 
                orderRepository.getSalesTrendForPeriod(restaurantId, bounds[0], bounds[1]);
        
        List<SalesTrendResponse.DataPoint> dataPoints = projections.stream()
                .map(p -> new SalesTrendResponse.DataPoint(
                        p.getDate(),
                        p.getRevenue() != null ? p.getRevenue() : BigDecimal.ZERO,
                        p.getOrderCount() != null ? p.getOrderCount() : 0L
                ))
                .collect(Collectors.toList());
        
        // Fill in missing dates with zero values
        dataPoints = fillMissingDates(dataPoints, bounds[0].toLocalDate(), bounds[1].toLocalDate());
        
        return new SalesTrendResponse(period, dataPoints);
    }

    public TopDishesResponse getTopDishes(Long restaurantId, AnalyticsPeriod period) {
        LocalDateTime[] bounds = getPeriodBounds(period, 0);
        
        List<OrderRepository.TopDishProjection> projections = 
                orderRepository.getTopDishesForPeriod(
                        restaurantId, bounds[0], bounds[1], PageRequest.of(0, 3));
        
        List<TopDishesResponse.DishData> topDishes = projections.stream()
                .map(p -> new TopDishesResponse.DishData(
                        p.getMenuItemId(),
                        p.getMenuItemName(),
                        p.getOrderCount() != null ? p.getOrderCount() : 0L,
                        p.getQuantitySold() != null ? p.getQuantitySold() : 0L
                ))
                .collect(Collectors.toList());
        
        return new TopDishesResponse(period, topDishes);
    }

    private LocalDateTime[] getPeriodBounds(AnalyticsPeriod period, int periodsBack) {
        LocalDate today = LocalDate.now();
        LocalDateTime start;
        LocalDateTime end;

        switch (period) {
            case TODAY:
                LocalDate targetDay = today.minusDays(periodsBack);
                start = targetDay.atStartOfDay();
                end = targetDay.plusDays(1).atStartOfDay();
                break;
            case WEEK:
                LocalDate startOfWeek = today.minusWeeks(periodsBack)
                        .with(java.time.DayOfWeek.MONDAY);
                start = startOfWeek.atStartOfDay();
                end = startOfWeek.plusWeeks(1).atStartOfDay();
                break;
            case MONTH:
                LocalDate startOfMonth = today.minusMonths(periodsBack)
                        .withDayOfMonth(1);
                start = startOfMonth.atStartOfDay();
                end = startOfMonth.plusMonths(1).atStartOfDay();
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        return new LocalDateTime[]{start, end};
    }

    private GeneralOverviewResponse.RevenueMetric calculateRevenueMetric(
            BigDecimal current, BigDecimal previous) {
        if (current == null) current = BigDecimal.ZERO;
        if (previous == null) previous = BigDecimal.ZERO;

        BigDecimal change = current.subtract(previous);
        BigDecimal percentageChange = previous.compareTo(BigDecimal.ZERO) != 0
                ? change.divide(previous, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : (current.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);

        return new GeneralOverviewResponse.RevenueMetric(
                current.setScale(2, RoundingMode.HALF_UP),
                previous.setScale(2, RoundingMode.HALF_UP),
                change.setScale(2, RoundingMode.HALF_UP),
                percentageChange
        );
    }

    private GeneralOverviewResponse.OrdersMetric calculateOrdersMetric(
            Long current, Long previous) {
        if (current == null) current = 0L;
        if (previous == null) previous = 0L;

        Long change = current - previous;
        BigDecimal percentageChange = previous != 0
                ? BigDecimal.valueOf(change)
                        .divide(BigDecimal.valueOf(previous), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : (current > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);

        return new GeneralOverviewResponse.OrdersMetric(
                current,
                previous,
                change,
                percentageChange
        );
    }

    private GeneralOverviewResponse.PreparationTimeMetric calculatePrepTimeMetric(
            BigDecimal current, BigDecimal previous) {
        if (current == null) current = BigDecimal.ZERO;
        if (previous == null) previous = BigDecimal.ZERO;

        BigDecimal change = current.subtract(previous);
        BigDecimal percentageChange = previous.compareTo(BigDecimal.ZERO) != 0
                ? change.divide(previous, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : (current.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);

        return new GeneralOverviewResponse.PreparationTimeMetric(
                current.setScale(2, RoundingMode.HALF_UP),
                previous.setScale(2, RoundingMode.HALF_UP),
                change.setScale(2, RoundingMode.HALF_UP),
                percentageChange
        );
    }

    private GeneralOverviewResponse.RatingMetric calculateRatingMetric(
            RestaurantRatingRepository.RatingAggregate current,
            RestaurantRatingRepository.RatingAggregate previous) {
        
        BigDecimal currentStars = calculateStarsFromAggregate(current);
        BigDecimal previousStars = calculateStarsFromAggregate(previous);
        
        BigDecimal change = currentStars.subtract(previousStars);
        BigDecimal percentageChange = previousStars.compareTo(BigDecimal.ZERO) != 0
                ? change.divide(previousStars, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                : (currentStars.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);

        return new GeneralOverviewResponse.RatingMetric(
                currentStars,
                previousStars,
                change,
                percentageChange
        );
    }

    private BigDecimal calculateStarsFromAggregate(RestaurantRatingRepository.RatingAggregate aggregate) {
        if (aggregate == null || aggregate.totalCount() == 0) {
            return BigDecimal.ZERO;
        }
        
        // Convert thumbs up/down to 5-star scale
        double percentage = (double) aggregate.thumbsUpCount() / aggregate.totalCount();
        return BigDecimal.valueOf(percentage * 5).setScale(1, RoundingMode.HALF_UP);
    }

    private List<SalesTrendResponse.DataPoint> fillMissingDates(
            List<SalesTrendResponse.DataPoint> dataPoints,
            LocalDate startDate,
            LocalDate endDate) {
        
        if (dataPoints.isEmpty()) {
            // If no data, create empty data points for the period
            List<SalesTrendResponse.DataPoint> result = new ArrayList<>();
            LocalDate current = startDate;
            while (!current.isAfter(endDate.minusDays(1))) {
                result.add(new SalesTrendResponse.DataPoint(current, BigDecimal.ZERO, 0L));
                current = current.plusDays(1);
            }
            return result;
        }
        
        // Create a map for quick lookup
        java.util.Map<LocalDate, SalesTrendResponse.DataPoint> dataMap = dataPoints.stream()
                .collect(Collectors.toMap(
                        SalesTrendResponse.DataPoint::date,
                        dp -> dp
                ));
        
        // Fill in missing dates
        List<SalesTrendResponse.DataPoint> result = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate.minusDays(1))) {
            result.add(dataMap.getOrDefault(
                    current,
                    new SalesTrendResponse.DataPoint(current, BigDecimal.ZERO, 0L)
            ));
            current = current.plusDays(1);
        }
        
        return result;
    }
}
