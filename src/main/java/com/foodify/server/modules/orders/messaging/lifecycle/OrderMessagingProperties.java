package com.foodify.server.modules.orders.messaging.lifecycle;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging.orders")
public record OrderMessagingProperties(String lifecycleTopic, OutboxProperties outbox) {

    public static final String DEFAULT_LIFECYCLE_TOPIC = "orders.lifecycle";

    public OrderMessagingProperties {
        if (lifecycleTopic == null || lifecycleTopic.isBlank()) {
            lifecycleTopic = DEFAULT_LIFECYCLE_TOPIC;
        }
        if (outbox == null) {
            outbox = OutboxProperties.disabled();
        }
    }

    public record OutboxProperties(boolean enabled, long pollInterval, long retryDelay, int batchSize) {

        private static final long DEFAULT_POLL_INTERVAL = 15_000L;
        private static final long DEFAULT_RETRY_DELAY = 60_000L;
        private static final int DEFAULT_BATCH_SIZE = 50;

        public static OutboxProperties disabled() {
            return new OutboxProperties(false, DEFAULT_POLL_INTERVAL, DEFAULT_RETRY_DELAY, DEFAULT_BATCH_SIZE);
        }

        public OutboxProperties {
            if (pollInterval <= 0) {
                pollInterval = DEFAULT_POLL_INTERVAL;
            }
            if (retryDelay <= 0) {
                retryDelay = DEFAULT_RETRY_DELAY;
            }
            if (batchSize <= 0) {
                batchSize = DEFAULT_BATCH_SIZE;
            }
        }
    }
}
