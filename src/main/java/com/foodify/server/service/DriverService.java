package com.foodify.server.service;

import com.foodify.server.Redis.DriverLocationService;
import com.foodify.server.dto.DeliverOrderDto;
import com.foodify.server.dto.OrderDto;
import com.foodify.server.dto.PickUpOrderRequest;
import com.foodify.server.enums.NotificationType;
import com.foodify.server.enums.OrderStatus;
import com.foodify.server.firebase.PushNotificationService;
import com.foodify.server.mappers.OrderMapper;
import com.foodify.server.models.Delivery;
import com.foodify.server.models.Driver;
import com.foodify.server.models.Order;
import com.foodify.server.models.UserDevice;
import com.foodify.server.repository.DeliveryRepository;
import com.foodify.server.repository.DriverRepository;
import com.foodify.server.repository.OrderRepository;
import com.foodify.server.ws.WebSocketService;
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
    private final UserDeviceService userDeviceService;
    private final PushNotificationService pushNotificationService;
    private final QrCodeService qrCodeService;
    private final StringRedisTemplate redisTemplate;
    private final GoogleMapsService googleMapsService;
    private final DriverLocationService driverLocationService;
    private final WebSocketService webSocketService;


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
        delivery.setTimeToPickUp(
                googleMapsService.getDrivingRoute(
                        lastPosition.getY(),
                        lastPosition.getX(),
                        order.getRestaurant().getLatitude(),
                        order.getRestaurant().getLongitude()
                )
        );
        delivery.setDeliveryTime(
                googleMapsService.getDrivingRoute(
                        order.getRestaurant().getLatitude(),
                        order.getRestaurant().getLongitude(),
                        order.getLat(),
                        order.getLng()
                )
        );

        // Update relations
        order.setDelivery(delivery);
        order.setStatus(OrderStatus.PREPARING);
        String token = qrCodeService.generatePickupToken();
        order.setPickupToken(token);
        driver.setAvailable(false);

        // Persist changes
        deliveryRepository.save(delivery);
        driverRepository.save(driver);
        List<UserDevice> userDevices = userDeviceService.findByUser(order.getClient().getId());
        userDevices.forEach(userDevice -> {
            try {
                pushNotificationService.sendOrderNotification(
                        userDevice.getDeviceToken(),
                        orderId,
                        "Your order has been updated",
                        "A driver has been assigned to your order",
                        NotificationType.ORDER_DRIVER_ASSIGNED
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        order.setPendingDriver(null);
        order = orderRepository.save(order);
        webSocketService.notifyDriver(driverId, order);
        return OrderMapper.toDto(order);
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
        order.setStatus(OrderStatus.IN_DELIVERY);
        String deliveryToken = String.format("%03d", new Random().nextInt(900) + 100);
        order.setDeliveryToken(deliveryToken);
        order = orderRepository.save(order);
        webSocketService.notifyDriver(userId, order);
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
        order.setStatus(OrderStatus.DELIVERED);
        order.getDelivery().setDeliveredTime(LocalDateTime.now());
        webSocketService.notifyDriver(driverId, orderRepository.save(order));
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
