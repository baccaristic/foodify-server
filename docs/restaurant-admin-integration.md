h1. Restaurant Admin API & Realtime Integration Guide

This page enumerates every REST endpoint and realtime channel exposed by the Foodify server that the restaurant-facing client relies on. It was generated directly from the Spring controllers, services, and DTOs in this repository so it reflects the current implementation.

h2. Authentication and Session Lifecycle

h3. Login
* *Endpoint*: `POST /api/auth/login`
* *Body* (`application/json`):
{code:language=json}
{
  "email": "owner@example.com",
  "password": "plaintext-password"
}
{code}
* *Behavior*: Validates the credentials against the `User` table. Restaurant admins sign in through the general login endpoint (there is no role-specific login route). Upon success the response includes both JWT tokens and the persisted user object.
* *Success (200)*: Returns `{ "accessToken", "refreshToken", "user" }`. The `user.role` value must be `RESTAURANT_ADMIN` for the admin client to have the required authorities.
* *Failure (401)*: `{ "success": false, "message": "Invalid email or password" }` when the credentials do not match.

h3. Token Refresh & Session Heartbeat
* `POST /api/auth/refresh` exchanges a valid refresh token for a new access token. The request body is `{ "refreshToken": "..." }`.
* `GET /api/auth/heart-beat` validates an access token and returns `{ "status": "active" }` when the session is still valid.

h3. Authorizing Requests
* Every REST call must include the `Authorization: Bearer <accessToken>` header.
* STOMP WebSocket connections also require the same header in the CONNECT frame. The server extracts the `role` and JWT subject to resolve the admin id before streaming order data.

h2. WebSocket Connectivity

Restaurant admins receive live order state through the `/ws` SockJS/STOMP endpoint.

h3. Connection Flow
# Open a WebSocket (SockJS is supported) to `/ws`.
# Send a STOMP `CONNECT` frame that includes `Authorization: Bearer <accessToken>` as a native header.
# Upon successful authentication the server immediately publishes a snapshot of all non-archived active orders for the admin.

h3. Destinations
All destinations use Spring’s user queues, so clients must subscribe to the `/user` prefix.

|| Purpose || Subscription || Payload || Trigger ||
| Initial backlog | `/user/queue/restaurant/orders/snapshot` | Array of `OrderNotificationDTO` objects | Emitted automatically when the WebSocket session is established for a restaurant admin. |
| New order alert | `/user/queue/restaurant/orders/new` | Single `OrderNotificationDTO` | Fired exactly once right after an order is created so restaurants can surface prominent “new order” UI states. |
| Realtime updates | `/user/queue/restaurant/orders` | Single `OrderNotificationDTO` | Sent whenever an order owned by the restaurant changes status (accepted, preparing, ready, etc.). |

{tip:title=Realtime subscription guidance}
Subscribe to both `/user/queue/restaurant/orders` and `/user/queue/restaurant/orders/new` if the UI needs to flash dedicated “new order” badges or sounds. The creation-specific channel emits once per order while the general feed continues streaming every lifecycle transition so dashboards stay in sync.
{tip}

h3. OrderNotificationDTO Shape
Each realtime message uses the `OrderNotificationDTO` record with the following structure.

{panel:title=OrderNotificationDTO}
|| Field || Type || Description ||
| `orderId` | `number` | Unique identifier for the order. |
| `deliveryAddress` | `string` | Formatted delivery address shown to staff. |
| `paymentMethod` | `string` | Payment channel label (e.g., `CARD`, `CASH`). |
| `date` | `string` | ISO-8601 timestamp when the order was placed. |
| `items` | `OrderItemDTO[]` | Detailed list of ordered menu items. |
| `savedAddress` | `SavedAddressSummaryDto` | Customer’s saved address context when available. |
| `client` | `ClientSummaryDTO` | Customer identity displayed to staff. |
| `status` | `string` | Current order status (use enum in status table below). |
| `deliveryLocation` | `LocationDto` | Lat/Lng coordinates for the drop-off. |
| `restaurantImage` | `string` | URL of the restaurant banner image. |
| `restaurantIcon` | `string` | URL of the restaurant square icon. |
| `restaurant` | `RestaurantSummary` | Restaurant metadata for multi-location dashboards. |
| `delivery` | `DeliverySummary` | Driver assignment, timestamps, and live location. |
| `payment` | `PaymentSummary` | Monetary totals and breakdowns. |
| `statusHistory` | `OrderStatusHistoryDTO[]` | Chronological lifecycle log for the order. |
| `deliveryToken` | `string` | Code shared with the driver when needed. |
| `pickupToken` | `string` | Code used at handoff for pickup verification. |
{panel}

