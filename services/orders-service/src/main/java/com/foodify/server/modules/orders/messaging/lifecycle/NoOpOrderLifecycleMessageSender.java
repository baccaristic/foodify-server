package com.foodify.server.modules.orders.messaging.lifecycle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpOrderLifecycleMessageSender implements OrderLifecycleMessageSender {

    @Override
    public void send(OrderLifecycleMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("Order lifecycle message suppressed because no sender is configured: {}", message);
        }
    }
}
