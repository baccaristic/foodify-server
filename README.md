# Foodify Microservices Platform

Foodify has fully transitioned to a microservices architecture. The legacy monolithic
application has been removed and the codebase now hosts independent Spring Boot
services that can be deployed and scaled separately.

## Services

The following services live in this repository:

- **Identity Service** (`services/identity-service`) &mdash; handles authentication,
  authorization, and user profile management.
- **Catalog Service** (`services/catalog-service`) &mdash; serves restaurant,
  menu, availability, and pricing data consumed by downstream workloads such as
  ordering and discovery experiences.

Each service is a standalone Gradle project with its own `settings.gradle`. Use
Gradle's `-p` flag (or change directory into the service) when running build
and runtime tasks.

## Running locally

1. Start required infrastructure such as PostgreSQL, Kafka, Redis, and Eureka
   using the provided Docker Compose profiles:

   ```bash
   docker compose --profile platform up -d
   ```

   This provisions the shared dependencies used during development. You can
   start a subset (for example just PostgreSQL) by omitting the `--profile`
   flag.

2. Launch the desired service. For example, to start the identity service on
   port `8080`:

   ```bash
   ./gradlew -p services/identity-service bootRun
   ```

   Start the catalog service similarly:

   ```bash
   ./gradlew -p services/catalog-service bootRun
   ```

3. Optional: build container images for distribution:

   ```bash
   ./gradlew -p services/identity-service bootJar
   docker build -t foodify/identity-service:latest services/identity-service

   ./gradlew -p services/catalog-service bootJar
   docker build -t foodify/catalog-service:latest services/catalog-service
   ```

Discovery registration is disabled by default to keep the binaries self-contained.
Enable it by exporting the `SPRING_CLOUD_DISCOVERY_ENABLED`,
`EUREKA_CLIENT_ENABLED`, and related flags documented in each service's
`application.yml` before launching the service.

## Tooling

- **Gradle Wrapper** &mdash; shared across services for consistent builds.
- **Docker Compose** &mdash; provisions local infrastructure dependencies.
- **OpenAPI/Swagger** &mdash; each service exposes its own API documentation once
  running.

## Next steps

The order command service is being extracted in a dedicated repository. Once
completed, it will integrate with these services via REST, messaging, and
outbox-driven workflows.
