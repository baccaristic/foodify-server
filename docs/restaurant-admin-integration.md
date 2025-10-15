# Restaurant Admin API & Realtime Integration Guide

This document enumerates every REST endpoint and realtime channel exposed by the Foodify server that the restaurant-facing client relies on. It was generated directly from the Spring controllers, services, and DTOs in this repository so it reflects the current implementation.

## 1. Authentication and Session Lifecycle

### 1.1 Login
- **Endpoint**: `POST /api/auth/login`
- **Body** (`application/json`):
  ```json
  {
    "email": "owner@example.com",
    "password": "plaintext-password"
  }
  ```
- **Behavior**: Validates the credentials against the `User` table. Restaurant admins sign in through the general login endpoint (there is no role-specific login route). Upon success the response includes both JWT tokens and the persisted user object.
- **Success (200)**: Returns `{ "accessToken", "refreshToken", "user" }`. The `user.role` value must be `RESTAURANT_ADMIN` for the admin client to have the required authorities.
- **Failure (401)**: `{ "success": false, "message": "Invalid email or password" }` when the credentials do not match.

### 1.2 Token Refresh & Session Heartbeat
- `POST /api/auth/refresh` exchanges a valid refresh token for a new access token. The request body is `{ "refreshToken": "..." }`.
- `GET /api/auth/heart-beat` validates an access token and returns `{ "status": "active" }` when the session is still valid.

### 1.3 Authorizing Requests
- Every REST call must include the `Authorization: Bearer <accessToken>` header.
- STOMP WebSocket connections also require the same header in the CONNECT frame. The server extracts the `role` and JWT subject to resolve the admin id before streaming order data.【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketEventListener.java†L34-L72】

## 2. WebSocket Connectivity

Restaurant admins receive live order state through the `/ws` SockJS/STOMP endpoint.【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketConfig.java†L18-L27】

### 2.1 Connection Flow
1. Open a WebSocket (SockJS is supported) to `/ws`.
2. Send a STOMP `CONNECT` frame that includes `Authorization: Bearer <accessToken>` as a native header.
3. Upon successful authentication the server immediately publishes a snapshot of all non-archived active orders for the admin.

### 2.2 Destinations
All destinations use Spring’s user queues, so clients must subscribe to the `/user` prefix.

| Purpose | Subscription | Payload | Trigger |
| --- | --- | --- | --- |
| Initial backlog | `/user/queue/restaurant/orders/snapshot` | Array of `OrderNotificationDTO` objects | Emitted automatically when the WebSocket session is established for a restaurant admin.【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketEventListener.java†L24-L71】【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketService.java†L45-L54】 |
| Realtime updates | `/user/queue/restaurant/orders` | Single `OrderNotificationDTO` | Sent whenever an order owned by the restaurant changes status (accepted, preparing, ready, etc.).【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketService.java†L36-L43】【F:src/main/java/com/foodify/server/modules/orders/application/OrderLifecycleEventListener.java†L42-L60】 |

### 2.3 OrderNotificationDTO Shape
Each realtime message uses the `OrderNotificationDTO` record. Important fields include:
- `orderId`, `status`, `date`, and `statusHistory` for the lifecycle timeline.【F:src/main/java/com/foodify/server/modules/orders/dto/OrderNotificationDTO.java†L11-L45】
- `restaurant` summary with id, contact information, and location (useful for multi-location dashboards).【F:src/main/java/com/foodify/server/modules/orders/dto/OrderNotificationDTO.java†L17-L26】
- `delivery` summary containing driver identity, ETA timestamps, and live driver location when available.【F:src/main/java/com/foodify/server/modules/orders/dto/OrderNotificationDTO.java†L28-L36】
- `items` with quantities, extras, and pricing broken down per line item.【F:src/main/java/com/foodify/server/modules/orders/mapper/OrderNotificationMapper.java†L36-L118】
- `payment` aggregate showing subtotal, extras, discounts, delivery fee, and final total.【F:src/main/java/com/foodify/server/modules/orders/dto/OrderNotificationDTO.java†L37-L44】

> 📄 A complete JSON-ready description of every DTO referenced in this guide (including `OrderNotificationDTO`, menu payloads, and their nested records) now lives in [`docs/restaurant-admin-dtos.md`](./restaurant-admin-dtos.md). Share that appendix with the restaurant app team for implementation details.

## 3. Restaurant Order Management REST APIs
Every endpoint under `/api/restaurant` requires a valid restaurant admin access token (`ROLE_RESTAURANT_ADMIN`).【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantController.java†L25-L96】

