package com.foodify.server.modules.orders.messaging.lifecycle;

public interface OrderLifecycleMessageSender {

    void send(OrderLifecycleMessage message);
}
