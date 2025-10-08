# Migration Phase 4 – Reliable Order Event Delivery

This phase introduces a transactional outbox for order lifecycle messages so that downstream
microservices can rely on guaranteed delivery even when Kafka is unavailable. Messages are now
persisted alongside order changes and replayed by a scheduled processor that hands off payloads to
the configured transport (Kafka or a no-op fallback).

## Highlights

- Added an `order_lifecycle_outbox` table, repository, and service that serialises
  `OrderLifecycleMessage` payloads and tracks delivery attempts.
- Wrapped the existing messaging pipeline with an outbox-aware publisher that stores events before
  they are sent, enabling retries and visibility into dispatch history.
- Created a scheduled processor that scans for pending outbox entries and replays them using the
  active transport sender, deferring retries with exponential-friendly backoff controls.
- Extended messaging configuration with transport senders and nested outbox properties that can be
  tuned via `application.yml`.

## Next Steps

- Adopt the outbox pattern for payments and logistics events to align downstream saga steps with the
  new reliability guarantees.
- Emit metrics and traces from the outbox processor (e.g. dispatched/failed counts) for operational
  dashboards.
- Introduce administrative tooling to inspect and manually replay stuck outbox entries during the
  migration.