{panel:title=OrderItemDTO}
|| Field || Type || Description ||
| `menuItemId` | `number` | Identifier of the ordered menu item. |
| `menuItemName` | `string` | Display name of the menu item. |
| `quantity` | `number` | Quantity requested by the customer. |
| `extras` | `string[]` | Labels of selected extras/modifiers. |
| `specialInstructions` | `string` | Freeform instructions from the customer. |
| `unitBasePrice` | `number` | Base price without extras. |
| `unitPrice` | `number` | Final per-item price (base + extras). |
| `unitExtrasPrice` | `number` | Additional price contributed by extras. |
| `lineSubtotal` | `number` | Quantity × base price. |
| `promotionDiscount` | `number` | Discount applied to the line. |
| `lineItemsTotal` | `number` | Subtotal plus extras before discounts. |
| `extrasTotal` | `number` | Total value of extras for the line. |
| `lineTotal` | `number` | Final amount charged for the line. |
{panel}

{panel:title=SavedAddressSummaryDto}
|| Field || Type || Description ||
| `id` | `string` | UUID identifier of the saved address. |
| `type` | `string` | Address type enum (`HOME`, `WORK`, etc.). |
| `label` | `string` | Custom nickname shown in the UI. |
| `formattedAddress` | `string` | Full formatted address text. |
| `placeId` | `string` | Provider-specific place identifier. |
| `entrancePreference` | `string` | Preferred building entrance description. |
| `entranceNotes` | `string` | Additional entrance instructions. |
| `directions` | `string` | Turn-by-turn hints provided by the customer. |
| `notes` | `string` | Extra notes for couriers. |
| `primary` | `boolean` | Indicates whether this is the customer’s primary saved address. |
{panel}

{panel:title=ClientSummaryDTO}
|| Field || Type || Description ||
| `id` | `number` | Customer identifier. |
| `name` | `string` | Display name for receipts and dashboards. |
{panel}

{panel:title=LocationDto}
|| Field || Type || Description ||
| `lat` | `number` | Latitude in decimal degrees. |
| `lng` | `number` | Longitude in decimal degrees. |
{panel}

{panel:title=RestaurantSummary}
|| Field || Type || Description ||
| `id` | `number` | Restaurant identifier. |
| `name` | `string` | Venue name. |
| `address` | `string` | Restaurant address displayed to staff. |
| `phone` | `string` | Contact number for the venue. |
| `imageUrl` | `string` | Hero image URL. |
| `iconUrl` | `string` | Icon-sized image URL. |
| `location` | `LocationDto` | Geographic coordinates for mapping. |
{panel}

{panel:title=DeliverySummary}
|| Field || Type || Description ||
| `id` | `number` | Delivery task identifier. |
| `driver` | `DriverSummary` | Assigned driver details. |
| `estimatedPickupTime` | `number` | Epoch milliseconds estimate for pickup. |
| `estimatedDeliveryTime` | `number` | Epoch milliseconds estimate for drop-off. |
| `pickupTime` | `string` | ISO timestamp when driver picked up the order. |
| `deliveredTime` | `string` | ISO timestamp when delivery completed. |
| `driverLocation` | `LocationDto` | Current driver coordinates when tracking. |
| `address` | `string` | Delivery address seen by the driver. |
| `location` | `LocationDto` | Drop-off coordinates. |
| `savedAddress` | `SavedAddressSummaryDto` | Saved-address mirror shared with the driver when applicable. |
{panel}

{panel:title=DriverSummary}
|| Field || Type || Description ||
| `id` | `number` | Driver identifier. |
| `name` | `string` | Driver display name. |
| `phone` | `string` | Contact phone number. |
{panel}

