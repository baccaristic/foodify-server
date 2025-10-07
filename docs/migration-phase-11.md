# Migration Phase 11 – Outbox Backfill Workflow

Phase 11 introduces an opt-in backfill runner so existing orders can be copied into the
order lifecycle outbox before downstream services consume the new messaging pipeline.

## Highlights

- Added an `OrderLifecycleOutboxBackfillService` that scans historical orders in
  configurable chunks and enqueues `ORDER_CREATED` events into the outbox when they do not
  already exist.
- Exposed a `backfill` configuration block under `app.messaging.orders.outbox` that
  controls the chunk size, dry-run mode, and audit actor used when seeding the outbox.
- Registered an `OrderLifecycleOutboxBackfillRunner` that can be toggled on via
  `APP_ORDERS_OUTBOX_BACKFILL_ENABLED=true`, logging a summary of processed, inserted, and
  skipped orders at startup.

## Next Steps

- Extend the backfill to cover historical status transitions using the
  `OrderStatusHistory` table so downstream consumers receive a complete lifecycle.
- Move the backfill runner into a dedicated one-shot Spring Batch job to support
  multi-instance execution with checkpoints.
- Schedule automated backfill executions in staging environments ahead of enabling the
  dispatcher to verify data parity with the legacy notification pipeline.
