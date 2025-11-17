package com.foodify.server.modules.admin.order.application;

import com.foodify.server.modules.admin.order.dto.*;
import com.foodify.server.modules.admin.order.repository.AdminOrderRepository;
import com.foodify.server.modules.admin.order.repository.OrderSpecifications;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final AdminOrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<OrderDto> getDriverList(String query, List<OrderStatus> status, Long restaurantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepository.findAll(
                OrderSpecifications.withFilters(query, status, restaurantId),
                pageable
        );

        return ordersPage.map(order -> OrderDto.builder()
                .id(order.getId())
                .restaurantName(order.getRestaurant().getName())
                .status(order.getStatus())
                .amount(order.getTotal())
                .orderDate(order.getOrderTime())
                .build());
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
    public PendingOrdersStatsDto getPendingOrdersCount() {
        long count = orderRepository.countByStatus(OrderStatus.PENDING);
        return PendingOrdersStatsDto.builder()
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
}
