# Production Performance Considerations

This document captures potential hotspots observed in the current code base that are likely to
impact latency, throughput, or infrastructure costs once the platform is under real production load.
It is meant to serve as a checklist for future hardening work.

## 1. Database access patterns

- **Order placement performs per-item lookups.** `CustomerOrderService#createOrder` loads each
  `MenuItem` individually and optionally calls `findAllById` for extras inside the request loop.
  A cart with many distinct items will translate into 1 + _n_ JPA queries before the order is even
  persisted. This also happens in the request thread, increasing response time and DB contention
  during peak ordering windows. Consider bulk-fetching menu items up front or caching menu metadata
  in Redis to avoid the N+1 pattern.【F:src/main/java/com/foodify/server/modules/orders/application/CustomerOrderService.java†L118-L198】

- **Client history endpoints return unbounded result sets.** `ClientService#getMyOrders` and the
  corresponding repository method returned the entire order history as a `List`. In the mobile app this
  was triggered by `/api/client/my-orders`, meaning an avid customer could easily load hundreds of
  records in a single request. Introducing pagination or a rolling window (for example "last 90 days")
  keeps queries fast and payload sizes predictable. **Update:** the service now paginates results,
  returning the newest orders first.【F:src/main/java/com/foodify/server/modules/customers/application/ClientService.java†L43-L135】【F:src/main/java/com/foodify/server/modules/orders/repository/OrderRepository.java†L16-L63】【F:src/main/java/com/foodify/server/modules/customers/api/ClientController.java†L1-L140】

- **Order history summary fetches can overload Hibernate when multiple bags are fetched together.**
  The shared entity graph we introduced for order summaries eagerly loads order items and their extras
  so N+1 lookups disappear. However Hibernate throws `MultipleBagFetchException` when two `List`
  associations are fetched in the same query. **Update:** we now model `OrderItem.menuItemExtras` as a
  `Set`, keeping deterministic iteration while allowing Hibernate to materialize order summaries with
  a single fetch.【F:src/main/java/com/foodify/server/modules/orders/domain/OrderItem.java†L1-L60】【F:src/main/java/com/foodify/server/modules/orders/application/CustomerOrderService.java†L210-L320】【F:src/main/java/com/foodify/server/modules/orders/support/OrderPricingCalculator.java†L1-L140】

- **Several repository helpers lack pagination limits.** `OrderRepository` exposes convenience
  methods such as `findAllByRestaurantOrderByDateDesc`, `findAllByPendingDriverId`, and
  `findAllByDriverIdAndStatus` (the last one fetch-joins items and delivery data). These queries can
  return very large collections with no safety net, which will inflate both heap usage and query time
  as data grows. Add explicit pagination/streaming variants or tighten the call sites to request only
  the required slice of data. **Update:** restaurant admin snapshots and driver pending-order views
  now cap the returned slice using configurable `orders.view` limits so only the most recent activity
  is loaded on dashboards.【F:src/main/java/com/foodify/server/modules/orders/repository/OrderRepository.java†L16-L118】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantService.java†L1-L160】【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketEventListener.java†L1-L120】【F:src/main/java/com/foodify/server/modules/delivery/application/DriverService.java†L1-L220】【F:src/main/java/com/foodify/server/config/OrderViewProperties.java†L1-L40】

- **Archival job processes every eligible order in one transaction.** The scheduled
  `OrderArchivalService#archiveCompletedOrders` loads the full result set, updates the entities in
  memory, and issues a single `saveAll`. On a busy day this could mean thousands of rows being locked
  and flushed in one batch, delaying other writers. Switching to chunked processing (paging through
  IDs or using a bulk `UPDATE`) would keep the archival window bounded. **Update:** the job now iterates
  through configurable `orders.archive.batch-size` slices, archiving batches of orders until none remain so memory footprint
  and transaction time stay predictable.【F:src/main/java/com/foodify/server/modules/orders/application/OrderArchivalService.java†L1-L74】【F:src/main/java/com/foodify/server/modules/orders/repository/OrderRepository.java†L92-L138】

## 2. Driver assignment and location tracking

- **Location updates hit the primary database.** `DriverLocationService#updateDriverLocation`
  performs a `findById` and potentially a `save` each time a driver pushes GPS coordinates—even when
  only the Redis geo index needs to change. Under production traffic this introduces unnecessary
  write amplification on the relational database. Persist availability state in Redis or queue
  updates so that the database is not on the critical path for high-frequency telemetry. **Update:**
  the method now stores GPS points directly in Redis and initializes availability flags lazily,
  avoiding any synchronous database work during telemetry bursts.【F:src/main/java/com/foodify/server/modules/delivery/location/DriverLocationService.java†L92-L104】