### 3.1 Fetch current orders
- **Endpoint**: `GET /api/restaurant/my-orders`
- **Response**: List of `OrderNotificationDTO`, sorted by newest first. Includes customer profile, delivery estimates, line items, and totals.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantService.java†L70-L83】【F:src/main/java/com/foodify/server/modules/orders/dto/OrderNotificationDTO.java†L11-L45】

### 3.2 Fetch a single order
- **Endpoint**: `GET /api/restaurant/order/{orderId}`
- **Behavior**: Returns an `OrderNotificationDTO` only when the order belongs to the authenticated restaurant. Otherwise `null` is returned.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantService.java†L146-L155】

### 3.3 Accept an order
- **Endpoint**: `POST /api/restaurant/accept-order/{orderId}`
- **Behavior**:
  - Ensures the authenticated admin owns the order.
  - Transitions the order to `ACCEPTED` and records the lifecycle event.
  - Kicks off driver assignment (WebSocket and push notifications to drivers happen automatically inside `RestaurantService`).【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantService.java†L86-L135】
- **Response**: Fresh `OrderNotificationDTO` snapshot.

### 3.4 Mark an order ready for pickup
- **Endpoint**: `POST /api/restaurant/order/ready/{orderId}`
- **Behavior**: Validates restaurant ownership and transitions the order to `READY_FOR_PICK_UP`. Downstream notifications (driver, client, restaurant WebSocket) are emitted by the lifecycle listener.

### 3.5 Retrieve menu catalog
- **Endpoint**: `GET /api/restaurant/my-menu`
- **Behavior**: Returns all `MenuItem` entities belonging to the admin’s restaurant. Useful for editing experiences.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantController.java†L61-L73】

## 4. Menu Management APIs
Both menu write endpoints accept multipart payloads so images can be attached alongside JSON metadata.

### 4.1 Add menu item
- **Endpoint**: `POST /api/restaurant/addMenu`
- **Payload**: `multipart/form-data` with parts:
  - `menu`: JSON serialized `MenuItemRequestDTO` containing name, description, price, category, flags, and optional extras.
  - `files`: Optional list of images.
- **Behavior**: Injects the authenticated restaurant id into the DTO, persists the menu item, and stores uploaded images. Returns the saved `MenuItem` entity.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantController.java†L37-L57】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantService.java†L157-L205】

### 4.2 Update menu item
- **Endpoint**: `PUT /api/restaurant/menu/{menuId}`
- **Payload**: Same structure as the add endpoint (multipart with `menu` and optional `files`).
- **Behavior**: Validates restaurant ownership of the targeted menu item, updates persisted fields, and returns the updated entity.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantController.java†L59-L87】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantService.java†L205-L236】

## 5. Order Lifecycle & Status Reference
Restaurant dashboards should support the server’s canonical order statuses. These values appear in both REST and realtime payloads and represent the full lifecycle handled by the listener and repository filters.【F:src/main/java/com/foodify/server/modules/orders/domain/OrderStatus.java†L5-L19】【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketEventListener.java†L25-L32】

| Status | Description | Typical Restaurant Action |
| --- | --- | --- |
| `PENDING` | Order submitted by customer, awaiting restaurant decision. | Accept or reject.
| `ACCEPTED` | Restaurant approved order; driver assignment in progress. | Begin preparation.
| `PREPARING` | Driver assigned; kitchen preparing food. | Keep prepping and update when ready.
| `READY_FOR_PICK_UP` | Kitchen finished, waiting for driver pickup. | Handoff to driver using QR code.
| `IN_DELIVERY` | Driver picked up order. | Monitor delivery, respond to support.
| `DELIVERED` | Driver confirmed delivery to customer. | No action required.
| `CANCELED` / `REJECTED` | Order canceled by system or rejected by restaurant. | Communicate with support if needed.

Only non-archived orders with statuses through `IN_DELIVERY` are included in the websocket snapshot for admins; once delivered or archived they disappear unless explicitly fetched via REST.【F:src/main/java/com/foodify/server/modules/notifications/websocket/WebSocketEventListener.java†L24-L52】

## 6. Notifications Triggered by Restaurant Actions
- When an admin accepts an order, `RestaurantService` transitions the status and triggers driver assignment. Drivers receive both WebSocket updates (`notifyDriverUpcoming`) and push notifications if they have opted in.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantService.java†L90-L134】
- Order status changes (accept, ready, etc.) automatically propagate to:
  - Restaurant WebSocket queue (`notifyRestaurant`).
  - Assigned driver or pending driver queue.
  - Client WebSocket queue and, when enabled, push notifications through `OrderLifecycleEventListener`.【F:src/main/java/com/foodify/server/modules/orders/application/OrderLifecycleEventListener.java†L42-L104】

By wiring RESTful actions with these realtime channels, the restaurant app can keep its dashboard synchronized with the server while giving staff insight into driver progress and payment details.
