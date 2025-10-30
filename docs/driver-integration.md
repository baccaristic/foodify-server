# Driver API and Realtime Integration Guide

This document describes every REST endpoint and realtime channel a driver-facing client can use in the Foodify platform. It is derived from the Spring controllers and services in the repository so it always reflects the server implementation.

## 1. Authentication and Session Management

### 1.1 Login
- **Endpoint**: `POST /api/auth/driver/login`
- **Body** (`application/json`):
  ```json
  {
    "email": "driver@example.com",
    "password": "plaintext-password"
  }
  ```
- **Behavior**: Accepts driver credentials and validates both the password and that the persisted account has the `DRIVER` role. Requests from non-driver accounts receive `403 Forbidden`.
- **Success (200)**: Returns `accessToken`, `refreshToken`, and the persisted `user` object. The access token embeds the user identifier as the JWT subject and a `role` claim such as `DRIVER`.
- **Failure (401)**: `{"success": false, "message": "Invalid email or password"}` when the credentials are incorrect.
- **Failure (403)**: `{"success": false, "message": "Driver role required"}` when the email belongs to a non-driver account.

### 1.2 Authorized Requests
- Attach the access token to every HTTP call and STOMP connection using `Authorization: Bearer <accessToken>`.
- When the server validates the token it loads the role-specific authority (e.g. `ROLE_DRIVER`) and exposes the numeric user id as the Spring Security `Authentication#getPrincipal`. Driver endpoints use this principal to enforce ownership.

### 1.3 Token Refresh & Session Checks
- `POST /api/auth/refresh` accepts `{ "refreshToken": "..." }` and issues a fresh `accessToken` if the refresh token is valid.
- `GET /api/auth/heart-beat` validates an access token and returns `{ "status": "active" }` when the session is still valid.

## 2. Driver REST Endpoints
All endpoints below require a valid driver access token and the `ROLE_DRIVER` authority.

### 2.1 Update GPS position
- **Endpoint**: `POST /api/driver/location`
- **Body**:
  ```json
  {
    "driverId": 42,
    "latitude": 40.7128,
    "longitude": -74.0060
  }
  ```
- **Behavior**: Stores the latest position in Redis Geo, ensuring that the driver is marked `AVAILABLE` whenever they are globally flagged as available. The location is later used to compute pickup/delivery ETAs and to find nearby drivers.
- **Response**: Empty body with status `204` (Spring default for void).

### 2.2 Inspect a specific order
- **Endpoint**: `GET /api/driver/order/{orderId}`
- **Behavior**: Returns the persisted `Order` entity (including line items) only if the authenticated driver currently owns the delivery. Otherwise `null` is returned.

### 2.3 Accept an assignment
- **Endpoint**: `POST /api/driver/accept-order/{orderId}`
- **Behavior**:
  - Validates that the driver is available and that the order has not been claimed yet.
  - Creates a `Delivery` record, attaches it to the order, generates a pickup QR token, flips the driver to unavailable, and transitions the order to `PREPARING`.
  - Computes `estimatedPickUpTime`/`estimatedDeliveryTime` using the driver’s last known location.
- **Response**: An `OrderDto` snapshot of the accepted order.
- **Errors**: Throws `IllegalStateException` if the order already has a driver or if the driver is not available.

### 2.4 Toggle availability
- **Endpoint**: `POST /api/driver/updateStatus`
- **Body**: `{ "available": true }`
- **Behavior**: When `available` is `true`, flags the driver as available both in the SQL store and in Redis by writing the status key `driver:status:<driverId>` to `AVAILABLE`.

### 2.5 View pending offers
- **Endpoint**: `GET /api/driver/pendingOrders`
- **Behavior**: Lists every order currently assigned to the driver as a pending candidate (`pendingDriver`). These are the offers a driver can still accept.
- **Response**: Array of raw `Order` entities.

### 2.6 Mark order as picked up
- **Endpoint**: `POST /api/driver/pickup`
- **Body**:
  ```json
  {
    "orderId": "123",
    "token": "pickup-qr-token"
  }
  ```
- **Behavior**: Validates driver ownership, verifies the pickup QR token, generates a 3-digit delivery confirmation token, and transitions the order to `IN_DELIVERY`.
- **Response**: `200 OK` with message `"Order marked as picked up"` on success, otherwise `400 Bad Request`.

