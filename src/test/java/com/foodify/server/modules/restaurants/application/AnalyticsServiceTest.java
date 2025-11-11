package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.dto.analytics.*;
import com.foodify.server.modules.restaurants.repository.RestaurantRatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRatingRepository ratingRepository;

    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        analyticsService = new AnalyticsService(orderRepository, ratingRepository);
    }

    @Test
    void getGeneralOverview_withTodayPeriod_returnsCorrectMetrics() {
        // Arrange
        Long restaurantId = 1L;
        AnalyticsPeriod period = AnalyticsPeriod.TODAY;
        
        // Mock revenue
        when(orderRepository.getTotalRevenueForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.valueOf(1000)) // today
                .thenReturn(BigDecimal.valueOf(800));  // yesterday
        
        // Mock orders
        when(orderRepository.countDeliveredOrdersForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(20L) // today
                .thenReturn(15L); // yesterday
        
        // Mock prep time
        when(orderRepository.getAveragePreparationTimeForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.valueOf(25)) // today
                .thenReturn(BigDecimal.valueOf(30)); // yesterday
        
        // Mock ratings
        RestaurantRatingRepository.RatingAggregate todayRating = 
                new RestaurantRatingRepository.RatingAggregate(10L, 8L);
        RestaurantRatingRepository.RatingAggregate yesterdayRating = 
                new RestaurantRatingRepository.RatingAggregate(5L, 3L);
        when(ratingRepository.findAggregateByRestaurantIdAndPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(todayRating)
                .thenReturn(yesterdayRating);

        // Act
        GeneralOverviewResponse response = analyticsService.getGeneralOverview(restaurantId, period);

        // Assert
        assertNotNull(response);
        assertEquals(AnalyticsPeriod.TODAY, response.period());
        
        // Verify revenue
        assertEquals(BigDecimal.valueOf(1000).setScale(2), response.revenue().current());
        assertEquals(BigDecimal.valueOf(800).setScale(2), response.revenue().previous());
        assertEquals(BigDecimal.valueOf(200).setScale(2), response.revenue().change());
        assertTrue(response.revenue().percentageChange().compareTo(BigDecimal.ZERO) > 0);
        
        // Verify orders
        assertEquals(20L, response.orders().current());
        assertEquals(15L, response.orders().previous());
        assertEquals(5L, response.orders().change());
        
        // Verify prep time
        assertEquals(BigDecimal.valueOf(25).setScale(2), response.preparationTime().currentMinutes());
        assertEquals(BigDecimal.valueOf(30).setScale(2), response.preparationTime().previousMinutes());
        
        // Verify ratings
        assertNotNull(response.rating());
        assertTrue(response.rating().currentStars().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void getGeneralOverview_withZeroData_handlesGracefully() {
        // Arrange
        Long restaurantId = 1L;
        AnalyticsPeriod period = AnalyticsPeriod.TODAY;
        
        when(orderRepository.getTotalRevenueForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.ZERO);
        when(orderRepository.countDeliveredOrdersForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);
        when(orderRepository.getAveragePreparationTimeForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.ZERO);
        
        RestaurantRatingRepository.RatingAggregate emptyRating = 
                new RestaurantRatingRepository.RatingAggregate(0L, 0L);
        when(ratingRepository.findAggregateByRestaurantIdAndPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(emptyRating);

        // Act
        GeneralOverviewResponse response = analyticsService.getGeneralOverview(restaurantId, period);

        // Assert
        assertNotNull(response);
        assertEquals(0, BigDecimal.ZERO.compareTo(response.revenue().current()));
        assertEquals(0L, response.orders().current());
        assertEquals(0, BigDecimal.ZERO.compareTo(response.rating().currentStars()));
    }

    @Test
    void getSalesTrend_withTodayPeriod_returnsTrendData() {
        // Arrange
        Long restaurantId = 1L;
        AnalyticsPeriod period = AnalyticsPeriod.TODAY;
        
        LocalDate today = LocalDate.now();
        OrderRepository.SalesTrendProjection projection = mock(OrderRepository.SalesTrendProjection.class);
        when(projection.getDate()).thenReturn(today);
        when(projection.getRevenue()).thenReturn(BigDecimal.valueOf(1000));
        when(projection.getOrderCount()).thenReturn(20L);
        
        when(orderRepository.getSalesTrendForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(projection));

        // Act
        SalesTrendResponse response = analyticsService.getSalesTrend(restaurantId, period);

        // Assert
        assertNotNull(response);
        assertEquals(AnalyticsPeriod.TODAY, response.period());
        assertFalse(response.data().isEmpty());
        assertEquals(today, response.data().get(0).date());
        assertEquals(BigDecimal.valueOf(1000), response.data().get(0).revenue());
        assertEquals(20L, response.data().get(0).orderCount());
    }

    @Test
    void getTopDishes_withTodayPeriod_returnsTop3Dishes() {
        // Arrange
        Long restaurantId = 1L;
        AnalyticsPeriod period = AnalyticsPeriod.TODAY;
        
        List<OrderRepository.TopDishProjection> projections = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            OrderRepository.TopDishProjection projection = mock(OrderRepository.TopDishProjection.class);
            when(projection.getMenuItemId()).thenReturn((long) i);
            when(projection.getMenuItemName()).thenReturn("Dish " + i);
            when(projection.getOrderCount()).thenReturn((long) (10 - i));
            when(projection.getQuantitySold()).thenReturn((long) (50 - i * 5));
            projections.add(projection);
        }
        
        when(orderRepository.getTopDishesForPeriod(
                eq(restaurantId), 
                any(LocalDateTime.class), 
                any(LocalDateTime.class), 
                any(PageRequest.class)))
                .thenReturn(projections);

        // Act
        TopDishesResponse response = analyticsService.getTopDishes(restaurantId, period);

        // Assert
        assertNotNull(response);
        assertEquals(AnalyticsPeriod.TODAY, response.period());
        assertEquals(3, response.topDishes().size());
        assertEquals("Dish 1", response.topDishes().get(0).menuItemName());
        assertEquals(9L, response.topDishes().get(0).orderCount());
        assertEquals(45L, response.topDishes().get(0).quantitySold());
    }

    @Test
    void getGeneralOverview_withWeekPeriod_calculatesCorrectly() {
        // Arrange
        Long restaurantId = 1L;
        AnalyticsPeriod period = AnalyticsPeriod.WEEK;
        
        when(orderRepository.getTotalRevenueForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.valueOf(5000))  // this week
                .thenReturn(BigDecimal.valueOf(4500)); // last week
        
        when(orderRepository.countDeliveredOrdersForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(100L) // this week
                .thenReturn(90L);  // last week
        
        when(orderRepository.getAveragePreparationTimeForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.valueOf(20))
                .thenReturn(BigDecimal.valueOf(25));
        
        RestaurantRatingRepository.RatingAggregate thisWeek = 
                new RestaurantRatingRepository.RatingAggregate(50L, 40L);
        RestaurantRatingRepository.RatingAggregate lastWeek = 
                new RestaurantRatingRepository.RatingAggregate(45L, 30L);
        when(ratingRepository.findAggregateByRestaurantIdAndPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(thisWeek)
                .thenReturn(lastWeek);

        // Act
        GeneralOverviewResponse response = analyticsService.getGeneralOverview(restaurantId, period);

        // Assert
        assertNotNull(response);
        assertEquals(AnalyticsPeriod.WEEK, response.period());
        assertEquals(100L, response.orders().current());
        assertEquals(90L, response.orders().previous());
        assertEquals(10L, response.orders().change());
    }

    @Test
    void getGeneralOverview_withMonthPeriod_calculatesCorrectly() {
        // Arrange
        Long restaurantId = 1L;
        AnalyticsPeriod period = AnalyticsPeriod.MONTH;
        
        when(orderRepository.getTotalRevenueForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.valueOf(20000))  // this month
                .thenReturn(BigDecimal.valueOf(18000)); // last month
        
        when(orderRepository.countDeliveredOrdersForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(400L) // this month
                .thenReturn(350L); // last month
        
        when(orderRepository.getAveragePreparationTimeForPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(BigDecimal.valueOf(22))
                .thenReturn(BigDecimal.valueOf(23));
        
        RestaurantRatingRepository.RatingAggregate thisMonth = 
                new RestaurantRatingRepository.RatingAggregate(200L, 160L);
        RestaurantRatingRepository.RatingAggregate lastMonth = 
                new RestaurantRatingRepository.RatingAggregate(180L, 135L);
        when(ratingRepository.findAggregateByRestaurantIdAndPeriod(eq(restaurantId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(thisMonth)
                .thenReturn(lastMonth);

        // Act
        GeneralOverviewResponse response = analyticsService.getGeneralOverview(restaurantId, period);

        // Assert
        assertNotNull(response);
        assertEquals(AnalyticsPeriod.MONTH, response.period());
        assertEquals(400L, response.orders().current());
        assertEquals(350L, response.orders().previous());
        assertEquals(50L, response.orders().change());
    }
}
