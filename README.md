# Foodify Platform Services

Foodify has been decomposed into independent Spring Boot microservices. Each service ships with
its own Gradle build, Dockerfile, and `application.yml` so they can run locally or in
containerized environments without relying on the legacy monolith.

## Service Overview

| Service | Path | Description |
| --- | --- | --- |
| Discovery | `services/discovery-service` | Eureka server used for service registration and discovery. |
| Identity | `services/identity-service` | Handles authentication, registration, and JWT management. |
| Catalog | `services/catalog-service` | Exposes restaurant, menu, availability, and pricing APIs. |
| Orders | `services/orders-service` | Manages the customer order lifecycle, outbox, and read models. |
| Delivery | `services/delivery-service` | Tracks driver assignments, ETA calculations, and logistics. |
| Notifications | `services/notifications-service` | Manages push notifications and WebSocket updates. |

Shared observability (OpenTelemetry), messaging (Kafka), and cache (Redis) configuration has been
copied into each service so that they can opt-in to the required infrastructure individually.

## Local Development

1. Launch infrastructure dependencies (PostgreSQL, Redis, Kafka, Eureka) via Docker Compose:
   ```bash
   docker compose --profile platform up -d
   ```
   The compose profile exposes the environment variables expected by each service's `application.yml`.
2. Start any subset of services with Gradle. For example:
   ```bash
   ./gradlew -p services/discovery-service bootRun
   ./gradlew -p services/identity-service bootRun
   ./gradlew -p services/catalog-service bootRun
   ./gradlew -p services/orders-service bootRun
   ./gradlew -p services/delivery-service bootRun
   ./gradlew -p services/notifications-service bootRun
   ```
3. Build container images if desired:
   ```bash
   ./gradlew -p services/orders-service bootJar
   docker build -t foodify/orders-service:latest services/orders-service
   ```

Each service exposes Swagger/OpenAPI documentation under `/swagger-ui/index.html` once running.

## Configuration Highlights

- **Discovery:** Toggle registration via `SPRING_CLOUD_DISCOVERY_ENABLED` / `EUREKA_CLIENT_ENABLED`.
- **Database:** Every service defaults to an in-memory H2 database. Override `spring.datasource.*`
  to point at PostgreSQL when running with Docker Compose.
- **Messaging:** Orders and notifications load Kafka/Redis properties from their respective
  `application.yml` files. Use the `platform` Compose profile to provision the brokers locally.
- **Tracing:** Micrometer OTLP exporters are pre-configured but disabled until `OTEL_EXPORTER_OTLP_ENDPOINT`
  is provided.

## Testing

Run the service-specific test suites via Gradle. For example:
```bash
./gradlew -p services/orders-service test --console=plain
```
(Gradle will attempt to provision the Java 17 toolchain as required.)
