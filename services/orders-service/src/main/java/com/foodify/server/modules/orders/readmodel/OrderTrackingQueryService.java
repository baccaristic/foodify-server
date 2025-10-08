package com.foodify.server.modules.orders.readmodel;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderTrackingQueryService {

    private final OrderTrackingViewRepository repository;

    public Optional<OrderTrackingView> find(Long orderId) {
        return repository.find(orderId);
    }
}
