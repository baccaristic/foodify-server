package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.delivery.application.DriverDispatchService;
import com.foodify.server.modules.identity.domain.RestaurantAdmin;
import com.foodify.server.modules.identity.domain.RestaurantCashier;
import com.foodify.server.modules.identity.domain.Role;
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
import com.foodify.server.config.OrderViewProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests to verify that restaurant cashiers can perform the same operations as admins
 * on endpoints that are authorized for both ROLE_RESTAURANT_ADMIN and ROLE_RESTAURANT_CASHIER.
 */
@ExtendWith(MockitoExtension.class)
class RestaurantServiceCashierAuthorizationTest {

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
                webSocketService
        );
    }

    @Test
    void shouldAllowCashierToAcceptOrder() {
        // Arrange
        Long orderId = 1L;
        Long cashierId = 200L;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(100L);

        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setId(cashierId);
        cashier.setRole(Role.RESTAURANT_CASHIER);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);
        Set<RestaurantCashier> cashiers = new HashSet<>();
        cashiers.add(cashier);
        restaurant.setCashiers(cashiers);
        cashier.setRestaurant(restaurant);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurant(restaurant);

        Order acceptedOrder = new Order();
        acceptedOrder.setId(orderId);
        acceptedOrder.setStatus(OrderStatus.ACCEPTED);
        acceptedOrder.setRestaurant(restaurant);

        OrderNotificationDTO mockDto = mock(OrderNotificationDTO.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderLifecycleService.transition(any(Order.class), eq(OrderStatus.ACCEPTED), anyString(), anyString()))
                .thenReturn(acceptedOrder);
        when(orderRepository.findDetailedById(orderId)).thenReturn(Optional.of(acceptedOrder));
        when(orderNotificationMapper.toRestaurantDto(acceptedOrder)).thenReturn(mockDto);

        // Act
        OrderNotificationDTO result = restaurantService.acceptOrder(orderId, cashierId);

        // Assert
        assertNotNull(result);
        verify(orderLifecycleService).transition(
                any(Order.class),
                eq(OrderStatus.ACCEPTED),
                eq("restaurant:" + cashierId),
                eq("Restaurant accepted order")
        );
    }

    @Test
    void shouldAllowCashierToStartPreparingOrder() {
        // Arrange
        Long orderId = 1L;
        Long cashierId = 200L;
        Integer minutes = 30;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(100L);

        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setId(cashierId);
        cashier.setRole(Role.RESTAURANT_CASHIER);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);
        Set<RestaurantCashier> cashiers = new HashSet<>();
        cashiers.add(cashier);
        restaurant.setCashiers(cashiers);
        cashier.setRestaurant(restaurant);

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
        OrderNotificationDTO result = restaurantService.startPreparingOrder(orderId, cashierId, minutes);

        // Assert
        assertNotNull(result);
        verify(orderLifecycleService).transition(
                any(Order.class),
                eq(OrderStatus.PREPARING),
                eq("restaurant:" + cashierId),
                eq("Restaurant started preparing order")
        );
    }

    @Test
    void shouldAllowCashierToUpdatePreparationEstimate() {
        // Arrange
        Long orderId = 1L;
        Long cashierId = 200L;
        Integer minutes = 45;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(100L);

        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setId(cashierId);
        cashier.setRole(Role.RESTAURANT_CASHIER);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);
        Set<RestaurantCashier> cashiers = new HashSet<>();
        cashiers.add(cashier);
        restaurant.setCashiers(cashiers);
        cashier.setRestaurant(restaurant);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PREPARING);
        order.setRestaurant(restaurant);

        OrderNotificationDTO mockDto = mock(OrderNotificationDTO.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderRepository.findDetailedById(orderId)).thenReturn(Optional.of(order));
        when(orderNotificationMapper.toRestaurantDto(order)).thenReturn(mockDto);

        // Act
        OrderNotificationDTO result = restaurantService.updatePreparationEstimate(orderId, cashierId, minutes);

        // Assert
        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldAllowCashierToMarkOrderReady() {
        // Arrange
        Long orderId = 1L;
        Long cashierId = 200L;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(100L);

        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setId(cashierId);
        cashier.setRole(Role.RESTAURANT_CASHIER);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);
        Set<RestaurantCashier> cashiers = new HashSet<>();
        cashiers.add(cashier);
        restaurant.setCashiers(cashiers);
        cashier.setRestaurant(restaurant);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PREPARING);
        order.setRestaurant(restaurant);

        Order readyOrder = new Order();
        readyOrder.setId(orderId);
        readyOrder.setStatus(OrderStatus.READY_FOR_PICK_UP);
        readyOrder.setRestaurant(restaurant);

        OrderNotificationDTO mockDto = mock(OrderNotificationDTO.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderLifecycleService.transition(any(Order.class), eq(OrderStatus.READY_FOR_PICK_UP), anyString(), anyString()))
                .thenReturn(readyOrder);
        when(orderRepository.findDetailedById(orderId)).thenReturn(Optional.of(readyOrder));
        when(orderNotificationMapper.toRestaurantDto(readyOrder)).thenReturn(mockDto);

        // Act
        OrderNotificationDTO result = restaurantService.markOrderReady(orderId, cashierId);

        // Assert
        assertNotNull(result);
        verify(orderLifecycleService).transition(
                any(Order.class),
                eq(OrderStatus.READY_FOR_PICK_UP),
                eq("restaurant:" + cashierId),
                eq("Order ready for pickup")
        );
    }

    @Test
    void shouldDenyCashierFromDifferentRestaurant() {
        // Arrange
        Long orderId = 1L;
        Long cashierId = 200L;

        RestaurantAdmin admin = new RestaurantAdmin();
        admin.setId(100L);

        RestaurantCashier cashier = new RestaurantCashier();
        cashier.setId(cashierId);
        cashier.setRole(Role.RESTAURANT_CASHIER);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setAdmin(admin);
        // Cashier not added to this restaurant
        restaurant.setCashiers(new HashSet<>());

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurant(restaurant);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.acceptOrder(orderId, cashierId);
        });

        assertEquals("Unauthorized", exception.getMessage());
        verify(orderLifecycleService, never()).transition(any(Order.class), any(OrderStatus.class), anyString(), anyString());
    }
}
