package com.foodify.server.modules.delivery.application;

import com.foodify.server.config.OrderViewProperties;
import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.delivery.application.DriverAvailabilityService;
import com.foodify.server.modules.delivery.application.DriverDispatchService;
import com.foodify.server.modules.delivery.application.GoogleMapsService;
import com.foodify.server.modules.delivery.application.QrCodeService;
import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftBalance;
import com.foodify.server.modules.delivery.domain.DriverShiftStatus;
import com.foodify.server.modules.delivery.dto.DeliverOrderDto;
import com.foodify.server.modules.delivery.dto.DriverEarningsSummaryDto;
import com.foodify.server.modules.delivery.dto.DriverShiftBalanceDto;
import com.foodify.server.modules.delivery.dto.DriverShiftDto;
import com.foodify.server.modules.delivery.dto.DriverShiftEarningDetailsDto;
import com.foodify.server.modules.delivery.dto.DriverShiftIncomeDto;
import com.foodify.server.modules.delivery.dto.DriverShiftIncomeResponseDto;
import com.foodify.server.modules.delivery.dto.DriverShiftOrderEarningDto;
import com.foodify.server.modules.delivery.dto.PickUpOrderRequest;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.delivery.repository.DeliveryRepository;
import com.foodify.server.modules.delivery.repository.DriverShiftBalanceRepository;
import com.foodify.server.modules.delivery.repository.DriverShiftRepository;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.identity.repository.DriverRepository;
import com.foodify.server.modules.orders.application.OrderLifecycleService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.repository.OrderItemRepository;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.orders.support.OrderPricingCalculator;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DriverService {
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final DriverShiftRepository driverShiftRepository;
    private final DriverShiftBalanceRepository driverShiftBalanceRepository;
    private final DriverFinancialService driverFinancialService;
    private final OrderItemRepository orderItemRepository;
    private final QrCodeService qrCodeService;
    private final GoogleMapsService googleMapsService;
    private final DriverLocationService driverLocationService;
    private final DriverAvailabilityService driverAvailabilityService;
    private final DriverDispatchService driverDispatchService;
    private final DriverDisciplineService driverDisciplineService;
    private final OrderLifecycleService orderLifecycleService;
    private final OrderViewProperties orderViewProperties;

    private static final List<OrderStatus> ACTIVE_DRIVER_STATUSES = List.of(
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY
    );

    private static final BigDecimal ZERO_AMOUNT = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal DEFAULT_DRIVER_COMMISSION_RATE = new BigDecimal("0.12");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public OrderDto acceptOrder(Long driverId, Long orderId) throws Exception {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        Order order = orderRepository.findById(orderId).orElse(null);

        if (driver == null || order == null) {
            return null;
        }

        driverFinancialService.assertCanWork(driver);

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
        orderRepository.save(order);
        driverDispatchService.markDriverAccepted(order.getId());
        driverDisciplineService.resetConsecutiveDeclines(driverId);
        driverLocationService.markBusy(String.valueOf(driverId), orderId);
        return OrderMapper.toDto(order);
    }

    @Transactional
    public void declineOrder(Long driverId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getDelivery() != null) {
            throw new IllegalStateException("Order already has a driver assigned.");
        }

        if (order.getPendingDriver() == null || !Objects.equals(order.getPendingDriver().getId(), driverId)) {
            throw new IllegalStateException("Driver is not assigned to this order.");
        }

        order.setPendingDriver(null);
        orderRepository.save(order);
        driverLocationService.markAvailable(String.valueOf(driverId));
        driverDisciplineService.recordDecline(driverId);
        driverDispatchService.handleDriverDecline(orderId, driverId);
    }

    public Driver findById(Long driverId) {
        return driverRepository.findById(driverId).orElse(null);
    }
    public List<Order> getIncommingOrders(Long driverId) {
        if (driverId == null) {
            return List.of();
        }
        int limit = Math.max(orderViewProperties.getPendingDriverLimit(), 1);
        return orderRepository.findAllByPendingDriverId(driverId, PageRequest.of(0, limit)).getContent();
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
        order.setDeliveryToken(null);
        orderLifecycleService.transition(order, OrderStatus.DELIVERED,
                "driver:" + driverId,
                "Order delivered to client");
        updateShiftBalance(driver, order);
        driverFinancialService.recordDelivery(order);
        driverAvailabilityService.refreshAvailability(driver.getId());
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
            driverFinancialService.assertCanWork(driver);
            driverFinancialService.applyDailyFeeIfNeeded(driver);
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

    public DriverShiftIncomeResponseDto getShiftIncomeDetails(Long driverId, LocalDate dateOn, LocalDate from, LocalDate to) {
        LocalDate rangeStart = dateOn != null ? dateOn : from;
        LocalDate rangeEnd = dateOn != null ? dateOn : to;

        if (rangeStart == null && rangeEnd == null) {
            LocalDate today = LocalDate.now();
            rangeStart = today;
            rangeEnd = today;
        }

        LocalDateTime startDateTime = rangeStart != null ? startOfDay(rangeStart) : null;
        LocalDateTime endDateTime = rangeEnd != null ? endOfDayExclusive(rangeEnd) : null;

        List<DriverShift> shifts;
        if (startDateTime != null && endDateTime != null) {
            shifts = driverShiftRepository
                    .findAllWithBalanceByDriverIdAndStartedAtBetweenOrderByStartedAtDesc(
                            driverId, startDateTime, endDateTime);
        } else if (startDateTime != null) {
            shifts = driverShiftRepository
                    .findAllWithBalanceByDriverIdAndStartedAtGreaterThanEqualOrderByStartedAtDesc(
                            driverId, startDateTime);
        } else if (endDateTime != null) {
            shifts = driverShiftRepository
                    .findAllWithBalanceByDriverIdAndStartedAtLessThanOrderByStartedAtDesc(
                            driverId, endDateTime);
        } else {
            shifts = driverShiftRepository.findAllWithBalanceByDriverIdOrderByStartedAtDesc(driverId);
        }

        Map<Long, DriverShift> uniqueShifts = new LinkedHashMap<>();
        for (DriverShift shift : shifts) {
            if (shift.getId() != null) {
                uniqueShifts.putIfAbsent(shift.getId(), shift);
            }
        }

        BigDecimal total = ZERO_AMOUNT;
        List<DriverShiftIncomeDto> shiftDtos = new ArrayList<>();
        for (DriverShift shift : uniqueShifts.values()) {
            DriverShiftBalance balance = resolveBalance(shift);
            BigDecimal driverShare = balance != null ? balance.getDriverShare() : ZERO_AMOUNT;
            total = total.add(driverShare);

            String startTime = Optional.ofNullable(shift.getStartedAt())
                    .map(time -> time.format(TIME_FORMATTER))
                    .orElse(null);
            String endTime = Optional.ofNullable(shift.getEndedAt())
                    .map(time -> time.format(TIME_FORMATTER))
                    .orElse(null);

            shiftDtos.add(DriverShiftIncomeDto.builder()
                    .id(shift.getId())
                    .startTime(startTime)
                    .endTime(endTime)
                    .total(driverShare)
                    .build());
        }

        total = total.setScale(2, RoundingMode.HALF_UP);

        return DriverShiftIncomeResponseDto.builder()
                .total(total)
                .shifts(shiftDtos)
                .build();
    }

    public DriverShiftEarningDetailsDto getShiftEarningDetails(Long driverId, Long shiftId) {
        DriverShift shift = driverShiftRepository
                .findByIdAndDriverIdWithDetails(shiftId, driverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift not found"));

        DriverShiftBalance balance = resolveBalance(shift);
        BigDecimal shiftTotal = Optional.ofNullable(balance)
                .map(DriverShiftBalance::getDriverShare)
                .orElse(ZERO_AMOUNT)
                .setScale(2, RoundingMode.HALF_UP);

        Map<Long, Delivery> uniqueDeliveries = new LinkedHashMap<>();
        for (Delivery delivery : Optional.ofNullable(shift.getDeliveries()).orElse(Collections.emptyList())) {
            if (delivery == null || delivery.getOrder() == null || delivery.getId() == null) {
                continue;
            }
            uniqueDeliveries.putIfAbsent(delivery.getId(), delivery);
        }

        Collection<Delivery> deliveries = uniqueDeliveries.values();
        Map<Long, OrderEarningAggregate> orderAggregates = loadOrderAggregates(deliveries);

        List<DriverShiftOrderEarningDto> orders = deliveries.stream()
                .sorted(Comparator.comparing(this::resolveDeliverySortTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(delivery -> mapToOrderEarningDto(delivery, orderAggregates.get(resolveOrderId(delivery))))
                .collect(Collectors.toList());

        return DriverShiftEarningDetailsDto.builder()
                .shiftId(shift.getId())
                .from(formatTime(shift.getStartedAt()))
                .to(formatTime(resolveShiftEnd(shift)))
                .total(shiftTotal)
                .date(formatDate(shift.getStartedAt()))
                .orders(orders)
                .build();
    }

    private DriverShiftOrderEarningDto mapToOrderEarningDto(Delivery delivery, OrderEarningAggregate aggregate) {
        Order order = delivery.getOrder();
        BigDecimal orderValue = Optional.ofNullable(aggregate)
                .map(OrderEarningAggregate::orderValue)
                .orElse(ZERO_AMOUNT);
        Restaurant restaurant = order.getRestaurant();
        Integer itemsCount = Optional.ofNullable(aggregate)
                .map(OrderEarningAggregate::itemCount)
                .orElse(0);
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee()).orElse(ZERO_AMOUNT);
        BigDecimal orderTotal = Optional.ofNullable(order.getTotal())
                .orElse(orderValue.add(deliveryFee).setScale(2, RoundingMode.HALF_UP));

        return DriverShiftOrderEarningDto.builder()
                .orderId(order.getId())
                .deliveryId(delivery.getId())
                .pickUpLocation(Optional.ofNullable(restaurant).map(Restaurant::getAddress).orElse(null))
                .deliveryLocation(resolveDeliveryLocation(order))
                .orderTotal(orderTotal)
                .driverEarningFromOrder(calculateDriverShareForOrder(orderValue, deliveryFee))
                .deliveryFee(deliveryFee)
                .restaurantName(Optional.ofNullable(restaurant).map(Restaurant::getName).orElse(null))
                .orderItemsCount(itemsCount)
                .orderAcceptedAt(formatTime(delivery.getAssignedTime()))
                .orderDeliveredAt(formatTime(delivery.getDeliveredTime()))
                .build();
    }

    private Long resolveOrderId(Delivery delivery) {
        return Optional.ofNullable(delivery)
                .map(Delivery::getOrder)
                .map(Order::getId)
                .orElse(null);
    }

    private Map<Long, OrderEarningAggregate> loadOrderAggregates(Collection<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<Long> orderIds = deliveries.stream()
                .map(this::resolveOrderId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (orderIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrder_IdIn(orderIds);
        Map<Long, List<OrderItem>> itemsByOrder = new HashMap<>();
        for (OrderItem item : orderItems) {
            Long orderId = Optional.ofNullable(item)
                    .map(OrderItem::getOrder)
                    .map(Order::getId)
                    .orElse(null);
            if (orderId == null) {
                continue;
            }
            itemsByOrder.computeIfAbsent(orderId, ignored -> new ArrayList<>()).add(item);
        }

        Map<Long, OrderEarningAggregate> aggregates = new HashMap<>();
        for (Map.Entry<Long, List<OrderItem>> entry : itemsByOrder.entrySet()) {
            BigDecimal total = OrderPricingCalculator.calculateTotal(entry.getValue());
            int itemCount = entry.getValue().stream()
                    .map(OrderItem::getQuantity)
                    .reduce(0, Integer::sum);
            aggregates.put(entry.getKey(), new OrderEarningAggregate(total, itemCount));
        }

        return aggregates;
    }

    private record OrderEarningAggregate(BigDecimal orderValue, int itemCount) {
    }

    private BigDecimal calculateDriverShareForOrder(BigDecimal orderValue, BigDecimal deliveryFee) {
        BigDecimal safeOrderValue = Optional.ofNullable(orderValue).orElse(ZERO_AMOUNT);
        BigDecimal safeDeliveryFee = Optional.ofNullable(deliveryFee).orElse(ZERO_AMOUNT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal driverCommission = safeOrderValue.multiply(DEFAULT_DRIVER_COMMISSION_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        return driverCommission.add(safeDeliveryFee).setScale(2, RoundingMode.HALF_UP);
    }

    private String resolveDeliveryLocation(Order order) {
        if (order == null) {
            return null;
        }
        if (order.getDeliveryAddress() != null && !order.getDeliveryAddress().isBlank()) {
            return order.getDeliveryAddress();
        }
        return Optional.ofNullable(order.getSavedAddress())
                .map(SavedAddress::getFormattedAddress)
                .orElse(null);
    }

    private LocalDateTime resolveShiftEnd(DriverShift shift) {
        if (shift == null) {
            return null;
        }
        if (shift.getEndedAt() != null) {
            return shift.getEndedAt();
        }
        return shift.getFinishableAt();
    }

    private LocalDateTime resolveDeliverySortTime(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        if (delivery.getDeliveredTime() != null) {
            return delivery.getDeliveredTime();
        }
        if (delivery.getPickupTime() != null) {
            return delivery.getPickupTime();
        }
        return delivery.getAssignedTime();
    }

    private String formatTime(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.format(TIME_FORMATTER);
    }

    private String formatDate(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.format(DATE_FORMATTER);
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
        BigDecimal sum;
        if (start == null && end == null) {
            sum = driverShiftBalanceRepository.sumDriverShareByDriverId(driverId);
        } else if (start != null && end == null) {
            sum = driverShiftBalanceRepository
                    .sumDriverShareByDriverIdAndStartedAtGreaterThanEqual(driverId, start);
        } else if (start == null) {
            sum = driverShiftBalanceRepository
                    .sumDriverShareByDriverIdAndStartedAtLessThan(driverId, end);
        } else {
            sum = driverShiftBalanceRepository
                    .sumDriverShareByDriverIdAndStartedAtBetween(driverId, start, end);
        }
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

        BigDecimal itemsTotal = OrderPricingCalculator.calculateTotal(order);
        if (itemsTotal.compareTo(BigDecimal.ZERO) <= 0) {
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
        BigDecimal commissionRate = Optional.ofNullable(order.getRestaurant())
                .map(Restaurant::getCommissionRate)
                .orElse(null);
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee()).orElse(BigDecimal.ZERO);

        balance.recordOrder(itemsTotal, commissionRate, deliveryFee);
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
