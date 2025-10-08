# Foodify Server

## Swagger / OpenAPI Playground

This project now exposes interactive API documentation powered by [Swagger UI](https://swagger.io/tools/swagger-ui/) through Springdoc OpenAPI.

### How to run locally
1. Start the required infrastructure services (PostgreSQL) via Docker Compose:
   ```bash
   docker compose up -d postgres
   ```
   The app expects a PostgreSQL database named `foodify` with the username and password `foodify`. These defaults can be overridden through the `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` environment variables.

   To enable the optional platform integrations (Kafka, Redis, Eureka, and the OTLP collector), launch the compose profile that provisions them:
   ```bash
   docker compose --profile platform up -d
   ```
   This mirrors the environment variables exposed in `application.yml`, allowing you to opt in to discovery, messaging, caching, and tracing locally when needed.
2. Install dependencies and start the application:
   ```bash
   ./gradlew bootRun
   ```
   The Gradle wrapper downloads the Java toolchain automatically. If you prefer to run
   the packaged jar instead, build it with `./gradlew bootJar` and then execute
   `java -jar build/libs/foodify-server-*.jar`.
3. Once the server is running, open your browser at
   [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html).

### Migration feature toggle quickstart

The new messaging, outbox, and projection components are disabled by default so the
monolith can still run without auxiliary services. Use the following environment variables
to opt in as you work through the migration roadmap:

```bash
export APP_ORDERS_OUTBOX_ENABLED=true
export APP_ORDERS_OUTBOX_DISPATCHER_ENABLED=false   # keep the dispatcher off during backfill
export APP_ORDERS_OUTBOX_BACKFILL_ENABLED=true      # seed historical orders into the outbox
export APP_ORDERS_TRACKING_ENABLED=true             # project lifecycle events into Redis
export APP_ORDERS_TRACKING_KAFKA_ENABLED=true       # consume lifecycle events from Kafka
export SPRING_CLOUD_DISCOVERY_ENABLED=true          # register with Eureka (requires compose profile)
export EUREKA_CLIENT_ENABLED=true
export EUREKA_REGISTER_WITH_EUREKA=true
```

Run `./gradlew bootRun` after exporting the variables. The application logs will indicate
which optional components were activated. The order tracking endpoint will still appear in
Swagger UI even when the projection is disabled; in that scenario it responds with
`501 Not Implemented` to indicate that the feature toggle must be enabled before cached
tracking snapshots are available.

### Identity service extraction toggles

Authentication, JWT issuance, and profile workflows now flow through an `IdentityAuthService`
layer so the monolith can either execute the logic locally or proxy to a dedicated identity
microservice. The behaviour is controlled through the `identity.*` settings in
`application.yml`:

- `IDENTITY_SERVICE_MODE` &mdash; defaults to `monolith`. Set to `remote` to forward all
  `/api/auth/**` requests to the external identity service configured by
  `IDENTITY_SERVICE_BASE_URL`.
- `IDENTITY_SERVICE_CONNECT_TIMEOUT` / `IDENTITY_SERVICE_READ_TIMEOUT` &mdash; tune the REST
  client timeouts when proxying.
- `IDENTITY_ACCESS_TOKEN_SECRET` and `IDENTITY_REFRESH_TOKEN_SECRET` &mdash; Base64-encoded keys
  used to sign JWTs when the monolith issues tokens locally. Rotate these in each
  environment; the defaults are provided for local development only.
- `IDENTITY_SCHEMA_MANAGED` &mdash; when `true`, Flyway will apply migrations located in
  `db/identity/migration` to the schema defined by `IDENTITY_SCHEMA_NAME`. This allows the
  identity service to own its database objects independently of the rest of the monolith.

Example local configuration that proxies to a standalone identity service while running the
schema migrations:

```bash
export IDENTITY_SERVICE_MODE=remote
export IDENTITY_SERVICE_BASE_URL=http://localhost:8085
export IDENTITY_SCHEMA_MANAGED=true
./gradlew bootRun
```

When `IDENTITY_SERVICE_MODE=monolith` (the default), the monolith continues to handle user
management in-process, issuing JWTs via the configured secrets so existing clients remain
compatible during the strangler migration.

### Testing the order lifecycle workflow

1. Launch the infrastructure stack with the `platform` profile:
   ```bash
   docker compose --profile platform up -d
   ```
2. Enable the environment variables listed above and start the application. The
   `OrderLifecycleOutboxBackfillRunner` executes at startup when
   `APP_ORDERS_OUTBOX_BACKFILL_ENABLED=true`. Review the log statement to confirm how many
   historical orders were enqueued and whether the run was a dry run.
3. Create a new order through the existing REST API (for example via Swagger UI). The
   order will be written to the outbox, published to Kafka, and projected into Redis.
4. Verify the projection through the new tracking endpoint (requires `ROLE_CLIENT` authentication):
   ```bash
   curl -H "Authorization: Bearer <jwt>" http://localhost:8081/api/orders/<orderId>/tracking
   ```
   The response should include the cached logistics, amounts, and status history that were populated by
   the projection pipeline. A `404` indicates that the projection has not yet materialised or belongs to a
   different client account.
5. Inspect Redis to verify the cached projection:
   ```bash
   docker exec -it redis redis-cli --raw get "orders:tracking:<orderId>"
   ```
6. Once the backfill has been verified, re-enable the dispatcher by exporting
   `APP_ORDERS_OUTBOX_DISPATCHER_ENABLED=true` and restarting the application so the
   outbox processor delivers messages continuously.

### Authenticating requests in Swagger UI
Many endpoints require a JWT access token. You can obtain a token by calling one of the authentication endpoints (for example `POST /api/auth/login`) with valid credentials. After retrieving the `accessToken` from the response:

1. Open the Swagger UI page and press the **Authorize** button in the top-right corner.
2. Select the `bearerAuth` scheme and enter the token in the field using the format:
   ```
   Bearer <your-jwt-token>
   ```
3. Click **Authorize** and then **Close**. All subsequent requests sent from Swagger UI will include the JWT in the `Authorization` header, allowing you to exercise protected endpoints.

### Notes
- The OpenAPI specification is available at `/v3/api-docs` (JSON) or `/v3/api-docs.yaml` (YAML).
- Swagger UI and the OpenAPI documents are publicly accessible while the rest of the API remains protected by the existing security configuration.
