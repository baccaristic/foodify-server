# Migration Phase 2 – Platform Foundations

This phase introduces the shared infrastructure capabilities that future microservices will rely on. The monolith now boots with Spring Cloud Config, Eureka service discovery, and OpenTelemetry-friendly observability so the same runtime baseline can be reused when modules are extracted.

## Highlights

- Added Spring Cloud dependencies (Config Client, LoadBalancer, Eureka) and Micrometer OpenTelemetry exporters to the Gradle build, alongside Actuator for health/metrics endpoints.
- Declared core configuration in `application.yml` for Config Server import, Eureka registration, management endpoints, and OTLP tracing export.
- Provided load-balanced `RestTemplate` and `WebClient` beans to route outbound calls through service discovery once services are split out.
- Centralised observation metadata via a Micrometer `ObservationFilter`, tagging telemetry with the service name for downstream collectors.

## Next Steps

- Stand up dedicated Config Server and Eureka instances; externalise credentials and secrets into the config repository.
- Introduce Spring Cloud Gateway in front of the monolith to validate routing rules before services are separated.
- Define OpenTelemetry collector deployment manifests and ship traces/metrics to the central observability stack.
- Replace direct repository configuration with Config Server managed properties and secrets.
