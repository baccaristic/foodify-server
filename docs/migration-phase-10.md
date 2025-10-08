# Migration Phase 10 – Platform Profiles & Deployment Toggles

Phase 10 closes the operational gaps identified in the previous milestone by making it
simple to opt in to the new platform components locally and by codifying how those
capabilities are activated in cluster deployments.

## Highlights

- Added a `platform` Docker Compose profile that provisions Kafka, Redis, the Eureka
  registry, and an OTLP collector, mirroring the feature flags exposed in the
  application configuration.
- Introduced an OpenTelemetry Collector configuration that accepts OTLP traffic over
  gRPC/HTTP and exports to structured logs for quick inspection during local
  development.
- Supplied a baseline Kubernetes deployment manifest that enables discovery,
  messaging, caching, and tracing via explicit environment variables so staging and
  production environments can activate the new integrations consistently.

## Next Steps

- Backfill historical order lifecycle events into the outbox table before turning on
  the dispatcher in long-lived environments.
- Automate configuration management for the new infrastructure services (config
  server entries, Helm values, Terraform, etc.) to keep environment toggles in sync.
- Expand the deployment manifests with readiness/liveness probes and horizontal
  autoscaling policies tuned for the decomposed workloads.
