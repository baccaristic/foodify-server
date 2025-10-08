package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import com.foodify.server.modules.delivery.dto.DeliverOrderDto;
import com.foodify.server.modules.delivery.dto.PickUpOrderRequest;
import com.foodify.server.modules.delivery.dto.DriverShiftDto;
import com.foodify.server.modules.delivery.application.QrCodeService;
import com.foodify.server.modules.delivery.application.GoogleMapsService;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DeliveryRepository;
import com.foodify.server.modules.delivery.repository.DriverShiftRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class DriverService {
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final DriverShiftRepository driverShiftRepository;
    private final QrCodeService qrCodeService;
    private final GoogleMapsService googleMapsService;
    private final DriverLocationService driverLocationService;
    private final DriverAvailabilityService driverAvailabilityService;
    private final OrderLifecycleService orderLifecycleService;

    private static final List<OrderStatus> ACTIVE_DRIVER_STATUSES = List.of(
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY
    );


    public OrderDto acceptOrder(Long driverId, Long orderId) throws Exception {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        Order order = orderRepository.findById(orderId).orElse(null);

        if (driver == null || order == null) {
            return null;
        }

        if (orderRepository.findByDriverIdAndStatusIn(driverId, ACTIVE_DRIVER_STATUSES).isPresent()) {
            throw new IllegalStateException("Driver already has an active order.");
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
        Point lastPosition = driverLocationService.getLastKnownPosition(driverId);
        delivery.setTimeToPickUp(resolveRouteDuration(
                lastPosition != null ? lastPosition.getY() : null,
                lastPosition != null ? lastPosition.getX() : null,
                order.getRestaurant() != null ? order.getRestaurant().getLatitude() : null,
                order.getRestaurant() != null ? order.getRestaurant().getLongitude() : null
        ));
        delivery.setDeliveryTime(resolveRouteDuration(
                order.getRestaurant() != null ? order.getRestaurant().getLatitude() : null,
                order.getRestaurant() != null ? order.getRestaurant().getLongitude() : null,
                order.getLat(),
                order.getLng()
        ));
        delivery.setAssignedTime(LocalDateTime.now());

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
        driverLocationService.markBusy(String.valueOf(driverId), orderId);
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
        if (driver == null || order == null) {
            return null;
        }
        Delivery delivery = order.getDelivery();
        if (delivery == null || delivery.getDriver() == null) {
            return null;
        }
        if (Objects.equals(delivery.getDriver().getId(), driver.getId())) {
            return order;
        }
        return null;
    }

    public Boolean pickUpOrder(PickUpOrderRequest request, Long userId) {
        Long orderId;
        try {
            orderId = Long.valueOf(request.getOrderId());
        } catch (NumberFormatException ex) {
            log.warn("Driver {} sent invalid order id {} when attempting pickup", userId, request.getOrderId());
            return false;
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }
        Delivery delivery = order.getDelivery();
        if (delivery == null || delivery.getDriver() == null || !Objects.equals(delivery.getDriver().getId(), userId)) {
            return false;
        }
        if (!Objects.equals(order.getPickupToken(), request.getToken())) {
            return false;
        }
        String deliveryToken = String.format("%03d", new Random().nextInt(900) + 100);
        order.setDeliveryToken(deliveryToken);
        delivery.setPickupTime(LocalDateTime.now());
        deliveryRepository.save(delivery);
        orderLifecycleService.transition(order, OrderStatus.IN_DELIVERY,
                "driver:" + userId,
                "Order picked up by driver");
        driverLocationService.markBusy(String.valueOf(userId), order.getId());
        return true;
    }
    @Transactional
    public OrderDto getOngoingOrder(Long userId) {
        return OrderMapper.toDto(orderRepository.findByDriverIdAndStatusIn(userId, ACTIVE_DRIVER_STATUSES).orElse(null));
    }

    public Optional<Order> getOrderInDelivery(Long driverId) {
        return orderRepository.findByDriverIdAndStatusIn(driverId, List.of(OrderStatus.IN_DELIVERY));
    }

    public Boolean deliverOrder(Long driverId, DeliverOrderDto request) {
        Order order = orderRepository.findById(request.getOrderId()).orElse(null);
        if (order == null) {
            return false;
        }
        Delivery delivery = order.getDelivery();
        if (delivery == null || delivery.getDriver() == null || !Objects.equals(delivery.getDriver().getId(), driverId)) {
            return false;
        }
        if (!Objects.equals(order.getDeliveryToken(), request.getToken())) {
            return false;
        }
        delivery.setDeliveredTime(LocalDateTime.now());
        deliveryRepository.save(delivery);
        Driver driver = delivery.getDriver();
        driverAvailabilityService.refreshAvailability(driver.getId());
        order.setDeliveryToken(null);
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

    @Transactional
    public DriverShiftDto updateAvailability(Long driverId, boolean available) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() ->
                new IllegalArgumentException("Driver not found"));

        DriverShift activeShift = driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE)
                .orElse(null);

        if (available) {
            if (activeShift == null) {
                LocalDateTime now = LocalDateTime.now();
                DriverShift newShift = new DriverShift();
                newShift.setDriver(driver);
                newShift.setStatus(DriverShiftStatus.ACTIVE);
                newShift.setStartedAt(now);
                newShift.setFinishableAt(now.plusHours(2));
                activeShift = driverShiftRepository.save(newShift);
            }
            driverLocationService.markAvailable(String.valueOf(driverId));
            return toShiftDto(activeShift);
        }

        if (activeShift != null) {
            LocalDateTime now = LocalDateTime.now();
            if (activeShift.getFinishableAt() != null && activeShift.getFinishableAt().isAfter(now)) {
                throw new IllegalStateException("Shift can be ended after " + activeShift.getFinishableAt());
            }
            activeShift.setStatus(DriverShiftStatus.COMPLETED);
            activeShift.setEndedAt(now);
            driverShiftRepository.save(activeShift);
            driverLocationService.markUnavailable(String.valueOf(driverId));
            return toShiftDto(activeShift);
        }

        driverLocationService.markUnavailable(String.valueOf(driverId));
        return null;
    }

    @Transactional
    public DriverShiftDto getCurrentShift(Long driverId) {
        return driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE)
                .map(this::toShiftDto)
                .orElse(null);
    }

    private DriverShiftDto toShiftDto(DriverShift shift) {
        if (shift == null) {
            return null;
        }
        return DriverShiftDto.builder()
                .status(shift.getStatus())
                .startedAt(shift.getStartedAt())
                .finishableAt(shift.getFinishableAt())
                .endedAt(shift.getEndedAt())
                .build();
    }

    private Long resolveRouteDuration(Double originLat, Double originLng, Double destLat, Double destLng) {
        if (originLat == null || originLng == null || destLat == null || destLng == null) {
            return null;
        }
        try {
            return googleMapsService.getDrivingRoute(originLat, originLng, destLat, destLng);
        } catch (Exception ex) {
            log.warn("Failed to fetch route duration for driver flow", ex);
            return null;
        }
    }
}