{panel:title=PaymentSummary}
|| Field || Type || Description ||
| `subtotal` | `number` | Base subtotal before extras. |
| `extrasTotal` | `number` | Total cost of extras/modifiers. |
| `total` | `number` | Final amount charged to the customer. |
| `itemsSubtotal` | `number` | Subtotal considering quantities. |
| `promotionDiscount` | `number` | Discount applied to the entire order. |
| `itemsTotal` | `number` | Items subtotal minus discounts. |
| `deliveryFee` | `number` | Delivery charge applied to the order. |
{panel}

{panel:title=OrderStatusHistoryDTO}
|| Field || Type || Description ||
| `action` | `string` | Lifecycle event that triggered the update. |
| `previousStatus` | `string` | Status before the action. |
| `newStatus` | `string` | Status after the action. |
| `changedBy` | `string` | Actor responsible for the change (system, driver, admin). |
| `reason` | `string` | Reason text when provided. |
| `metadata` | `string` | Additional metadata payload. |
| `changedAt` | `string` | ISO timestamp when the transition occurred. |
{panel}

h2. Restaurant Order Management REST APIs
Every endpoint under `/api/restaurant` requires a valid restaurant admin access token (`ROLE_RESTAURANT_ADMIN`).

h3. Fetch current orders
* *Endpoint*: `GET /api/restaurant/my-orders`
* *Query parameters*:
** `page` (default `0`): zero-based page index.
** `pageSize` (default `20`, max `50`): number of orders per page.
** `from` / `to` (optional, format `dd/MM/yyyy`): filter orders by creation date range.
* *Response*: `PageResponse<OrderNotificationDTO>` with orders sorted by newest first. Each record includes the customer profile, delivery estimates, line items, and payment totals.

{panel:title=PageResponse structure}
|| Field || Type || Description ||
| `items` | `OrderNotificationDTO[]` | Slice of orders matching the filters. |
| `page` | `number` | Zero-based index of the returned slice. |
| `pageSize` | `number` | Size of each slice; aligns with the `pageSize` query parameter. |
| `totalItems` | `number` | Total orders that match the filters (useful for pagination UI). |
{panel}

h3. Fetch active orders only
* *Endpoint*: `GET /api/restaurant/my-active-orders`
* *Response*: List of `OrderNotificationDTO` entries restricted to active lifecycle states (`PENDING` through `IN_DELIVERY`) and excluding archived records. Sorted by newest first for dashboard convenience.

h3. Fetch a single order
* *Endpoint*: `GET /api/restaurant/order/{orderId}`
* *Behavior*: Returns an `OrderNotificationDTO` only when the order belongs to the authenticated restaurant. Otherwise `null` is returned.

h3. Accept an order
* *Endpoint*: `POST /api/restaurant/accept-order/{orderId}`
* *Behavior*:
** Ensures the authenticated admin owns the order.
** Transitions the order to `ACCEPTED` and records the lifecycle event.
** Kicks off driver assignment (WebSocket and push notifications to drivers happen automatically inside the order lifecycle services).
* *Response*: Fresh `OrderNotificationDTO` snapshot.

h3. Mark an order ready for pickup
* *Endpoint*: `POST /api/restaurant/order/ready/{orderId}`
* *Behavior*: Validates restaurant ownership and transitions the order to `READY_FOR_PICK_UP`. Downstream notifications (driver, client, restaurant WebSocket) are emitted by the lifecycle listener.

h3. Retrieve menu catalog
* *Endpoint*: `GET /api/restaurant/my-menu`
* *Behavior*: Returns all `MenuItem` entities belonging to the admin’s restaurant. Useful for editing experiences.

h3. Toggle menu item availability
* *Endpoint*: `PATCH /api/restaurant/menu/{menuId}/availability`
* *Body* (`application/json`): `{ "available": true }`
* *Behavior*: Validates restaurant ownership and toggles the availability flag without altering other menu attributes. Returns the updated `MenuItem`. Use this for quick “86” flows in the kitchen UI.

h3. Retrieve menu categories
* *Endpoint*: `GET /api/restaurant/categories`
* *Behavior*: Returns all `MenuCategory` entries for the authenticated restaurant. Ideal for pre-populating category filters in menu editors.

