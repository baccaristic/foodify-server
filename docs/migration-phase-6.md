# Migration Phase 6 – Order Tracking Read Model

This phase introduces a Redis-backed order tracking projection that consumes
order lifecycle events and materializes a query-friendly view for web and
mobile BFFs. The projection provides a low-latency read model that survives the
strangler migration while order creation and status changes move to external
services.

## Highlights

- Added an opt-in Redis projection that listens for `OrderLifecycleEvent`
  publications after commit, converts them into enriched `OrderTrackingView`
  records, and stores them with configurable TTL semantics.
- Reused the existing `OrderLifecycleMessageFactory` to ensure the read model
  mirrors the payload emitted on Kafka/outbox transports, keeping item totals
  and delivery context consistent across write and read paths.
- Exposed a repository/query service abstraction so future BFF modules can
  retrieve cached tracking snapshots without depending on Redis APIs directly.

## Next Steps

- Extend the projection to consume Kafka lifecycle events once orders are
  handled out-of-process, ensuring remote services can rebuild their read
  models without direct database access.
- Emit cache invalidation events when orders are archived or deleted so stale
  tracking entries are purged automatically.
- Create HTTP endpoints (or GraphQL resolvers) that expose the tracking view
  for client applications, falling back to the primary database when Redis is
  unavailable.
