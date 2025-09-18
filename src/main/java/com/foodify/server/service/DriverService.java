package com.foodify.server.service;

import com.foodify.server.enums.OrderStatus;
import com.foodify.server.models.Delivery;
import com.foodify.server.models.Driver;
import com.foodify.server.models.Order;
import com.foodify.server.repository.DeliveryRepository;
import com.foodify.server.repository.DriverRepository;
import com.foodify.server.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;


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

        // Create Delivery
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDriver(driver);

        // Update relations
        order.setDelivery(delivery);
        order.setStatus(OrderStatus.PREPARING);
        driver.setAvailable(false);

        // Persist changes
        deliveryRepository.save(delivery);
        driverRepository.save(driver);
        return orderRepository.save(order);
    }

    public Driver findById(Long driverId) {
        return driverRepository.findById(driverId).orElse(null);
    }
    public List<Order> getIncommingOrders(Long driverId) {
        return orderRepository.findAllByPendingDriverId(driverId);
    }
}
