# Migration Phase 12 – Order Tracking API & Local Platform Hardening

Phase 12 exposes the Redis-backed order tracking projection through the public API and
updates the local platform profile so the compose stack remains functional after the
Bitnami image deprecation.

## Highlights

- Introduced an opt-in `OrderTrackingController` that serves
  `GET /api/orders/{orderId}/tracking` for authenticated clients whenever the Redis read
  model is enabled. The controller verifies ownership before returning projections so the
  read path remains multi-tenant safe.
- Documented the tracking endpoint in the workflow guide to make it easy to validate the
  new projection without touching Redis directly.
- Swapped the deprecated `bitnami/eureka` image in the `platform` compose profile for the
  maintained `springcloud/spring-cloud-netflix-eureka-server` container, keeping local
  discovery parity with future microservices.

## Next Steps

- Surface the tracking projection through WebSocket/BFF layers once clients migrate
  away from monolith polling.
- Expand contract and consumer-driven tests that cover the new REST response payload to
  lock in compatibility guarantees for downstream channels.
- Package a lightweight Eureka server manifest for local Kubernetes to mirror the compose
  defaults during cluster experiments.
