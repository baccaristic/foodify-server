# Migration Phase 3 – Domain Events & API Contracts

This phase introduces explicit order lifecycle messages so the monolith can coexist with
future microservices listening to Kafka topics. The messages mirror the planned CQRS/Saga
backbone and remove direct module coupling by standardising the payload exposed outside
of the process boundary.

## Highlights

- Added a serialisable `OrderLifecycleMessage` contract (including items, pricing, and delivery context)
  alongside a factory that maps domain aggregates into transport-friendly records.
- Wired the `OrderLifecycleService` to a pluggable `OrderLifecycleMessagePublisher` so that
  order creation and status transitions now emit Kafka events in addition to Spring application events.
- Provisioned Kafka infrastructure beans dedicated to lifecycle messages, with configuration
  properties (`app.messaging.orders.lifecycle-topic`) and a safe no-op fallback when Kafka is unavailable.

## Next Steps

- Extend the same event contract strategy to payments, logistics, and notifications to support
  Saga orchestration across services.
- Publish REST-friendly response DTOs for Orders and Catalog modules to prepare for BFF/API extraction.
- Introduce an outbox/poller mechanism to guarantee delivery when Kafka is unavailable,
  ensuring at-least-once semantics ahead of service decomposition.
