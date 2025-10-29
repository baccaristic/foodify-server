# Delivery Rating Integration Guide

This document explains the backend delivery rating capabilities that were just added and how the React Native application should interact with them. It covers the new REST endpoints, request/response payloads, and the data transfer objects that mobile clients must update.

## What's New

* Deliveries can now be rated across four dimensions (timing, food condition, professionalism, and overall) with an optional free-text comment.
* Clients can submit and fetch their own delivery ratings by order.
* Drivers can view a summary of their ratings as well as the most recent detailed reviews.
* Admin tooling can override ratings and browse driver feedback for moderation.
* Order notification payloads now include the delivery rating snapshot so clients and drivers receive rating data in push/socket updates.

## Authentication Overview

All endpoints rely on the usual bearer token/`Authorization` header that the mobile apps already use.

| Audience | Endpoint | Method | Scope / Role Required |
| --- | --- | --- | --- |
| Client app | `/api/delivery/ratings/orders/{orderId}` | `POST` | `ROLE_CLIENT` |
| Client app | `/api/delivery/ratings/orders/{orderId}` | `GET` | `ROLE_CLIENT` |
| Driver app | `/api/driver/ratings/summary` | `GET` | `ROLE_DRIVER` |
| Driver app | `/api/driver/ratings?limit={n}` | `GET` | `ROLE_DRIVER` |
| Admin tooling (FYI) | `/api/admin/drivers/{driverId}/ratings/summary` | `GET` | `ROLE_ADMIN` |
| Admin tooling (FYI) | `/api/admin/drivers/{driverId}/ratings?limit={n}` | `GET` | `ROLE_ADMIN` |
| Admin tooling (FYI) | `/api/admin/drivers/orders/{orderId}/ratings` | `POST` | `ROLE_ADMIN` |

Only the client and driver endpoints need React Native integration. The admin APIs are exposed for completeness but are consumed by the backoffice web app.

## Client App: Submit and Fetch Ratings

### Submit a Rating

```
POST /api/delivery/ratings/orders/{orderId}
Authorization: Bearer <client access token>
Content-Type: application/json
```

**Request body (`DeliveryRatingRequest`)**

```json
{
  "timing": 5,
  "foodCondition": 4,
  "professionalism": 5,
  "overall": 5,
  "comments": "Driver was punctual and friendly."
}
```

* All four numeric fields are required and must be integers between `1` and `5`.
* `comments` is optional. Send `null` or omit the property if not provided.

**Successful response (`DeliveryRatingResponse`)**

```json
{
  "orderId": 12345,
  "deliveryId": 67890,
  "driverId": 333,
  "clientId": 555,
  "clientName": "Jane Doe",
  "timing": 5,
  "foodCondition": 4,
  "professionalism": 5,
  "overall": 5,
  "comments": "Driver was punctual and friendly.",
  "createdAt": "2024-05-01T17:24:12.301",
  "updatedAt": "2024-05-01T17:24:12.301"
}
```

Expect HTTP `409` if the order is not yet marked as delivered or lacks delivery information, and HTTP `403` if the authenticated client does not own the order.

### Fetch an Existing Rating

```
GET /api/delivery/ratings/orders/{orderId}
Authorization: Bearer <client access token>
```

* Returns `200` with the same `DeliveryRatingResponse` payload as above when a rating exists.
* Returns `204 No Content` if no rating has been captured yet.

Use this in the new delivery rating screen to pre-fill data (e.g., allow edits) after a client submits feedback.

## Driver App: Rating Summary and History

### Driver Rating Summary

```
GET /api/driver/ratings/summary
Authorization: Bearer <driver access token>
```

**Response (`DriverRatingSummaryDto`)**

```json
{
  "driverId": 333,
  "ratingCount": 42,
  "timingAverage": 4.7,
  "foodConditionAverage": 4.6,
  "professionalismAverage": 4.9,
  "overallAverage": 4.8
}
```

All averages are double precision values rounded by the database. If a driver has not been rated yet, all averages return `0.0` and `ratingCount` is `0`.

### Recent Driver Ratings

```
GET /api/driver/ratings?limit=20
Authorization: Bearer <driver access token>
```

