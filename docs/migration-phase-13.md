# Migration Phase 13 – Identity Service Facade & Schema Controls

Phase 13 begins the identity service extraction by wrapping all authentication endpoints
with a dedicated service facade, introducing remote proxy support, and externalising JWT
configuration so credentials remain stable across restarts.

## Highlights

- Routed `/api/auth/**` flows through a new `IdentityAuthService` abstraction that can run
  locally or forward to an external identity microservice via configurable REST clients.
- Persisted JWT signing secrets, TTLs, and schema management controls under
  `identity.*` configuration, allowing environments to rotate keys and delegate schema
  ownership to the identity bounded context.
- Added conditional Flyway migrations for the `identity` schema so database objects can be
  provisioned independently when the identity service runs outside the monolith.

## Next Steps

- Move the phone signup workflow and any remaining repository access to the
  `IdentityAuthService` facade so the monolith never touches identity persistence
  directly.
- Stand up the dedicated identity microservice, implement the remote endpoints, and enable
  `IDENTITY_SERVICE_MODE=remote` in staging environments to exercise the strangler rollout.
- Introduce OIDC-compliant token issuance and contract tests between the edge gateway/BFFs
  and the identity service to guarantee compatibility.
