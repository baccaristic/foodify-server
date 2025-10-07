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
