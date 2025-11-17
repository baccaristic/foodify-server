package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.delivery.application.DriverDispatchService;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.application.OrderLifecycleService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuCategoryRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantOperatingHourRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantSpecialDayRepository;
import com.foodify.server.modules.storage.application.CloudflareImagesService;
import com.foodify.server.config.OrderViewProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceStartPreparingTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuCategoryRepository menuCategoryRepository;

    @Mock
    private RestaurantOperatingHourRepository restaurantOperatingHourRepository;

    @Mock
    private RestaurantSpecialDayRepository restaurantSpecialDayRepository;

    @Mock
    private DriverDispatchService driverDispatchService;

    @Mock
    private OrderLifecycleService orderLifecycleService;

    @Mock
    private OrderNotificationMapper orderNotificationMapper;

    @Mock
    private OrderViewProperties orderViewProperties;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private CloudflareImagesService cloudflareImagesService;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService(
                orderRepository,
                restaurantRepository,
                menuItemRepository,
                menuCategoryRepository,
                restaurantOperatingHourRepository,
                restaurantSpecialDayRepository,
                driverDispatchService,
                orderLifecycleService,
                orderNotificationMapper,
                orderViewProperties,
                webSocketService,
                cloudflareImagesService
        );
    }

    @Test
    void shouldTransitionOrderToPreparingWithEstimation() {
        // Arrange
        Long orderId = 1L;
        Long userId = 100L;
        Integer minutes = 30;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(userId);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.ACCEPTED);
        order.setRestaurant(restaurant);

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus(OrderStatus.PREPARING);
        updatedOrder.setRestaurant(restaurant);

        OrderNotificationDTO mockDto = mock(OrderNotificationDTO.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderLifecycleService.transition(any(Order.class), eq(OrderStatus.PREPARING), anyString(), anyString()))
                .thenReturn(updatedOrder);
        when(orderRepository.findDetailedById(orderId)).thenReturn(Optional.of(updatedOrder));
        when(orderNotificationMapper.toRestaurantDto(updatedOrder)).thenReturn(mockDto);

        // Act
        OrderNotificationDTO result = restaurantService.startPreparingOrder(orderId, userId, minutes);

        // Assert
        assertNotNull(result);
        verify(orderLifecycleService).transition(
                any(Order.class),
                eq(OrderStatus.PREPARING),
                eq("restaurant:" + userId),
                eq("Restaurant started preparing order")
        );
        assertNotNull(order.getEstimatedReadyAt());
        assertTrue(order.getEstimatedReadyAt().isAfter(LocalDateTime.now().plusMinutes(minutes - 1)));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotInAcceptedStatus() {
        // Arrange
        Long orderId = 1L;
        Long userId = 100L;
        Integer minutes = 30;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(userId);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PREPARING); // Already preparing
        order.setRestaurant(restaurant);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            restaurantService.startPreparingOrder(orderId, userId, minutes);
        });

        assertEquals("Only accepted orders can be started for preparation", exception.getMessage());
        verify(orderLifecycleService, never()).transition(any(Order.class), any(OrderStatus.class), anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenUnauthorized() {
        // Arrange
        Long orderId = 1L;
        Long userId = 100L;
        Long differentUserId = 200L;
        Integer minutes = 30;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(differentUserId); // Different user

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.ACCEPTED);
        order.setRestaurant(restaurant);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.startPreparingOrder(orderId, userId, minutes);
        });

        assertEquals("Unauthorized", exception.getMessage());
        verify(orderLifecycleService, never()).transition(any(Order.class), any(OrderStatus.class), anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenMinutesIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.startPreparingOrder(1L, 100L, null);
        });

        assertEquals("Estimated minutes are required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMinutesLessThanOne() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.startPreparingOrder(1L, 100L, 0);
        });

        assertEquals("Estimated minutes must be at least 1", exception.getMessage());
    }
}
