package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

public record OrderLifecycleOutboxBackfillSummary(long processed,
                                                  long enqueued,
                                                  long skipped,
                                                  boolean dryRun) {
}
