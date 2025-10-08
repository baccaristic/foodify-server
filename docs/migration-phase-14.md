# Migration Phase 14 – Catalog Service Facade & Remote Microservice

Phase 14 focuses on the restaurant/catalog bounded context. The monolith now consumes menu and
restaurant data exclusively through a `RestaurantCatalogService` seam that can back onto local
repositories or proxy to a standalone catalog microservice. This mirrors the identity migration and
prepares order/cart flows for eventual extraction.

## Highlights

- Renamed the previous repository-backed implementation to `LocalRestaurantCatalogService` and
  introduced a `RemoteRestaurantCatalogService` that communicates with the catalog microservice via
  Spring's `RestClient` while maintaining compatibility with existing JPA entities.
- Added `catalog.service.*` configuration properties, REST client wiring, and contract tests to ensure
  the remote adapter honours the new REST endpoints.
- Bootstrapped a standalone Spring Boot catalog service under `services/catalog-service` exposing
  REST APIs for restaurants, menu items, and extras, complete with Docker packaging and Compose
  wiring for the shared `platform` profile.

## Next Steps

- Gradually decouple the order aggregate from JPA relationships to restaurants/menu items so the
  remote catalog data can become the single source of truth without relying on shared schemas.
- Introduce CDC or synchronization between the monolith and catalog service databases to support
  independent persistence and horizontal scaling.
- Expand contract/integration tests to cover complex catalog reads (search, availability) and verify
  pricing consistency when promotions or extras change.
