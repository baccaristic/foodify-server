package com.foodify.server.modules.orders.messaging.lifecycle.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Slf4j
@RequiredArgsConstructor
public class OrderLifecycleOutboxBackfillRunner implements ApplicationRunner {

    private final OrderLifecycleOutboxBackfillService service;

    @Override
    public void run(ApplicationArguments args) {
        OrderLifecycleOutboxBackfillSummary summary = service.backfillCreatedEvents();
        log.info("Order lifecycle outbox backfill result: processed={}, enqueued={}, skipped={}, dryRun={}",
                summary.processed(), summary.enqueued(), summary.skipped(), summary.dryRun());
    }
}
