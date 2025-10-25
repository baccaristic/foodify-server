package com.foodify.server.modules.orders.messaging;

import com.foodify.server.modules.orders.application.CustomerOrderService;
import com.foodify.server.modules.orders.dto.OrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final CustomerOrderService customerOrderService;

    @KafkaListener(topics = "orders", groupId = "order_group")
    @Transactional
    public void handleOrder(OrderRequest request) {
        customerOrderService.placeOrder(request);
    }
}
