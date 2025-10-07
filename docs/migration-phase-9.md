# Migration Phase 9 – Local-First Platform Toggles & Outbox Schema

This phase focuses on making the new platform capabilities safe to run in local and
ad-hoc environments where Kafka, Eureka, Redis, or OpenTelemetry collectors are not
available. It also introduces a schema-backed order lifecycle outbox so that future
service extractions can rely on consistent persistence instead of Hibernate DDL.

## Highlights

- Disabled discovery client registration, tracing export, order lifecycle outbox, and
  Redis tracking projections by default, while keeping environment variables to opt-in
  when the supporting infrastructure is present.
- Guarded the load-balanced client beans behind `spring.cloud.discovery.enabled` so the
  monolith can start without loading discovery adapters when Eureka is offline.
- Added a Flyway migration that creates the `order_lifecycle_outbox` table and covering
  index, ensuring the reliable messaging pipeline works consistently across
  environments once it is enabled.

## Next Steps

- Provide docker-compose services for Eureka, Kafka, Redis, and the OTLP collector so
  engineers can toggle the integrations on with a single profile.
- Extend the deployment manifests to set the new opt-in environment variables when the
  platform services are provisioned in staging and production clusters.
- Backfill the outbox table with in-flight order lifecycle events before enabling the
  dispatcher in production to avoid dropping state transitions.