h3. Create menu category
* *Endpoint*: `POST /api/restaurant/categories`
* *Body* (`application/json`): `MenuCategoryRequestDTO` containing the fields below. The server enriches additional metadata (color, ordering) from defaults or subsequent updates.
* *Behavior*: Creates a category tied to the admin’s restaurant and returns the persisted `MenuCategory`. Use this to let restaurants customize their menus beyond the defaults.

{panel:title=MenuCategoryRequestDTO}
|| Field || Type || Description ||
| `name` | `string` | Display label for the category. |
{panel}

h2. Menu & Operating Hours Management APIs
Both menu write endpoints accept multipart payloads so images can be attached alongside JSON metadata.

h3. Add menu item
* *Endpoint*: `POST /api/restaurant/addMenu`
* *Payload*: `multipart/form-data` with parts:
** `menu`: JSON serialized `MenuItemRequestDTO` detailed below.
** `files`: Optional list of images.
* *Behavior*: Injects the authenticated restaurant id into the DTO, persists the menu item, and stores uploaded images. Returns the saved `MenuItem` entity.

h3. Update menu item
* *Endpoint*: `PUT /api/restaurant/menu/{menuId}`
* *Payload*: Same structure as the add endpoint (multipart with `menu` and optional `files`).
* *Behavior*: Validates restaurant ownership of the targeted menu item, updates persisted fields, and returns the updated entity.

{panel:title=MenuItemRequestDTO}
|| Field || Type || Description ||
| `id` | `number` | Optional item id when editing an existing dish. |
| `name` | `string` | Menu item name displayed to customers. |
| `description` | `string` | Rich description shown in the menu. |
| `price` | `number` | Base price for the item. |
| `categoryIds` | `number[]` | Category identifiers applied to the item. |
| `popular` | `boolean` | Marks the item as popular in the UI. |
| `restaurantId` | `number` | Automatically set to the authenticated restaurant. |
| `available` | `boolean` | Optional availability flag during creation or update. |
| `promotionLabel` | `string` | Badge text for promotional pricing. |
| `promotionPrice` | `number` | Discounted promotional price. |
| `promotionActive` | `boolean` | Indicates whether the promotion is active. |
| `imageUrls` | `string[]` | Existing image URLs retained during updates. |
| `optionGroups` | `OptionGroupDTO[]` | Modifier groups such as toppings or sizes. |
{panel}

{panel:title=OptionGroupDTO}
|| Field || Type || Description ||
| `id` | `number` | Optional identifier for updates. |
| `name` | `string` | Group label (e.g., “Choose your toppings”). |
| `minSelect` | `number` | Minimum number of extras a customer must select. |
| `maxSelect` | `number` | Maximum extras allowed from the group. |
| `required` | `boolean` | Whether the customer must choose from the group. |
| `extras` | `ExtraDTO[]` | Available choices within the group. |
{panel}

{panel:title=ExtraDTO}
|| Field || Type || Description ||
| `id` | `number` | Optional identifier for updates. |
| `name` | `string` | Name of the extra or modifier. |
| `price` | `number` | Additional price added when selected. |
| `isDefault` | `boolean` | Indicates whether the extra is preselected. |
{panel}

h3. Read current operating hours
* *Endpoint*: `GET /api/restaurant/operating-hours`
* *Behavior*: Retrieves the current weekly schedule and any special days configured for the restaurant. The response body contains two collections described below. Use this to populate availability editors in the admin UI.

{panel:title=Operating hours response}
|| Field || Type || Description ||
| `weeklySchedule` | `WeeklyScheduleEntry[]` | Seven-entry array (Monday–Sunday). |
| `specialDays` | `SpecialDayEntry[]` | Overrides for specific calendar dates. |
{panel}

{panel:title=WeeklyScheduleEntry}
|| Field || Type || Description ||
| `day` | `string` | Enum-like uppercase day name (`MONDAY`–`SUNDAY`). |
| `open` | `boolean` | Whether the restaurant accepts orders on that day. |
| `opensAt` | `string` | Optional `HH:mm` opening time when `open = true`. |
| `closesAt` | `string` | Optional `HH:mm` closing time when `open = true`. |
{panel}

