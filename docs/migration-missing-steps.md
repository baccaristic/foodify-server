# Remaining Migration Steps to a Fully Functional Microservice Platform

This checklist expands on the completed migration phases and captures the remaining work required to operate Foodify as a production-ready microservice ecosystem. It is organised by capability so teams can prioritise and track progress towards the target architecture.

## 1. Domain Service Extraction
- **Identity Service** *(partially addressed)*: Authentication flows now route through an `IdentityAuthService` facade with remote proxying, dedicated token secrets, and optional schema migrations. Complete the extraction by moving persistence to the standalone service, exposing OIDC-compliant contracts, and migrating dependent modules (phone signup, notifications) to consume the remote APIs/events.
- **Catalog Service**: Split restaurant, menu, and availability management into an independent service with its own search index preparation and cache warming flows.
- **Order Service**: Convert the monolithic orders module into a standalone command service exposing REST/gRPC endpoints, retaining the lifecycle publisher and outbox pattern as the canonical integration surface.
- **Payments & Pricing Services**: Move payment authorisation, refund workflows, and pricing computations out of the order service. Adopt Sagas for order → payment → logistics orchestration.
- **Logistics & Notifications**: Detach courier assignment, ETA updates, and outbound messaging (email/SMS/push) into specialised services subscribed to lifecycle events.

## 2. Data Management & Persistence
- Provision isolated PostgreSQL instances or schemas per service with Flyway/Liquibase pipelines aligned to each codebase.
- Implement CDC or event-sourcing strategies to replicate historical data from the monolith into the new services during the strangler rollout.
- Finalise entity-to-contract transformations so no service shares JPA entities; instead, rely on versioned DTO/Avro/protobuf schemas.
- Establish data retention, archival, and GDPR/PII deletion procedures per bounded context.

## 3. Event Backbone Hardening
- Promote Kafka from optional to required infrastructure with IaC-managed clusters and per-topic ACLs.
- Adopt schema registry governance and backward-compatible evolution rules for lifecycle and integration events.
- Implement dead-letter queues, replay tooling, and observability dashboards for consumer lag, error rates, and throughput.
- Expand the outbox pattern to other domains (payments, logistics) and introduce an outbox-to-Kafka forwarder service for horizontal scaling.

## 4. API Gateway & BFF Layer
- Deploy Spring Cloud Gateway (or an equivalent API gateway) as the single ingress point with OIDC validation, rate limiting, and routing to each microservice.
- Build dedicated BFFs for Web, Mobile, and Partner experiences to aggregate data and tailor response contracts while shielding clients from service churn.
- Introduce contract testing between BFFs and downstream services to prevent breaking changes from propagating to edge channels.

## 5. Observability & Resilience
- Roll out OpenTelemetry collectors, Grafana/Tempo (or Jaeger), and centralised logging stacks in all environments.
- Configure service-level SLOs, alerting rules, and runbooks for the outbox processor, Kafka consumers, and Redis projections.
- Add chaos/resilience testing (fault injection, load tests) to validate behaviour under degraded dependencies.

## 6. Deployment & DevOps
- Extract services into independent repositories or Gradle builds with CI/CD pipelines producing versioned Docker images and Helm charts.
- Define Kubernetes namespaces, network policies, service meshes (if required), and secrets management via Vault/External Secrets.
- Automate blue/green or canary deployments with progressive delivery tooling (Argo Rollouts/Flagger) to de-risk releases.
- Provide developer bootstrap scripts that spin up the gateway, BFFs, and downstream services using Docker Compose or Tilt.

## 7. Testing & Quality Gates
- Introduce consumer-driven contract tests for REST/gRPC and Kafka to guarantee compatibility during incremental releases.
- Expand end-to-end integration suites that simulate complete order lifecycles across services.
- Enforce performance and load testing thresholds for order placement, payment processing, and courier updates.

## 8. Governance & Security
- Centralise configuration management with Spring Cloud Config + Vault integration, applying secret rotation policies per service.
- Implement RBAC, audit logging, and tenant isolation strategies across identity, order, and payment services.
- Establish architectural governance—design reviews, ADRs, and dependency baselines—to maintain consistency as teams proliferate.

---

Tracking these items alongside the completed phases ensures Foodify transitions from a modularised monolith to a resilient, scalable microservice platform aligned with the target blueprint.
