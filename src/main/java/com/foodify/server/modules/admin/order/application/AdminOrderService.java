package com.foodify.server.modules.admin.order.application;

import com.foodify.server.modules.admin.order.dto.*;
import com.foodify.server.modules.admin.order.repository.AdminOrderRepository;
import com.foodify.server.modules.admin.order.repository.OrderSpecifications;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.domain.OrderStatusHistory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final AdminOrderRepository orderRepository;

    public Page<OrderDto> getDriverList(String query, List<OrderStatus> status, Long restaurantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepository.findAll(
                OrderSpecifications.withFilters(query, status, restaurantId),
                pageable
        );

        return ordersPage.map(order -> {
            Duration prepTime = getPreparationTime(order);

            return OrderDto.builder()
                    .id(order.getId())
                    .restaurantId(order.getRestaurant().getId())
                    .restaurantName(order.getRestaurant().getName())
                    .clientId(order.getClient().getId())
                    .clientName(order.getClient().getName())
                    .status(order.getStatus())
                    .amount(order.getTotal())
                    .orderDate(order.getOrderTime())
                    .preparationTime(prepTime != null && restaurantId != null ? prepTime.toMinutes() : null)
                    .build();
        });
    }

    private Duration getPreparationTime(Order order) {
        LocalDateTime acceptedTime = order.getStatusHistory().stream()
                .filter(h -> h.getNewStatus() == OrderStatus.ACCEPTED)
                .map(OrderStatusHistory::getChangedAt)
                .findFirst()
                .orElse(null);

        LocalDateTime readyTime = order.getStatusHistory().stream()
                .filter(h -> h.getNewStatus() == OrderStatus.READY_FOR_PICK_UP)
                .map(OrderStatusHistory::getChangedAt)
                .findFirst()
                .orElse(null);

        if (acceptedTime != null && readyTime != null) {
            return Duration.between(acceptedTime, readyTime);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public OrderStatsDto getTodayOrdersStats() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime yesterdayStart = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime yesterdayEnd = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);

        long todayCount = orderRepository.countOrdersBetween(todayStart, todayEnd);
        long yesterdayCount = orderRepository.countOrdersBetween(yesterdayStart, yesterdayEnd);

        double percentageChange = 0.0;
        if (yesterdayCount > 0) {
            percentageChange = ((double) (todayCount - yesterdayCount) / yesterdayCount) * 100;
        } else if (todayCount > 0) {
            percentageChange = 100.0;
        }

        return OrderStatsDto.builder()
                .count(todayCount)
                .percentageChange(Math.round(percentageChange * 100.0) / 100.0)
                .build();
    }

    @Transactional(readOnly = true)
    public OrdersCountDto getPendingOrdersCount() {
        long count = orderRepository.countByStatus(OrderStatus.PENDING);
        return OrdersCountDto.builder()
                .count(count)
                .build();
    }

    @Transactional(readOnly = true)
    public RevenueStatsDto getTodayTotalRevenue() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        BigDecimal revenue = orderRepository.sumTotalRevenueBetween(todayStart, todayEnd);
        return RevenueStatsDto.builder()
                .totalRevenue(revenue)
                .build();
    }

    @Transactional(readOnly = true)
    public ClientDto getClientByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        var client = order.getClient();
        if (client == null) {
            throw new EntityNotFoundException("Client not found for order");
        }
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhoneNumber())
                .address(order.getSavedAddress().getFormattedAddress())
                .build();
    }

    @Transactional(readOnly = true)
    public OrdersCountDto getTotalOrdersByClientId(Long clientId) {
        long count = orderRepository.countByClientId(clientId);
        return OrdersCountDto.builder()
                .count(count)
                .build();
    }

    @Transactional(readOnly = true)
    public ClientLifetimeValueDto getClientLifetimeValue(Long clientId) {
        BigDecimal lifetimeValue = orderRepository.sumTotalByClientId(clientId);
        return ClientLifetimeValueDto.builder()
                .lifetimeValue(lifetimeValue)
                .build();
    }

    @Transactional(readOnly = true)
    public ClientAvgOrderValueDto getClientAvgOrderValue(Long clientId) {
        BigDecimal avgOrderValue = orderRepository.avgTotalByClientId(clientId);
        return ClientAvgOrderValueDto.builder()
                .avgOrderValue(avgOrderValue)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<ClientOrderDetailDto> getOrdersByClientId(Long clientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findOrdersByClientIdWithDetails(clientId, pageable);
    }
}
