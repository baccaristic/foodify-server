package com.foodify.server.service;

import com.foodify.server.dto.PickUpOrderRequest;
import com.foodify.server.enums.NotificationType;
import com.foodify.server.enums.OrderStatus;
import com.foodify.server.firebase.PushNotificationService;
import com.foodify.server.models.Delivery;
import com.foodify.server.models.Driver;
import com.foodify.server.models.Order;
import com.foodify.server.models.UserDevice;
import com.foodify.server.repository.DeliveryRepository;
import com.foodify.server.repository.DriverRepository;
import com.foodify.server.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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


    public Order acceptOrder(Long driverId, Long orderId) {
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
        return orderRepository.save(order);
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
        orderRepository.save(order);
        return true;
    }
}
