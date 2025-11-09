# Foodify Server

## Swagger / OpenAPI Playground

This project now exposes interactive API documentation powered by [Swagger UI](https://swagger.io/tools/swagger-ui/) through Springdoc OpenAPI.

### How to run locally
1. Start the infrastructure services (PostgreSQL, Kafka, Redis) via Docker Compose:
   ```bash
   docker compose up -d
   ```
   The app expects a PostgreSQL database named `foodify` with the username and password `foodify`. These defaults can be overridden through the `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` environment variables.
2. Install dependencies and start the application:
   ```bash
   ./gradlew bootRun
   ```
3. Once the server is running, open your browser at [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html).

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

## Admin Push Notification APIs

Admins can send push notifications through the following endpoints:

- `POST /api/admin/notifications/push/bulk` - Send notifications to all users or filter by role (CLIENT, DRIVER, RESTAURANT_ADMIN)
- `POST /api/admin/notifications/push/user/{userId}` - Send notification to a specific user

Both endpoints support:
- Customizable title and message body
- Optional data payload (JSON object with custom key-value pairs)
- Detailed response with success/failure counts

These endpoints require ADMIN role authentication.

## Client nearby restaurant APIs

The consolidated `/api/client/nearby` endpoint has been split into focused endpoints so clients can
load targeted sections independently:

- `GET /api/client/nearby/top` – returns the five closest restaurants ranked by overall score.
- `GET /api/client/nearby/favorites` – returns up to five nearby restaurants drawn from the client’s
  saved favorites.
- `GET /api/client/nearby/orders` – returns nearby restaurants the client has ordered from most
  recently ("order again").
- `GET /api/client/nearby/restaurants` – returns the complete nearby restaurant listing with
  pagination support.

Each response reuses `RestaurantDisplayDto` and includes delivery-fee and favorite flags so the
mobile app can render consistent cards across the different sections.

### Setting environment variables with Docker Compose

Docker Compose automatically reads a `.env` file that lives next to `docker-compose.yml`. Populate it with any overrides you need before starting the stack:

```env
KONNECT_SANDBOX_API_KEY=your-sandbox-key
KONNECT_SANDBOX_WALLET_ID=wallet-id
KONNECT_SANDBOX_WEBHOOK_URL=http://host.docker.internal:8081/api/payments/konnect/webhook
```

Alternatively, pass a different env file when booting the services:

```bash
docker compose --env-file ops/dev.env up -d
```

You can also export variables in your shell (`export DATABASE_URL=...`) before running `docker compose up`. Compose propagates those values into each service's `environment` block, including the application container.

## Performance Testing

Foodify Server includes comprehensive performance testing infrastructure using Gatling to ensure the system can handle production load.

### Quick Start

Run a quick smoke test (5 minutes, 50 users):
```bash
./performance-test.sh smoke
```

Run the full load test (30 minutes, 200 users):
```bash
./performance-test.sh load
```

### Available Performance Tests

| Test Type | Purpose | Duration | Users | Command |
|-----------|---------|----------|-------|---------|
| **Smoke** | Quick validation | 5 min | 50 | `./performance-test.sh smoke` |
| **Load** | Normal peak load | 30 min | 200 | `./performance-test.sh load` |
| **Stress** | Find breaking point | 20 min | up to 2000 | `./performance-test.sh stress` |
| **Endurance** | Memory leak detection | 2-24 hours | 100 | `./performance-test.sh endurance` |
| **Spike** | Sudden traffic surges | 15 min | spikes to 1200 | `./performance-test.sh spike` |

### Using Gradle Directly

```bash
# Run specific tests with Gradle
./gradlew runLoadTest
./gradlew runStressTest
./gradlew runEnduranceTest
./gradlew runSpikeTest

# With custom configuration
./gradlew runLoadTest -Dperf.baseUrl=http://staging:8081 -Dperf.normalUsers=500
```

### Performance Test Environment

For realistic performance testing, use the performance-optimized Docker Compose setup:

```bash
# Start performance testing environment
docker compose -f docker-compose.performance.yml up -d

# The app will be available at http://localhost:8082
# Run tests against it
./performance-test.sh load --url http://localhost:8082

# View metrics in Prometheus: http://localhost:9090
# View dashboards in Grafana: http://localhost:3000 (admin/admin)
```

### Key Performance Metrics

The tests validate:
- **Response Time**: p95 < 500ms, p99 < 1000ms
- **Throughput**: 100-500+ requests/second
- **Error Rate**: < 0.1% under normal load
- **Resource Usage**: CPU < 70%, Memory stable
- **Database Performance**: Query time < 50ms average

### Documentation

- **[Detailed Performance Testing Guide](docs/PERFORMANCE_TESTING.md)** - Complete guide with metrics, best practices, and optimization strategies
- **[Gatling Tests README](src/gatling/README.md)** - Quick reference for running and configuring tests

### Viewing Results

After running tests, Gatling generates detailed HTML reports:

```bash
# Reports are in: build/reports/gatling/
# Open the latest report
open build/reports/gatling/$(ls -t build/reports/gatling/ | head -1)/index.html
```

### CI/CD Integration

Performance smoke tests can be integrated into CI pipelines:

```yaml
performance-test:
  script:
    - docker compose up -d
    - ./performance-test.sh smoke
```
