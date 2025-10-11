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
  keeps queries fast and payload sizes predictable. **Update:** the service now limits results to the
  most recent 90 days.【F:src/main/java/com/foodify/server/modules/customers/application/ClientService.java†L43-L135】【F:src/main/java/com/foodify/server/modules/orders/repository/OrderRepository.java†L16-L63】

- **Several repository helpers lack pagination limits.** `OrderRepository` exposes convenience
  methods such as `findAllByRestaurantOrderByDateDesc`, `findAllByPendingDriverId`, and
  `findAllByDriverIdAndStatus` (the last one fetch-joins items and delivery data). These queries can
  return very large collections with no safety net, which will inflate both heap usage and query time
  as data grows. Add explicit pagination/streaming variants or tighten the call sites to request only
  the required slice of data.【F:src/main/java/com/foodify/server/modules/orders/repository/OrderRepository.java†L16-L63】

- **Archival job processes every eligible order in one transaction.** The scheduled
  `OrderArchivalService#archiveCompletedOrders` loads the full result set, updates the entities in
  memory, and issues a single `saveAll`. On a busy day this could mean thousands of rows being locked
  and flushed in one batch, delaying other writers. Switching to chunked processing (paging through
  IDs or using a bulk `UPDATE`) would keep the archival window bounded.【F:src/main/java/com/foodify/server/modules/orders/application/OrderArchivalService.java†L27-L44】

## 2. Driver assignment and location tracking

- **Location updates hit the primary database.** `DriverLocationService#updateDriverLocation`
  performs a `findById` and potentially a `save` each time a driver pushes GPS coordinates—even when
  only the Redis geo index needs to change. Under production traffic this introduces unnecessary
  write amplification on the relational database. Persist availability state in Redis or queue
  updates so that the database is not on the critical path for high-frequency telemetry.【F:src/main/java/com/foodify/server/modules/delivery/location/DriverLocationService.java†L94-L104】

- **Candidate evaluation issues multiple synchronous lookups.** The driver matching algorithm loops
  through each candidate and for every one executes four repository calls (`driver`, `shift`,
  `active delivery`, `last delivery`). Under a surge of restaurant acceptances this multiplies
  database load and increases matching latency. Caching active shift/delivery state in Redis or using
  batched queries per search wave would drastically reduce the query count.【F:src/main/java/com/foodify/server/modules/delivery/application/DriverAssignmentService.java†L69-L129】

## 3. Notification delivery throughput

- **Push notifications are sent synchronously.** `PushNotificationService#sendOrderNotification`
  performs a blocking call to Firebase for every notification. During spikes (order accepted, driver
  assigned, delivered) these calls compete with request threads. Running the send through an async
  executor or event queue keeps the API responsive even when Firebase is slow.【F:src/main/java/com/foodify/server/modules/notifications/application/PushNotificationService.java†L12-L24】

## 4. Secondary data stores

- **Redis geo reads still rely on per-driver key lookups.** `DriverLocationService#findClosestDrivers`
  queries Redis Geo to rank candidates but then checks each driver’s status via an additional string
  lookup. While cheap, it still multiplies the Redis round-trips. Consider storing the status as a
  Geo member payload or pipeline the lookups if profiling shows Redis CPU saturation.【F:src/main/java/com/foodify/server/modules/delivery/location/DriverLocationService.java†L24-L66】

## 5. Monitoring and back-pressure considerations

- **Order lifecycle events fan out synchronously.** Each transition both persists a history row and
  publishes a Spring event from the same transaction. If downstream listeners become slow or start
  performing remote calls the original state change will block. Evaluate making the event emission
  asynchronous or buffering lifecycle notifications outside of the transaction boundary.【F:src/main/java/com/foodify/server/modules/orders/application/OrderLifecycleService.java†L23-L75】

- **Real-time order notifications hit Redis on-demand.** Every time we render an order notification we
  fetch the driver’s last known position from Redis. This is acceptable for low volume, but once the
  operations team builds dashboards polling these endpoints frequently it becomes another source of
  load. Adding short-lived caches or coalescing reads per driver will help.【F:src/main/java/com/foodify/server/modules/orders/mapper/OrderNotificationMapper.java†L21-L78】

## 6. Schema and indexing follow-ups

- Audit indexes for the most common filters (`orders.status`, `orders.archived_at`, `orders.date`,
  `delivery.driver_id`) so the queries mentioned above remain index-backed as the tables grow. The
  native geo-distance query on `restaurant` will also need supporting indexes on latitude/longitude
  to avoid full table scans.【F:src/main/java/com/foodify/server/modules/restaurants/repository/RestaurantRepository.java†L13-L34】

Addressing the items above before launch—or at least building observability around them—will reduce
risk of database saturation, Redis bottlenecks, and slow user-facing APIs when real-world traffic
arrives.
