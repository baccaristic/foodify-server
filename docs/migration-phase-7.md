# Migration Phase 7 – Kafka-Driven Order Tracking Synchronization

This phase extends the order tracking read model so it can rebuild itself from the
externalized Kafka lifecycle stream. By enriching the lifecycle payload and wiring a
conditional consumer, downstream BFFs keep their cache synchronized even after the
orders domain moves out of the monolith.

## Highlights

- Added customer and restaurant identity details to the `OrderLifecycleMessage`
  contract so consumers can materialize client-facing views without querying the
  orders database directly.
- Refactored the tracking projection to operate on lifecycle messages, making the
  same projection logic reusable for both transactional events and Kafka deliveries.
- Introduced an opt-in Kafka listener that projects lifecycle messages into Redis
  when `app.orders.tracking.kafka.enabled` is set, preparing the read model for an
  event-driven future state.

## Next Steps

- Enable the Kafka consumer in staging once the lifecycle topic is published by the
  orders service, validating end-to-end synchronization outside the monolith.
- Extend the lifecycle message with courier/logistics snapshots so delivery status
  updates can also flow into the cached tracking view.
- Add contract tests to ensure downstream services continue to deserialize the
  enriched lifecycle payload as the schema evolves.
