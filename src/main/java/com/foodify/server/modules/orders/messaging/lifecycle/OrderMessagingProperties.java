package com.foodify.server.modules.orders.messaging.lifecycle;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging.orders")
public record OrderMessagingProperties(String lifecycleTopic) {

    public static final String DEFAULT_LIFECYCLE_TOPIC = "orders.lifecycle";

    public OrderMessagingProperties {
        if (lifecycleTopic == null || lifecycleTopic.isBlank()) {
            lifecycleTopic = DEFAULT_LIFECYCLE_TOPIC;
        }
    }
}