{panel:title=SpecialDayEntry}
|| Field || Type || Description ||
| `id` | `number` | Identifier for updates/deletes. |
| `name` | `string` | Human-readable label (e.g., Holiday). |
| `date` | `string` | ISO `yyyy-MM-dd` date of the override. |
| `open` | `boolean` | Whether the restaurant opens on that day. |
| `opensAt` | `string` | Optional `HH:mm` opening time when `open = true`. |
| `closesAt` | `string` | Optional `HH:mm` closing time when `open = true`. |
{panel}

h3. Update weekly schedule
* *Endpoint*: `PUT /api/restaurant/operating-hours/weekly`
* *Body* (`application/json`): `UpdateWeeklyScheduleRequest` described below. Closed days can omit times or set `open` to `false`.
* *Behavior*: Overwrites the standard weekly schedule for the restaurant and returns the consolidated operating hours response.

{panel:title=UpdateWeeklyScheduleRequest}
|| Field || Type || Description ||
| `days` | `DaySchedule[]` | Weekly templates submitted together. |
{panel}

{panel:title=DaySchedule}
|| Field || Type || Description ||
| `day` | `string` | Enum value `MONDAY`–`SUNDAY`. |
| `open` | `boolean` | Whether the restaurant opens that day. |
| `opensAt` | `string` | Optional `HH:mm` opening time when `open = true`. |
| `closesAt` | `string` | Optional `HH:mm` closing time when `open = true`. |
{panel}

h3. Manage special operating days
* *Add special day*: `POST /api/restaurant/operating-hours/special-days`
* *Update special day*: `PUT /api/restaurant/operating-hours/special-days/{id}`
* *Delete special day*: `DELETE /api/restaurant/operating-hours/special-days/{id}`
* *Body* (`application/json` for add/update): `SaveSpecialDayRequest` detailed below. When `open` is false the time fields may be null to represent a full-day closure.
* *Behavior*: Adds exceptions to the weekly schedule for holidays or events. Responses include the created/updated `SpecialDay` entry so clients can keep local caches in sync.

{panel:title=SaveSpecialDayRequest}
|| Field || Type || Description ||
| `name` | `string` | Label for the special day (e.g., Holiday). |
| `date` | `string` | `yyyy-MM-dd` date of the override. |
| `open` | `boolean` | Whether the restaurant is open on that date. |
| `opensAt` | `string` | Optional `HH:mm` opening time when `open = true`. |
| `closesAt` | `string` | Optional `HH:mm` closing time when `open = true`. |
{panel}

h2. Order Lifecycle & Status Reference
Restaurant dashboards should support the server’s canonical order statuses. These values appear in both REST and realtime payloads and represent the full lifecycle handled by the listener and repository filters.

|| Status || Description || Typical Restaurant Action ||
| `PENDING` | Order submitted by customer, awaiting restaurant decision. | Accept or reject. |
| `ACCEPTED` | Restaurant approved order; driver assignment in progress. | Begin preparation. |
| `PREPARING` | Driver assigned; kitchen preparing food. | Keep prepping and update when ready. |
| `READY_FOR_PICK_UP` | Kitchen finished, waiting for driver pickup. | Handoff to driver using QR code. |
| `IN_DELIVERY` | Driver picked up order. | Monitor delivery, respond to support. |
| `DELIVERED` | Driver confirmed delivery to customer. | No action required. |
| `CANCELED` / `REJECTED` | Order canceled by system or rejected by restaurant. | Communicate with support if needed. |

Only non-archived orders with statuses through `IN_DELIVERY` are included in the websocket snapshot for admins; once delivered or archived they disappear unless explicitly fetched via REST.

h2. Notifications Triggered by Restaurant Actions
* When an admin accepts an order, the service layer transitions the status and triggers driver assignment. Drivers receive both WebSocket updates (`notifyDriverUpcoming`) and push notifications if they have opted in.
* Order status changes (accept, ready, etc.) automatically propagate to:
** Restaurant WebSocket queue (`notifyRestaurant`).
** Assigned driver or pending driver queue.
** Client WebSocket queue and, when enabled, push notifications through the order lifecycle listener.

By wiring RESTful actions with these realtime channels, the restaurant app can keep its dashboard synchronized with the server while giving staff insight into driver progress and payment details.