* `limit` defaults to `20` and is capped at `100`.
* Response is an array of `DeliveryRatingResponse` objects ordered by `createdAt` descending.

Example:

```json
[
  {
    "orderId": 12345,
    "deliveryId": 67890,
    "driverId": 333,
    "clientId": 555,
    "clientName": "Jane Doe",
    "timing": 5,
    "foodCondition": 4,
    "professionalism": 5,
    "overall": 5,
    "comments": "Driver was punctual and friendly.",
    "createdAt": "2024-05-01T17:24:12.301",
    "updatedAt": "2024-05-01T17:24:12.301"
  }
]
```

Render this list anywhere we show driver feedback (e.g., profile > reviews).

## Order Notification Payload Changes

`OrderNotificationDTO` now includes a nested `rating` object when a delivery has been rated. This impacts both the client and driver sockets/push handlers that rely on the existing notification DTO.

```json
{
  "orderId": 12345,
  "status": "DELIVERED",
  "delivery": { ... },
  "rating": {
    "timing": 5,
    "foodCondition": 4,
    "professionalism": 5,
    "overall": 5,
    "comments": "Driver was punctual and friendly.",
    "createdAt": "2024-05-01T17:24:12.301",
    "updatedAt": "2024-05-01T17:24:12.301"
  }
}
```

If no rating exists, `rating` is `null`.

### React Native DTO Updates

* **Order notification models** – Extend the TypeScript interface (or the equivalent data model) representing `OrderNotificationDTO` to include:
  ```ts
  type DeliveryRating = {
    timing: number;
    foodCondition: number;
    professionalism: number;
    overall: number;
    comments?: string | null;
    createdAt: string;
    updatedAt: string;
  } | null;
  ```
  and add a `rating?: DeliveryRating` property to the root notification type.
* **Driver rating summary model** – Add a model for the `DriverRatingSummaryDto` response when showing driver stats.
* **Delivery rating response model** – Reuse this for both client and driver screens (order rating display, driver review list).

## Implementation Checklist for the React Native Team

1. **Create the client rating screen**
   * Fetch the delivered order details and call the `GET /api/delivery/ratings/orders/{orderId}` endpoint to prefill any existing rating.
   * Allow the user to enter the four 1–5 scores and optional comment.
   * Submit via `POST /api/delivery/ratings/orders/{orderId}` and optimistically update local state.
   * Handle validation errors (HTTP 400) by highlighting invalid fields.

2. **Refresh notifications**
   * Update the order notification data parsing to read the new `rating` object.
   * When a notification arrives with `status: DELIVERED` and `rating` present, update the local store so the client can see their submitted feedback immediately.

3. **Driver app updates**
   * Add a driver profile section that calls `/api/driver/ratings/summary` on load.
   * Display the list returned by `/api/driver/ratings` (respect pagination by passing `limit` when necessary).

4. **Analytics / telemetry (optional)**
   * Track rating submissions and driver rating views to measure adoption.

5. **Error handling**
   * Surface conflict errors (HTTP 409) with a friendly message (“You can only rate completed deliveries”).
   * If `GET` returns `204`, treat it as “no rating yet” and show the blank form.

## Reference DTOs

For quick lookup, here are the backend record definitions:

* `DeliveryRatingRequest`
  ```java
  public record DeliveryRatingRequest(
      Integer timing,
      Integer foodCondition,
      Integer professionalism,
      Integer overall,
      String comments
  )
  ```

* `DeliveryRatingResponse`
  ```java
  public record DeliveryRatingResponse(
      Long orderId,
      Long deliveryId,
      Long driverId,
      Long clientId,
      String clientName,
      Integer timing,
      Integer foodCondition,
      Integer professionalism,
      Integer overall,
      String comments,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
  )
  ```

* `DriverRatingSummaryDto`
  ```java
  public record DriverRatingSummaryDto(
      Long driverId,
      long ratingCount,
      double timingAverage,
      double foodConditionAverage,
      double professionalismAverage,
      double overallAverage
  )
  ```

Keep this guide handy while wiring up the mobile flows. Reach out if any additional fields are needed on the responses.
