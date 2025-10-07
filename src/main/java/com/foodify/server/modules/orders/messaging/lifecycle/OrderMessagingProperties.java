package com.foodify.server.modules.orders.messaging.lifecycle;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging.orders")
public record OrderMessagingProperties(String lifecycleTopic,
                                       OutboxProperties outbox,
                                       BackfillProperties backfill) {

    public static final String DEFAULT_LIFECYCLE_TOPIC = "orders.lifecycle";

    public OrderMessagingProperties {
        if (lifecycleTopic == null || lifecycleTopic.isBlank()) {
            lifecycleTopic = DEFAULT_LIFECYCLE_TOPIC;
        }
        outbox = outbox == null ? OutboxProperties.disabled() : outbox;
        backfill = backfill == null ? BackfillProperties.disabled() : backfill;
    }

    public record OutboxProperties(boolean enabled,
                                   Boolean dispatcherEnabledFlag,
                                   long pollInterval,
                                   long retryDelay,
                                   int batchSize) {

        private static final long DEFAULT_POLL_INTERVAL = 15_000L;
        private static final long DEFAULT_RETRY_DELAY = 60_000L;
        private static final int DEFAULT_BATCH_SIZE = 50;

        public static OutboxProperties disabled() {
            return new OutboxProperties(false, Boolean.FALSE, DEFAULT_POLL_INTERVAL, DEFAULT_RETRY_DELAY, DEFAULT_BATCH_SIZE);
        }

        public OutboxProperties {
            if (!enabled) {
                dispatcherEnabledFlag = Boolean.FALSE;
            } else if (dispatcherEnabledFlag == null) {
                dispatcherEnabledFlag = Boolean.TRUE;
            }
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

        public boolean dispatcherEnabled() {
            return enabled && Boolean.TRUE.equals(dispatcherEnabledFlag);
        }
    }

    public record BackfillProperties(boolean enabled,
                                     int chunkSize,
                                     boolean dryRun,
                                     String actor) {

        private static final int DEFAULT_CHUNK_SIZE = 200;
        private static final String DEFAULT_ACTOR = "orders-outbox-backfill";

        public static BackfillProperties disabled() {
            return new BackfillProperties(false, DEFAULT_CHUNK_SIZE, false, DEFAULT_ACTOR);
        }

        public BackfillProperties {
            if (chunkSize <= 0) {
                chunkSize = DEFAULT_CHUNK_SIZE;
            }
            if (actor == null || actor.isBlank()) {
                actor = DEFAULT_ACTOR;
            }
        }
    }
}
