# Migration Phase 13 – Identity Service Facade & Schema Controls

Phase 13 begins the identity service extraction by wrapping all authentication endpoints
with a dedicated service facade, introducing remote proxy support, and externalising JWT
configuration so credentials remain stable across restarts. The follow-up refinements in this
iteration complete the strangler seam by moving phone signup state into the facade, emitting
OIDC-compliant tokens, and delivering the first standalone identity microservice build.

## Highlights

- Routed `/api/auth/**` flows through a new `IdentityAuthService` abstraction that can run
  locally or forward to an external identity microservice via configurable REST clients.
- Persisted JWT signing secrets, TTLs, and schema management controls under
  `identity.*` configuration, allowing environments to rotate keys and delegate schema
  ownership to the identity bounded context.
- Added conditional Flyway migrations for the `identity` schema so database objects can be
  provisioned independently when the identity service runs outside the monolith.

## Next Steps

- Extend contract testing coverage by adding consumer-driven tests for the new phone signup
  metadata (`tokenType`, `expiresIn`, `scope`) once downstream clients integrate the richer
  responses.
- Harden the standalone identity service with persistent storage, schema migrations, and CI/CD
  automation so remote mode becomes production-ready.
