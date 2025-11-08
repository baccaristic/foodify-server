package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.PageResponse;
import com.foodify.server.modules.restaurants.dto.RestaurantSearchItemDto;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantOperatingHourRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantSpecialDayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantSearchServicePromotionsTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    
    @Mock
    private MenuItemRepository menuItemRepository;
    
    @Mock
    private DeliveryFeeCalculator deliveryFeeCalculator;
    
    @Mock
    private RestaurantDeliveryMetricsService deliveryMetricsService;
    
    @Mock
    private RestaurantOperatingStatusService operatingStatusService;
    
    @Mock
    private RestaurantOperatingHourRepository operatingHourRepository;
    
    @Mock
    private RestaurantSpecialDayRepository specialDayRepository;

    private RestaurantSearchService service;

    @BeforeEach
    void setUp() {
        service = new RestaurantSearchService(
                restaurantRepository,
                menuItemRepository,
                deliveryFeeCalculator,
                deliveryMetricsService,
                operatingStatusService,
                operatingHourRepository,
                specialDayRepository
        );
    }

    @Test
    void getPromotions_shouldReturnSponsoredRestaurants() {
        // Arrange
        Restaurant sponsoredRestaurant1 = new Restaurant();
        sponsoredRestaurant1.setId(1L);
        sponsoredRestaurant1.setName("Sponsored Restaurant 1");
        sponsoredRestaurant1.setSponsored(true);
        sponsoredRestaurant1.setPosition(0);
        sponsoredRestaurant1.setLatitude(40.7128);
        sponsoredRestaurant1.setLongitude(-74.0060);

        Restaurant sponsoredRestaurant2 = new Restaurant();
        sponsoredRestaurant2.setId(2L);
        sponsoredRestaurant2.setName("Sponsored Restaurant 2");
        sponsoredRestaurant2.setSponsored(true);
        sponsoredRestaurant2.setPosition(1);
        sponsoredRestaurant2.setLatitude(40.7589);
        sponsoredRestaurant2.setLongitude(-73.9851);

        Page<Restaurant> mockPage = new PageImpl<>(List.of(sponsoredRestaurant1, sponsoredRestaurant2));
        when(restaurantRepository.findSponsored(any(Pageable.class))).thenReturn(mockPage);
        when(menuItemRepository.findByRestaurant_IdInAndPromotionActiveTrueAndAvailableTrue(any())).thenReturn(List.of());
        when(operatingHourRepository.findByRestaurant_IdOrderByDayOfWeekAsc(any())).thenReturn(List.of());
        when(specialDayRepository.findByRestaurant_IdOrderByDateAsc(any())).thenReturn(List.of());
        when(operatingStatusService.isRestaurantOpen(any(), any(), any(), any())).thenReturn(true);

        // Act
        PageResponse<RestaurantSearchItemDto> result = service.getPromotions(
                1,
                20,
                40.7128,
                -74.0060,
                Set.of(),
                Set.of(),
                LocalDate.now(),
                LocalTime.now()
        );

        // Assert
        assertNotNull(result);
        assertEquals(2, result.items().size());
        assertEquals("Sponsored Restaurant 1", result.items().get(0).name());
        assertEquals("Sponsored Restaurant 2", result.items().get(1).name());
        assertTrue(result.items().get(0).sponsored());
        assertTrue(result.items().get(1).sponsored());
        assertEquals(0, result.items().get(0).position());
        assertEquals(1, result.items().get(1).position());
    }

    @Test
    void getPromotions_shouldReturnEmptyWhenNoSponsoredRestaurants() {
        // Arrange
        Page<Restaurant> emptyPage = new PageImpl<>(List.of());
        when(restaurantRepository.findSponsored(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        PageResponse<RestaurantSearchItemDto> result = service.getPromotions(
                1,
                20,
                40.7128,
                -74.0060,
                Set.of(),
                Set.of(),
                LocalDate.now(),
                LocalTime.now()
        );

        // Assert
        assertNotNull(result);
        assertTrue(result.items().isEmpty());
    }
}
