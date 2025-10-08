package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.application.GoogleMapsService;
import com.foodify.server.modules.delivery.application.QrCodeService;
import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.dto.DeliverOrderDto;
import com.foodify.server.modules.delivery.dto.PickUpOrderRequest;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DeliveryRepository;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.application.OrderLifecycleService;
import com.foodify.server.modules.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final QrCodeService qrCodeService;
    private final StringRedisTemplate redisTemplate;
    private final GoogleMapsService googleMapsService;
    private final DriverLocationService driverLocationService;
    private final OrderLifecycleService orderLifecycleService;


    public OrderDto acceptOrder(Long driverId, Long orderId) throws Exception {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        Order order = orderRepository.findById(orderId).orElse(null);

        if (driver == null || order == null) {
            return null;
        }

        // Prevent re-assigning if already has delivery
        if (order.getDelivery() != null) {
            throw new IllegalStateException("Order already has a driver assigned.");
        }
        if (!driver.isAvailable()) {
            throw new IllegalStateException("Driver is not available.");
        }

        redisTemplate.delete("driver:status:" + driverId);


        // Create Delivery
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDriver(driver);
        Point lastPosition = driverLocationService.getLastKnownPosition(driverId);
        Double restaurantLat = order.getRestaurant() != null ? order.getRestaurant().getLatitude() : null;
        Double restaurantLng = order.getRestaurant() != null ? order.getRestaurant().getLongitude() : null;
        if (restaurantLat == null || restaurantLng == null) {
            throw new IllegalStateException("Order is missing restaurant location details");
        }
        delivery.setTimeToPickUp(
                googleMapsService.getDrivingRoute(
                        lastPosition.getY(),
                        lastPosition.getX(),
                        restaurantLat,
                        restaurantLng
                )
        );
        delivery.setDeliveryTime(
                googleMapsService.getDrivingRoute(
                        restaurantLat,
                        restaurantLng,
                        order.getLat(),
                        order.getLng()
                )
        );

        // Update relations
        order.setDelivery(delivery);
        String token = qrCodeService.generatePickupToken();
        order.setPickupToken(token);
        driver.setAvailable(false);

        // Persist changes
        deliveryRepository.save(delivery);
        driverRepository.save(driver);
        order.setPendingDriver(null);
        Order updatedOrder = orderLifecycleService.transition(order, OrderStatus.PREPARING,
                "driver:" + driverId,
                "Driver accepted order");
        return OrderMapper.toDto(updatedOrder);
    }

    public Driver findById(Long driverId) {
        return driverRepository.findById(driverId).orElse(null);
    }
    public List<Order> getIncommingOrders(Long driverId) {
        return orderRepository.findAllByPendingDriverId(driverId);
    }
    public Order getOrderDetails(Long orderId, Long userId ) {
        Driver driver = driverRepository.findById(userId).orElse(null);
        Order order = orderRepository.findById(orderId).orElse(null);
        if (Objects.equals(order.getDelivery().getDriver().getId(), driver.getId())) {
            return order;
        }
        return null;
    }

    public Boolean pickUpOrder(PickUpOrderRequest request, Long userId) {
        Order order = orderRepository.findById(Long.valueOf(request.getOrderId())).orElse(null);
        if (order == null) {
            return false;
        }
        if (!Objects.equals(order.getDelivery().getDriver().getId(), userId)) {
            return false;
        }
        if (!Objects.equals(order.getPickupToken(), request.getToken())) {
            return false;
        }
        String deliveryToken = String.format("%03d", new Random().nextInt(900) + 100);
        order.setDeliveryToken(deliveryToken);
        orderLifecycleService.transition(order, OrderStatus.IN_DELIVERY,
                "driver:" + userId,
                "Order picked up by driver");
        return true;
    }
    @Transactional
    public OrderDto getOngoingOrder(Long userId) {
        return OrderMapper.toDto(orderRepository.findByDriverIdAndStatusIn(userId, List.of(OrderStatus.ACCEPTED, OrderStatus.PREPARING,
                OrderStatus.READY_FOR_PICK_UP, OrderStatus.IN_DELIVERY)).orElse(null));
    }

    public Boolean deliverOrder(Long driverId, DeliverOrderDto request) {
        Order order = orderRepository.findById(request.getOrderId()).orElse(null);
        if (order == null || !Objects.equals(order.getDelivery().getDriver().getId(), driverId)) {
            return false;
        }
        if (!order.getDeliveryToken().equals(request.getToken())) {
            return false;
        }
        order.getDelivery().setDeliveredTime(LocalDateTime.now());
        deliveryRepository.save(order.getDelivery());
        orderRepository.save(order);
        orderLifecycleService.transition(order, OrderStatus.DELIVERED,
                "driver:" + driverId,
                "Order delivered to client");
        return true;
    }

    public List<OrderDto> getOrderHistory(Long driverId) {
        List<Order> orders = orderRepository.findAllByDriverIdAndStatus(driverId, OrderStatus.DELIVERED);
        List<OrderDto> result = new ArrayList<>();
        orders.forEach(order -> {
            result.add(OrderMapper.toDto(order));
        });
        return result;
    }
}