### 2.7 Retrieve the active job
- **Endpoint**: `GET /api/driver/ongoing-order`
- **Behavior**: Returns the current `OrderDto` whose status is in `{ACCEPTED, PREPARING, READY_FOR_PICK_UP, IN_DELIVERY}` for the driver.
- **Response**: Either an `OrderDto` or `null` if there is no ongoing delivery.

### 2.8 Complete delivery
- **Endpoint**: `POST /api/driver/deliver-order`
- **Body**:
  ```json
  {
    "orderId": 123,
    "token": "customer-pin"
  }
  ```
- **Behavior**: Confirms the driver owns the order and that the delivery token matches the 3-digit PIN shared with the customer. Marks the delivery time, persists the delivery, and transitions the order to `DELIVERED`.
- **Response**: Boolean `true` when the delivery is recorded; `false` otherwise.

### 2.9 Delivery history
- **Endpoint**: `GET /api/driver/history`
- **Behavior**: Returns a list of delivered orders for the authenticated driver as `OrderDto` instances.

### 2.10 Earnings summary
- **Endpoint**: `GET /api/driver/earnings`
- **Query parameters** (all optional):
  - `dateOn=dd/MM/yyyy` – aggregate earnings for a single calendar day.
  - `from=dd/MM/yyyy` and/or `to=dd/MM/yyyy` – aggregate earnings for a custom range (inclusive).
- **Behavior**:
  - Always returns the driver’s total shift earnings (`avilableBalance`) as well as the totals for the current day, week, and month.
  - When a date filter is supplied (`dateOn`, `from`, or `to`), the response also includes `totalEarnings` representing the sum of the filtered shifts.
- **Response**:
  ```json
  {
    "avilableBalance": 245.50,
    "todayBalance": 40.25,
    "weekBalance": 140.75,
    "monthBalance": 620.10,
    "totalEarnings": 95.00
  }
  ```
- **Errors**: `400 Bad Request` when the date format is invalid, ranges are inverted, or both `dateOn` and `from`/`to` are provided at the same time.

### 2.11 Finance overview & deposits
- **Deposit threshold**: When the driver’s collected cash (Cash on Delivery orders) reaches **250 DT**, they must deposit it before they can start a new shift or accept additional jobs. Failing to deposit automatically keeps the driver unavailable in dispatch.
- **Daily fee**: Each day a shift starts, the platform charges a fixed **20 DT** service fee that is deducted from the driver’s next payout. The platform tracks unpaid fees per day, so the next payout deducts `20 DT` for each unpaid day (for example 5 days → 100 DT) once the driver’s earnings cover the balance.
- **Offline cash handling**: Drivers physically bring their cash to the partner bureau. After they hand over the funds they submit the deposit in the app so the admin team can validate it against the receipt.

- **Endpoint**: `GET /api/driver/finance/summary`
  - **Behavior**: Returns the current financial snapshot for the authenticated driver, including:
    - `cashOnHand` – accumulated cash that still needs to be deposited.
    - `unpaidEarnings` – unpaid 12% commission plus delivery fees for delivered orders.
    - `outstandingDailyFees` – total daily fees that will be deducted from the next payout.
    - `depositThreshold` – the 250 DT ceiling, so the UI can highlight when the driver is close to the limit.
    - `depositRequired` – boolean flag telling the client to block the workflow and show a “Please deposit” action.
    - `nextPayoutAmount` – expected transfer to the driver once the next deposit is confirmed (after fees).
    - `feesToDeduct` – fees that will be removed from the upcoming payout.

- **Endpoint**: `GET /api/driver/finance/deposits`
  - **Behavior**: Lists every deposit request created by the driver, newest first, with status (`PENDING`/`CONFIRMED`), cash amount, payout, and fee deduction.

- **Endpoint**: `POST /api/driver/finance/deposits`
  - **Body** (optional): `{ "amount": 180.00 }`. When omitted the full `cashOnHand` value is used.
  - **Behavior**:
    - Validates that the driver has cash available and that there is no pending deposit awaiting confirmation.
    - For cash balances at or above the 250 DT threshold the server enforces a full deposit.
    - Deducts the declared cash from the running `cashOnHand` balance so the driver can resume shifts while finance validates the paper receipt.
    - Captures the unpaid earnings and outstanding fees snapshot to be processed once an administrator confirms the deposit.
  - **Response**: Deposit record mirroring the list endpoint.

