# Foodify Server

## Prerequisites

* **Java 21** (the Gradle build is configured to use the Java 21 toolchain)
* **Docker & Docker Compose** for the PostgreSQL, Kafka, and Redis dependencies
* **Node 18+** only if you plan to work on the optional websocket dashboard (not required for the backend build)

## Configuration

Sensitive settings are sourced from environment variables. Copy `.env.example` to `.env` (or export the values in the shell used to run Gradle) and fill in the placeholders with values appropriate for your environment:

| Variable | Description |
| --- | --- |
| `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` | Connection string and credentials for the PostgreSQL instance. |
| `KAFKA_BOOTSTRAP_SERVERS` | Address of your Kafka brokers (defaults to `localhost:9092`). |
| `OAUTH_GOOGLE_CLIENT_ID`, `OAUTH_GOOGLE_CLIENT_SECRET` | Google OAuth credentials used for social login. |
| `GOOGLE_MAPS_API_KEY` | API key with access to the Maps Routes APIs. |
| `FIREBASE_CREDENTIALS_BASE64` | Base64-encoded Firebase service account JSON (preferred). |
| `FIREBASE_CREDENTIALS_PATH` | Alternative file path or resource location to the Firebase service account JSON. |
| `GOOGLE_APPLICATION_CREDENTIALS_JSON` | Fallback raw JSON credentials (used if neither of the above values are provided). |

> **Security note:** Service account files, API keys, and generated uploads are ignored by Git and should never be committed to the repository.

## Running locally

1. Start the infrastructure services (PostgreSQL, Kafka, Redis) via Docker Compose:
   ```bash
   docker compose up -d
   ```
2. Export your environment variables (or rely on `.env` loading via your shell) and build the project:
   ```bash
   ./gradlew build
   ```
3. Launch the application:
   ```bash
   ./gradlew bootRun
   ```
4. Once the server is running, open [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) to explore the API documentation.

### Authenticating requests in Swagger UI

Many endpoints require a JWT access token. You can obtain a token by calling one of the authentication endpoints (for example `POST /api/auth/login`) with valid credentials. After retrieving the `accessToken` from the response:

1. Open the Swagger UI page and press the **Authorize** button in the top-right corner.
2. Select the `bearerAuth` scheme and enter the token in the field using the format:
   ```
   Bearer <your-jwt-token>
   ```
3. Click **Authorize** and then **Close**. All subsequent requests sent from Swagger UI will include the JWT in the `Authorization` header, allowing you to exercise protected endpoints.

### Additional notes

* The OpenAPI specification is available at `/v3/api-docs` (JSON) or `/v3/api-docs.yaml` (YAML).
* Swagger UI and the OpenAPI documents are publicly accessible while the rest of the API remains protected by the existing security configuration.
