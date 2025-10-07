# Migration Phase 1 – Modular Boundary Hardening

This phase prepares the monolith for strangler-style extraction by replacing cross-module repository access with explicit application services. Orders now depends on contracts owned by Identity, Restaurants, and Addresses, which will remain stable when the modules are extracted as autonomous services.

## Highlights

- Introduced `ClientDirectoryService`, `RestaurantCatalogService`, and `SavedAddressDirectoryService` interfaces inside their respective bounded contexts.
- Provided Spring-managed default implementations that wrap the existing repositories.
- Refactored `CustomerOrderService` to rely exclusively on those interfaces when fetching clients, restaurants, menu items, extras, and saved addresses.
- Centralised domain-specific validation and not-found handling inside the owning module.

## Next Steps

- Replace synchronous repository calls with REST/Kafka adapters once the services are extracted.
- Publish interface contracts (OpenAPI/AsyncAPI) based on the new service boundaries.
- Add consumer-driven contract tests for the newly exposed application services.
