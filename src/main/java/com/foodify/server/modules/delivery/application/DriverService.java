package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import com.foodify.server.modules.delivery.domain.DriverShiftBalance;
import com.foodify.server.modules.delivery.dto.DeliverOrderDto;
import com.foodify.server.modules.delivery.dto.PickUpOrderRequest;
import com.foodify.server.modules.delivery.dto.DriverShiftDto;
import com.foodify.server.modules.delivery.dto.DriverShiftBalanceDto;
import com.foodify.server.modules.delivery.dto.DriverEarningsSummaryDto;
import com.foodify.server.modules.delivery.application.QrCodeService;
import com.foodify.server.modules.delivery.application.GoogleMapsService;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DeliveryRepository;
import com.foodify.server.modules.delivery.repository.DriverShiftBalanceRepository;
import com.foodify.server.modules.delivery.repository.DriverShiftRepository;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.support.OrderPricingCalculator;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.orders.application.OrderLifecycleService;
import com.foodify.server.modules.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class DriverService {
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final DriverShiftRepository driverShiftRepository;
    private final DriverShiftBalanceRepository driverShiftBalanceRepository;
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

    private static final BigDecimal ZERO_AMOUNT = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);


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

        DriverShift activeShift = driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Driver must have an active shift to accept orders."));


        // Create Delivery
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDriver(driver);
        delivery.setShift(activeShift);
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
        updateShiftBalance(driver, order);
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
                DriverShiftBalance balance = new DriverShiftBalance();
                balance.setShift(activeShift);
                driverShiftBalanceRepository.save(balance);
                activeShift.setBalance(balance);
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
        DriverShiftBalance balance = resolveBalance(shift);
        return DriverShiftDto.builder()
                .status(shift.getStatus())
                .startedAt(shift.getStartedAt())
                .finishableAt(shift.getFinishableAt())
                .endedAt(shift.getEndedAt())
                .totalAmount(balance != null ? balance.getTotalAmount() : ZERO_AMOUNT)
                .driverShare(balance != null ? balance.getDriverShare() : ZERO_AMOUNT)
                .restaurantShare(balance != null ? balance.getRestaurantShare() : ZERO_AMOUNT)
                .settled(balance != null && balance.isSettled())
                .settledAt(balance != null ? balance.getSettledAt() : null)
                .build();
    }

    @Transactional
    public DriverShiftBalanceDto getCurrentShiftBalance(Long driverId) {
        BigDecimal driverShare = driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE)
                .map(shift -> {
                    DriverShiftBalance balance = resolveBalance(shift);
                    return balance != null ? balance.getDriverShare() : ZERO_AMOUNT;
                })
                .orElse(ZERO_AMOUNT);

        return new DriverShiftBalanceDto(driverShare);
    }

    public DriverEarningsSummaryDto getEarningsSummary(Long driverId, LocalDate dateOn, LocalDate from, LocalDate to) {
        BigDecimal availableBalance = sumDriverShare(driverId, null, null);

        LocalDate today = LocalDate.now();
        BigDecimal todayBalance = sumDriverShare(driverId, startOfDay(today), endOfDayExclusive(today));

        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        BigDecimal weekBalance = sumDriverShare(driverId, startOfDay(weekStart), startOfDay(weekStart.plusWeeks(1)));

        LocalDate monthStart = today.withDayOfMonth(1);
        BigDecimal monthBalance = sumDriverShare(driverId, startOfDay(monthStart), startOfDay(monthStart.plusMonths(1)));

        BigDecimal totalEarnings = null;
        if (dateOn != null) {
            totalEarnings = sumDriverShare(driverId, startOfDay(dateOn), endOfDayExclusive(dateOn));
        } else if (from != null || to != null) {
            LocalDateTime rangeStart = from != null ? startOfDay(from) : null;
            LocalDateTime rangeEnd = to != null ? endOfDayExclusive(to) : null;
            totalEarnings = sumDriverShare(driverId, rangeStart, rangeEnd);
        }

        return DriverEarningsSummaryDto.builder()
                .avilableBalance(availableBalance)
                .todayBalance(todayBalance)
                .weekBalance(weekBalance)
                .monthBalance(monthBalance)
                .totalEarnings(totalEarnings)
                .build();
    }

    private DriverShiftBalance resolveBalance(DriverShift shift) {
        if (shift == null) {
            return null;
        }
        DriverShiftBalance balance = shift.getBalance();
        if (balance == null && shift.getId() != null) {
            balance = driverShiftBalanceRepository.findByShift_Id(shift.getId()).orElse(null);
            shift.setBalance(balance);
        }
        return balance;
    }

    private BigDecimal sumDriverShare(Long driverId, LocalDateTime start, LocalDateTime end) {
        BigDecimal sum = driverShiftBalanceRepository
                .sumDriverShareByDriverIdAndStartedAtBetween(driverId, start, end);
        if (sum == null) {
            return ZERO_AMOUNT;
        }
        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime endOfDayExclusive(LocalDate date) {
        return date.plusDays(1).atStartOfDay();
    }

    private void updateShiftBalance(Driver driver, Order order) {
        if (driver == null || order == null) {
            return;
        }

        BigDecimal orderTotal = OrderPricingCalculator.calculateTotal(order);
        if (orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        Delivery delivery = order.getDelivery();
        DriverShift shift = Optional.ofNullable(delivery)
                .map(Delivery::getShift)
                .orElseGet(() -> findCurrentOrLatestShift(driver.getId()).orElse(null));

        if (shift == null) {
            log.warn("Unable to resolve shift for driver {} when recording order {} earnings", driver.getId(), order.getId());
            return;
        }

        if (delivery != null && delivery.getShift() == null) {
            delivery.setShift(shift);
            deliveryRepository.save(delivery);
        }

        DriverShiftBalance balance = shift.getBalance();
        if (balance == null) {
            balance = driverShiftBalanceRepository.findByShift_Id(shift.getId()).orElseGet(() -> {
                DriverShiftBalance newBalance = new DriverShiftBalance();
                newBalance.setShift(shift);
                return newBalance;
            });
        }
        BigDecimal restaurantShareRate = Optional.ofNullable(order.getRestaurant())
                .map(Restaurant::getRestaurantShareRate)
                .orElse(null);

        balance.recordOrder(orderTotal, restaurantShareRate);
        driverShiftBalanceRepository.save(balance);
        shift.setBalance(balance);
    }

    private Optional<DriverShift> findCurrentOrLatestShift(Long driverId) {
        Optional<DriverShift> activeShift = driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.ACTIVE);
        if (activeShift.isPresent()) {
            return activeShift;
        }
        return driverShiftRepository
                .findTopByDriverIdAndStatusOrderByStartedAtDesc(driverId, DriverShiftStatus.COMPLETED);
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
