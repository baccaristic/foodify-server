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

## Deploying to a VM

The following walkthrough assumes a fresh Ubuntu 22.04 LTS server, but the same concepts apply to any Linux distribution that
can run Java 21 and Docker. Commands prefixed with `#` must be executed as `root` (or with `sudo`).

### 1. Prepare the host

1. Update packages and install required tooling:
   ```bash
   # apt-get update && apt-get install -y unzip curl git docker.io docker-compose-plugin
   ```
2. Install a Java 21 runtime (for example, using the Adoptium binary):
   ```bash
   # curl -L -o /tmp/temurin21.tar.gz https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.4%2B7/OpenJDK21U-jre_x64_linux_hotspot_21.0.4_7.tar.gz
   # mkdir -p /opt/java
   # tar -xzf /tmp/temurin21.tar.gz -C /opt/java
   # update-alternatives --install /usr/bin/java java /opt/java/jdk-21*/bin/java 1
   ```
   > You can swap in any other Java 21 distribution you prefer; ensure `java -version` reports 21.x afterwards.
3. Create a dedicated system user and directory structure for the application (optional but recommended):
   ```bash
   # useradd --system --create-home --shell /usr/sbin/nologin foodify
   # mkdir -p /opt/foodify
   # chown foodify:foodify /opt/foodify
   ```

### 2. Retrieve the application code

1. Switch to the application user:
   ```bash
   # su - foodify
   ```
2. Clone the repository and enter it:
   ```bash
   $ git clone https://github.com/<your-org>/foodify-server.git /opt/foodify/app
   $ cd /opt/foodify/app
   ```
3. Copy the environment template and fill in all secrets and external service settings:
   ```bash
   $ cp .env.example .env
   $ nano .env  # or your preferred editor
   ```
   Required values include database credentials, Kafka brokers, Redis host, Google OAuth/Maps keys, and Firebase credentials.
   When running the companion Docker services locally on the VM, a typical configuration looks like:
   ```env
   DATABASE_URL=jdbc:postgresql://localhost:5432/foodify
   DATABASE_USERNAME=foodify
   DATABASE_PASSWORD=foodify
   KAFKA_BOOTSTRAP_SERVERS=localhost:9092
   REDIS_HOST=localhost
   REDIS_PORT=6379
   ```

### 3. Provision dependent services

1. Start PostgreSQL, Kafka, and Redis via Docker Compose from the repository root:
   ```bash
   $ docker compose up -d
   ```
   The default compose file exposes data directories under Docker-managed volumes. Add your own `- ./postgres-data:/var/lib/postgresql/data`
   style mounts if you need host-level persistence or backups.
2. Verify the containers are healthy before continuing:
   ```bash
   $ docker compose ps
   ```
   Proceed only once `postgres`, `kafka`, and `redis` report a `healthy` or `running` status.

### 4. Build and run the Spring Boot application

1. Build the executable JAR using Gradle (this will download the Gradle wrapper on the first run):
   ```bash
   $ ./gradlew bootJar
   ```
   The resulting artifact is written to `build/libs/foodify-server-0.0.1-SNAPSHOT.jar`.
2. Launch the service by exporting the same environment variables used during the build and running the JAR:
   ```bash
   $ set -a
   $ source .env
   $ set +a
   $ java -jar build/libs/foodify-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```
   By default Flyway will run pending migrations at startup and the REST API will listen on port `8081`.

### 5. (Optional) Configure a systemd service

To keep the application running after logout or across restarts, create `/etc/systemd/system/foodify.service` as `root` with the
following content:

```ini
[Unit]
Description=Foodify Spring Boot API
After=network.target docker.service
Requires=docker.service

[Service]
User=foodify
WorkingDirectory=/opt/foodify/app
EnvironmentFile=/opt/foodify/app/.env
ExecStart=/usr/bin/java -jar /opt/foodify/app/build/libs/foodify-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=on-failure
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

Reload systemd, enable, and start the service:

```bash
# systemctl daemon-reload
# systemctl enable --now foodify.service
```

Check logs with `journalctl -u foodify -f` and ensure the health endpoints respond (e.g., `curl http://localhost:8081/actuator/health`).
