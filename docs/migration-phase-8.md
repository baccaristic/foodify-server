# Migration Phase 8 – Logistics-Enriched Order Lifecycle Events

This phase augments the order lifecycle contract and read model with logistics data
so downstream services can react to driver assignments and delivery milestones without
querying the orders database. By embedding delivery snapshots into the Kafka/outbox
payloads, the eventual logistics microservice receives the same context that the
monolith currently keeps in-process.

## Highlights

- Extended the `OrderLifecycleMessage` contract with a `logistics` snapshot containing
  assigned and pending driver identifiers, contact details, ETA metadata, and pickup
  tokens for partner integrations.
- Updated the lifecycle message factory to map delivery entities and convert pickup /
  drop-off timestamps into transport-friendly instants, ensuring every published
  message carries logistics state alongside pricing and status changes.
- Enhanced the Redis-backed order tracking projection to persist logistics details so
  BFFs can show courier information as soon as drivers are assigned or delivery times
  change.

## Next Steps

- Propagate logistics metrics (driver assignment latency, pickup/delivery durations)
  into the observability stack using the enriched read model data.
- Publish AsyncAPI/Avro schemas for the extended lifecycle payload to coordinate with
  the upcoming logistics and notifications services.
- Backfill historical orders with logistics snapshots before enabling the consumer in
  production to avoid null driver information in cached tracking views.
