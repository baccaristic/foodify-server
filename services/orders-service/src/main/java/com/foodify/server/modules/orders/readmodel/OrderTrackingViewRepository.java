package com.foodify.server.modules.orders.readmodel;

import java.util.Optional;

public interface OrderTrackingViewRepository {

    Optional<OrderTrackingView> find(Long orderId);

    void save(OrderTrackingView view);

    void delete(Long orderId);
}