- **Candidate evaluation issues multiple synchronous lookups.** The driver matching algorithm loops
  through each candidate and for every one executes four repository calls (`driver`, `shift`,
  `active delivery`, `last delivery`). Under a surge of restaurant acceptances this multiplies
  database load and increases matching latency. Caching active shift/delivery state in Redis or using
  batched queries per search wave would drastically reduce the query count.【F:src/main/java/com/foodify/server/modules/delivery/application/DriverAssignmentService.java†L69-L129】

## 3. Notification delivery throughput

- **Push notifications used to be sent synchronously.** `PushNotificationService#sendOrderNotification`
  performed a blocking call to Firebase for every notification, so order spikes competed with request
  threads. **Update:** notification fan-out now dispatches via a bounded executor, keeping API
  threads free while the pushes are delivered.【F:src/main/java/com/foodify/server/modules/orders/application/OrderLifecycleEventListener.java†L40-L118】【F:src/main/java/com/foodify/server/modules/delivery/application/DriverDispatchService.java†L1-L320】【F:src/main/java/com/foodify/server/config/AsyncConfig.java†L1-L49】

## 4. Secondary data stores

- **Redis geo reads used to rely on per-driver key lookups.** `DriverLocationService#findClosestDrivers`
  queries Redis Geo to rank candidates and now batches status checks using a single `multiGet`
  call. This eliminates the prior loop of string fetches while keeping compatibility with the
  existing availability helpers.【F:src/main/java/com/foodify/server/modules/delivery/location/DriverLocationService.java†L20-L121】

## 5. Monitoring and back-pressure considerations

- **Guardrails instrumented and enforced.** Hot paths such as order placement, driver location
  updates, lifecycle notifications, and nearby restaurant searches now emit Micrometer timers so we
  can alert on latency regressions once traffic ramps up. Spring Boot Actuator is enabled with a
  Prometheus registry, async executors expose queue depth metrics, and a configurable rate-limiting
  filter protects `/api/orders` and the `/api/client/nearby/*` endpoints (top picks, favorites,
  order-again, and paginated listings) from floods while surfacing rejection
  counts for on-call dashboards.【F:build.gradle†L25-L45】【F:src/main/java/com/foodify/server/config/OperationalGuardrailConfig.java†L1-L52】【F:src/main/java/com/foodify/server/config/RateLimitingFilter.java†L1-L149】【F:src/main/resources/application.yml†L38-L84】【F:src/main/java/com/foodify/server/modules/orders/application/CustomerOrderService.java†L21-L128】【F:src/main/java/com/foodify/server/modules/delivery/location/DriverLocationService.java†L1-L104】【F:src/main/java/com/foodify/server/modules/orders/application/OrderLifecycleEventListener.java†L1-L118】【F:src/main/java/com/foodify/server/modules/customers/api/ClientController.java†L1-L120】

- **Order lifecycle events now fan out asynchronously.** The transition workflow persists history in
  the request thread, but notification listeners execute on a dedicated executor after the
  transaction commits, preventing downstream slowness from blocking the API.【F:src/main/java/com/foodify/server/modules/orders/application/OrderLifecycleService.java†L23-L75】【F:src/main/java/com/foodify/server/modules/orders/application/OrderLifecycleEventListener.java†L44-L118】【F:src/main/java/com/foodify/server/config/AsyncConfig.java†L1-L49】

- **Real-time order notifications hit Redis on-demand.** Every time we render an order notification we
  fetch the driver’s last known position from Redis. This is acceptable for low volume, but once the
  operations team builds dashboards polling these endpoints frequently it becomes another source of
  load. Adding short-lived caches or coalescing reads per driver will help.【F:src/main/java/com/foodify/server/modules/orders/mapper/OrderNotificationMapper.java†L21-L78】

## 6. Schema and indexing follow-ups

- Audit indexes for the most common filters (`orders.status`, `orders.archived_at`, `orders.date`,
  `delivery.driver_id`) so the queries mentioned above remain index-backed as the tables grow. The
  native geo-distance query on `restaurant` now pre-filters using latitude/longitude bounding boxes
  and relies on dedicated indexes for the common promotion flags to keep scans bounded.【F:src/main/java/com/foodify/server/modules/restaurants/repository/RestaurantRepository.java†L15-L109】【F:src/main/java/com/foodify/server/modules/restaurants/domain/Restaurant.java†L15-L83】

Addressing the items above before launch—or at least building observability around them—will reduce
risk of database saturation, Redis bottlenecks, and slow user-facing APIs when real-world traffic
arrives.
