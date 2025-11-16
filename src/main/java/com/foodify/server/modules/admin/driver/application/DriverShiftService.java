package com.foodify.server.modules.admin.driver.application;

import com.foodify.server.modules.admin.driver.dto.AdminDriverShiftDto;
import com.foodify.server.modules.admin.driver.dto.AdminShiftDeliveryDto;
import com.foodify.server.modules.admin.driver.repository.AdminShiftRepository;
import com.foodify.server.modules.admin.driver.util.helpers.DriverFinancialCalculator;
import com.foodify.server.modules.delivery.domain.Delivery;
import com.foodify.server.modules.delivery.domain.DeliveryRating;
import com.foodify.server.modules.delivery.domain.DriverShift;
import com.foodify.server.modules.delivery.domain.DriverShiftBalance;
import com.foodify.server.modules.identity.domain.Driver;
import com.foodify.server.modules.orders.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverShiftService {

    private final AdminShiftRepository adminShiftRepository;
    private final DriverFinancialCalculator financialCalculator;

    @Transactional(readOnly = true)
    public Page<AdminDriverShiftDto> getDriverShifts(Long driverId, LocalDateTime date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DriverShift> shiftsPage = adminShiftRepository.findShiftsWithFilters(driverId, date, pageable);
        return shiftsPage.map(this::convertToAdminDriverShiftDto);
    }

    private AdminDriverShiftDto convertToAdminDriverShiftDto(DriverShift shift) {
        Driver driver = shift.getDriver();
        DriverShiftBalance balance = shift.getBalance();

        List<AdminShiftDeliveryDto> deliveryDtos = shift.getDeliveries().stream()
                .map(this::convertToAdminShiftDeliveryDto)
                .toList();

        BigDecimal zeroAmount = financialCalculator.normalize(null);

        return AdminDriverShiftDto.builder()
                .id(shift.getId())
                .driverId(driver != null ? driver.getId() : null)
                .driverName(driver != null ? driver.getName() : null)
                .status(shift.getStatus())
                .startedAt(shift.getStartedAt())
                .finishableAt(shift.getFinishableAt())
                .endedAt(shift.getEndedAt())
                .totalAmount(balance != null ? balance.getTotalAmount() : zeroAmount)
                .driverShare(balance != null ? balance.getDriverShare() : zeroAmount)
                .restaurantShare(balance != null ? balance.getRestaurantShare() : zeroAmount)
                .settled(balance != null && balance.isSettled())
                .settledAt(balance != null ? balance.getSettledAt() : null)
                .deliveries(deliveryDtos)
                .build();
    }

    private AdminShiftDeliveryDto convertToAdminShiftDeliveryDto(Delivery delivery) {
        Order order = delivery.getOrder();
        DeliveryRating rating = delivery.getRating();

        String deliveryAddress = null;
        if (order != null && order.getSavedAddress() != null) {
            deliveryAddress = order.getSavedAddress().getFormattedAddress();
        }

        String restaurantName = null;
        if (order != null && order.getRestaurant() != null) {
            restaurantName = order.getRestaurant().getName();
        }

        return AdminShiftDeliveryDto.builder()
                .id(delivery.getId())
                .orderId(order != null ? order.getId() : null)
                .restaurantName(restaurantName)
                .deliveryAddress(deliveryAddress)
                .deliveryTime(delivery.getDeliveryTime())
                .timeToPickUp(delivery.getTimeToPickUp())
                .assignedTime(delivery.getAssignedTime())
                .pickupTime(delivery.getPickupTime())
                .deliveredTime(delivery.getDeliveredTime())
                .rating(rating != null ? rating.getOverallRating().doubleValue() : null)
                .ratingComment(rating != null ? rating.getComments() : null)
                .build();
    }
}
