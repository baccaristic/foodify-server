package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderItemRepository;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuCategory;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.domain.RestaurantCategory;
import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import com.foodify.server.modules.restaurants.dto.*;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRatingRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRestaurantManagementService {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRatingRepository restaurantRatingRepository;

    @Transactional(readOnly = true)
    public Page<AdminRestaurantListItemDto> getRestaurantsWithFilters(
            String query,
            String cuisine,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findRestaurantsWithFilters(query, cuisine, pageable);

        return restaurants.map(restaurant -> {
            boolean isOpen = isRestaurantOpen(restaurant);
            var cuisines = restaurant.getCategories().stream()
                    .map(RestaurantCategory::name)
                    .collect(Collectors.toSet());

            return AdminRestaurantListItemDto.builder()
                    .id(restaurant.getId())
                    .name(restaurant.getName())
                    .address(restaurant.getAddress())
                    .cuisines(cuisines)
                    .isOpen(isOpen)
                    .overallRating(restaurant.getRating())
                    .imageUrl(restaurant.getImageUrl())
                    .build();
        });
    }

    private boolean isRestaurantOpen(Restaurant restaurant) {
        if (restaurant.getOperatingHours() == null || restaurant.getOperatingHours().isEmpty()) {
            return false;
        }

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        LocalTime now = LocalTime.now();

        return restaurant.getOperatingHours().stream()
                .filter(hours -> hours.getDayOfWeek() == today)
                .anyMatch(hours -> hours.isOpen() && 
                         hours.getOpensAt() != null && 
                         hours.getClosesAt() != null &&
                         !now.isBefore(hours.getOpensAt()) && 
                         !now.isAfter(hours.getClosesAt()));
    }

    @Transactional(readOnly = true)
    public RestaurantRevenueDto getTodayRevenue(Long restaurantId) {
        validateRestaurant(restaurantId);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        LocalDateTime yesterdayEnd = todayEnd.minusDays(1);

        BigDecimal todayRevenue = orderRepository.getTotalRevenueByRestaurantAndDateRange(
                restaurantId, todayStart, todayEnd);
        BigDecimal yesterdayRevenue = orderRepository.getTotalRevenueByRestaurantAndDateRange(
                restaurantId, yesterdayStart, yesterdayEnd);

        double percentageChange = calculatePercentageChange(todayRevenue, yesterdayRevenue);

        return RestaurantRevenueDto.builder()
                .todayRevenue(todayRevenue)
                .yesterdayRevenue(yesterdayRevenue)
                .percentageChange(percentageChange)
                .build();
    }

    @Transactional(readOnly = true)
    public RestaurantTotalOrdersDto getTotalOrders(Long restaurantId) {
        validateRestaurant(restaurantId);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        LocalDateTime yesterdayEnd = todayEnd.minusDays(1);

        long todayOrders = orderRepository.countDeliveredOrdersByRestaurantAndDateRange(
                restaurantId, todayStart, todayEnd);
        long yesterdayOrders = orderRepository.countDeliveredOrdersByRestaurantAndDateRange(
                restaurantId, yesterdayStart, yesterdayEnd);

        double percentageChange = calculatePercentageChange(
                BigDecimal.valueOf(todayOrders), 
                BigDecimal.valueOf(yesterdayOrders)
        );

        return RestaurantTotalOrdersDto.builder()
                .todayOrders(todayOrders)
                .yesterdayOrders(yesterdayOrders)
                .percentageChange(percentageChange)
                .build();
    }

    @Transactional(readOnly = true)
    public RestaurantAvgOrderValueDto getAvgOrderValue(Long restaurantId) {
        validateRestaurant(restaurantId);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        LocalDateTime yesterdayEnd = todayEnd.minusDays(1);

        Double todayAvg = orderRepository.getAverageOrderValueByRestaurantAndDateRange(
                restaurantId, todayStart, todayEnd);
        Double yesterdayAvg = orderRepository.getAverageOrderValueByRestaurantAndDateRange(
                restaurantId, yesterdayStart, yesterdayEnd);

        BigDecimal todayAvgOrderValue = BigDecimal.valueOf(todayAvg != null ? todayAvg : 0.0);
        BigDecimal yesterdayAvgOrderValue = BigDecimal.valueOf(yesterdayAvg != null ? yesterdayAvg : 0.0);

        double percentageChange = calculatePercentageChange(todayAvgOrderValue, yesterdayAvgOrderValue);

        return RestaurantAvgOrderValueDto.builder()
                .todayAvgOrderValue(todayAvgOrderValue)
                .yesterdayAvgOrderValue(yesterdayAvgOrderValue)
                .percentageChange(percentageChange)
                .build();
    }

    @Transactional(readOnly = true)
    public RestaurantAvgRatingDto getAvgRating(Long restaurantId) {
        validateRestaurant(restaurantId);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        LocalDateTime yesterdayEnd = todayEnd.minusDays(1);

        var todayAggregate = restaurantRatingRepository.findAggregateByRestaurantIdAndDateRange(
                restaurantId, todayStart, todayEnd);
        var yesterdayAggregate = restaurantRatingRepository.findAggregateByRestaurantIdAndDateRange(
                restaurantId, yesterdayStart, yesterdayEnd);

        double todayAvgRating = calculateRatingPercentage(todayAggregate);
        double yesterdayAvgRating = calculateRatingPercentage(yesterdayAggregate);

        double percentageChange = calculatePercentageChange(
                BigDecimal.valueOf(todayAvgRating),
                BigDecimal.valueOf(yesterdayAvgRating)
        );

        return RestaurantAvgRatingDto.builder()
                .todayAvgRating(todayAvgRating)
                .yesterdayAvgRating(yesterdayAvgRating)
                .percentageChange(percentageChange)
                .todayRatingCount(todayAggregate.totalCount())
                .yesterdayRatingCount(yesterdayAggregate.totalCount())
                .build();
    }

    private double calculateRatingPercentage(RestaurantRatingRepository.RatingAggregate aggregate) {
        if (aggregate.totalCount() == 0) {
            return 0.0;
        }
        return (aggregate.thumbsUpCount() * 100.0) / aggregate.totalCount();
    }

    @Transactional(readOnly = true)
    public List<DailyRevenueDto> getRevenuePerDay(Long restaurantId, LocalDate startDate, LocalDate endDate) {
        validateRestaurant(restaurantId);

        List<DailyRevenueDto> dailyRevenues = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.atTime(LocalTime.MAX);

            BigDecimal revenue = orderRepository.getTotalRevenueByRestaurantAndDateRange(
                    restaurantId, dayStart, dayEnd);
            long orderCount = orderRepository.countDeliveredOrdersByRestaurantAndDateRange(
                    restaurantId, dayStart, dayEnd);

            dailyRevenues.add(DailyRevenueDto.builder()
                    .date(currentDate)
                    .revenue(revenue)
                    .orderCount(orderCount)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return dailyRevenues;
    }

    @Transactional(readOnly = true)
    public List<TopSellingItemDto> getTopSellingItems(Long restaurantId, int limit) {
        validateRestaurant(restaurantId);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<Object[]> results = orderItemRepository.findTopSellingItemsByRestaurantAndDateRange(
                restaurantId, todayStart, todayEnd, limit);

        return results.stream()
                .limit(limit)
                .map(row -> {
                    Long itemId = (Long) row[0];
                    String itemName = (String) row[1];
                    Long orderCount = (Long) row[2];
                    Double price = (Double) row[3];

                    // Get menu item for image
                    MenuItem menuItem = menuItemRepository.findById(itemId).orElse(null);
                    String imageUrl = menuItem != null && !menuItem.getImageUrls().isEmpty() 
                            ? menuItem.getImageUrls().get(0) 
                            : null;

                    return TopSellingItemDto.builder()
                            .itemId(itemId)
                            .itemName(itemName)
                            .orderCount(orderCount)
                            .price(price)
                            .imageUrl(imageUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersHistory(Long restaurantId, LocalDate date, OrderStatus status, int page, int size) {
        validateRestaurant(restaurantId);

        LocalDateTime dateTime = date.atStartOfDay();
        Pageable pageable = PageRequest.of(page, size);

        return orderRepository.findByRestaurantIdAndDateAndStatus(restaurantId, dateTime, status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MenuItemListDto> getMenuItems(Long restaurantId, String category, int page, int size) {
        validateRestaurant(restaurantId);

        Pageable pageable = PageRequest.of(page, size);
        Page<MenuItem> menuItems = menuItemRepository.findByRestaurantIdWithFilters(
                restaurantId, category, pageable);

        return menuItems.map(item -> {
            var categories = item.getCategories().stream()
                    .map(MenuCategory::getName)
                    .collect(Collectors.toSet());

            String imageUrl = item.getImageUrls() != null && !item.getImageUrls().isEmpty() 
                    ? item.getImageUrls().get(0) 
                    : null;

            return MenuItemListDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .price(item.getPrice())
                    .available(item.isAvailable())
                    .categories(categories)
                    .imageUrl(imageUrl)
                    .hasPromotion(item.getPromotionActive() != null && item.getPromotionActive())
                    .promotionPrice(item.getPromotionPrice())
                    .build();
        });
    }

    private void validateRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantId);
        }
    }

    private double calculatePercentageChange(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        if (current == null) {
            current = BigDecimal.ZERO;
        }
        BigDecimal difference = current.subtract(previous);
        return difference.divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}