- **Payout timing**: The driver’s unpaid earnings remain visible until the admin marks the deposit as confirmed. At that point the earnings are cleared and outstanding daily fees are deducted.

## 3. Driver Status & Location Semantics
The location service keeps two pieces of state in Redis:
- A GEO set (`drivers:geo`) storing `longitude/latitude` pairs for each driver. Updated through `/api/driver/location`.
- A status key (`driver:status:<driverId>`) storing one of `AVAILABLE`, `PENDING:<orderId>`, or `BUSY:<orderId>`. Availability is toggled via `/api/driver/updateStatus`, `DriverLocationService#markPending`, and `DriverLocationService#markBusy`. The accept/pickup/delivery workflow deletes or overwrites this key as orders progress so that dispatchers can locate free drivers accurately.

## 4. Device Push Registration
Drivers can register their mobile device for push notifications (Firebase, APNs, etc.).

### 4.1 Register a device
- **Endpoint**: `POST /api/devices/register`
- **Body**:
  ```json
  {
    "deviceToken": "fcmtoken",
    "platform": "android",
    "deviceId": "pixel-7",
    "appVersion": "1.4.0"
  }
  ```
- **Behavior**: Associates the token with the authenticated user so that lifecycle events can fan out push notifications in addition to WebSocket updates.

### 4.2 Unregister a device
- **Endpoint**: `POST /api/devices/unregister`
- **Body**: `{ "deviceToken": "fcmtoken" }`
- **Behavior**: Removes the token from the user’s registered devices.

## 5. WebSocket / STOMP Realtime Updates
The server uses Spring’s STOMP broker relay for realtime notifications.

### 5.1 Connection Setup
1. Open a SockJS or native WebSocket connection to `wss://<host>/ws`.
2. During the STOMP `CONNECT` frame, pass the HTTP header `Authorization: Bearer <accessToken>` so the backend can authenticate the session.
3. On success, the session is associated with the driver id from the JWT subject.

### 5.2 Subscriptions
- Subscribe to `/user/queue/orders` to receive per-driver updates. Messages are delivered whenever:
  - The driver is assigned an order (`pendingDriver` or accepted delivery).
  - The order transitions to a new lifecycle status relevant to the driver.

### 5.3 Message Payload
Messages published to `/user/queue/orders` for drivers use the `OrderDto` schema:
- Restaurant summary (name, address, phone, coordinates).
- Customer details (name, phone, delivery address, optional saved address metadata).
- `items`: array of ordered menu items.
- Driver section (`driverId`, `driverName`, `driverPhone`).
- Timing estimates (`estimatedPickUpTime`, `estimatedDeliveryTime`).
- Global order fields (`id`, `status`, `total`, `createdAt`).

The same destination is also used for client notifications, so driver apps should be prepared to filter by `driverId` to ensure they only process messages targeted to them.

### 5.4 Lifecycle Triggers
Order lifecycle events invoke the WebSocket service immediately after the encompassing database transaction commits. Drivers receive updates when:
- They are invited to accept an order (`pendingDriver` assigned).
- They have already accepted and the order status changes (e.g., restaurant marks it ready, customer updates address).
- The workflow reaches `IN_DELIVERY` or `DELIVERED`.

## 6. Suggested Client Workflow
1. Authenticate with `POST /api/auth/login` and store the returned tokens securely.
2. Immediately POST `/api/driver/location` and `/api/driver/updateStatus` (with `{ "available": true }`) after sign-in so the driver becomes discoverable for dispatch.
3. Register the device token if push notifications are supported.
4. Establish the STOMP connection and subscribe to `/user/queue/orders`.
5. Poll `/api/driver/pendingOrders` to display outstanding offers; call `/api/driver/accept-order/{id}` to commit to a job.
6. Use `/api/driver/ongoing-order` to drive the in-progress view, `/api/driver/pickup` when scanning the pickup QR, and `/api/driver/deliver-order` when completing the job with the delivery PIN.
7. Fetch `/api/driver/history` for historical earnings or past deliveries.

