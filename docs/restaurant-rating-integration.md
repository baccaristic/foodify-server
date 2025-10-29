# Restaurant Rating Integration Guide

This guide explains how the Foodify mobile client (React Native) should consume the new restaurant rating APIs. It is derived di
rectly from the Spring controllers, services, DTOs, and migration scripts that power the feature, so it reflects the current ser
ver implementation.

## Feature Overview

- Clients can rate restaurants once an order has been delivered. Subsequent submissions overwrite the previous rating for the sa
me order so edits are supported.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L31-L83】
- Ratings are stored per order with an optional free-text comment capped at 1,024 characters.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingRequest.java†L8-L11】【F:src/main/resources/db/migration/V9__create_restaurant_ratings.sql†L1-L16】
- Every time a rating is saved, the restaurant’s aggregated 5-star average and total rating count are recalculated and returned i
n the response.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L62-L113】
- Only authenticated clients (`ROLE_CLIENT`) can access these endpoints, and they can interact solely with their own delivered or
ders.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L21-L43】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L39-L47】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L93-L113】

## Endpoints

Both endpoints live under the existing client restaurant namespace and require the usual `Authorization: Bearer <accessToken>` h
eader.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L15-L43】

| Purpose | Method & Path | Success Codes | Empty State |
| --- | --- | --- | --- |
| Submit or update a rating for an order | `POST /api/client/restaurants/orders/{orderId}/ratings` | `201 Created` with `RestaurantRatingResponse` | — |
| Fetch the authenticated client’s rating for an order | `GET /api/client/restaurants/orders/{orderId}/ratings` | `200 OK` with `RestaurantRatingResponse` | `204 No Content` when no rating exists |

### Request Payload (`RestaurantRatingRequest`)

```json
{
  "rating": 4,
  "comments": "Loved the seasonal menu!"
}
```

- `rating` is required and must be an integer between `1` and `5` (inclusive). Validation errors return HTTP `400` with a message
 from the server.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingRequest.java†L8-L11】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L47-L123】
- `comments` is optional. Send `null` or omit the field entirely when no text feedback is provided. The backend trims whitespace
 before storing it.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L55-L60】

### Response Payload (`RestaurantRatingResponse`)

All successful responses share the same shape, whether the rating was freshly created, updated, or fetched.

```json
{
  "id": 8821,
  "restaurantId": 42,
  "orderId": 12345,
  "clientId": 777,
  "rating": 4,
  "comments": "Loved the seasonal menu!",
  "restaurantAverageRating": 4.3,
  "totalRatings": 128,
  "createdAt": "2024-05-17T13:22:45.103",
  "updatedAt": "2024-05-17T13:25:09.588"
}
```

Field notes:

- `restaurantAverageRating` is rounded to one decimal place using half-up rounding and will be `null` when no ratings exist yet.
 Use this value to refresh any local restaurant detail caches or badges without making an extra fetch.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L62-L129】
- `totalRatings` reflects the number of ratings the restaurant has received across all orders and can be surfaced next to the st
ar display.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L64-L113】
- `createdAt` / `updatedAt` timestamps use the server’s timezone and allow the UI to indicate when feedback was last modified.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L71-L116】【F:src/main/resources/db/migration/V9__create_restaurant_ratings.sql†L8-L9】

## Error Handling & Edge Cases

- `403 Forbidden` is returned when the authenticated client does not own the order. The React Native app should treat this as a n
on-recoverable state (e.g., hide the rating UI or show a generic error).【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L41-L43】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L95-L97】
- `409 Conflict` is emitted if the order has not reached `DELIVERED` yet. Prevent this by only enabling the rating screen once th
e order timeline indicates delivery. If it occurs, show an inline message and allow retry after the status changes.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L44-L45】
- `404 Not Found` appears when an invalid `orderId` is supplied or the order no longer exists. Display an inline toast and navigate back to the orders list.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L39-L40】
- Duplicate submissions for the same order simply update the rating; no extra handling is required on the client.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L52-L83】
- When `GET` returns `204 No Content`, render the unrated state (e.g., empty stars, input placeholders). This response means the o
rder is eligible for rating but no score has been stored yet.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L33-L43】

## React Native Integration Steps

1. **Eligibility Check** – Gate the rating button on the order detail screen so it only appears when `order.status === 'DELIVERED
'` and the authenticated user is the original purchaser. This matches backend enforcement to avoid unnecessary failures.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L41-L46】
2. **Prefill Existing Feedback** – When navigating to the rating screen, call the `GET` endpoint. If it returns `200`, populate th
e UI with the existing `rating` and `comments`. If `204`, keep the form empty. Cache the `restaurantAverageRating` and `totalRatings` to refresh any previously stored restaurant summaries.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L33-L43】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L86-L118】
3. **Submit Flow** – On form submission, POST the body shown above. Treat `201` as success, close the modal/screen, and surface a
 success toast. Update local caches (order history, restaurant cards, favorites) with the returned `restaurantAverageRating`/`totalRatings` so the UI reflects the new aggregate immediately.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L21-L31】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L62-L82】
4. **Error Messaging** – Map `400`, `403`, `404`, and `409` responses to user-friendly copy as described above. For transient netw
ork errors, allow retry without clearing the in-progress rating.
5. **Optimistic Updates (Optional)** – If you optimistically update the local restaurant rating, make sure to reconcile with the s
 erver response to respect the rounded average and count returned by the backend.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L64-L129】

## Data Modeling in the App

- Extend the order detail slice/state with an optional `restaurantRating` object mirroring `RestaurantRatingResponse` so both GET
 and POST handlers can reuse the same TypeScript interface.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingResponse.java†L5-L16】
- Update any restaurant list/detail caches to store `averageRating` (nullable) and `totalRatings` so the UI can render “4.3 ★ (12
8 reviews)” style strings. Backend `Restaurant` entities already expose a nullable `rating` field that stays in sync when the ave
rage changes.【F:src/main/java/com/foodify/server/modules/restaurants/domain/Restaurant.java†L28-L74】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L68-L82】
- Keep the comment field limited to 1,024 characters on the client to match the server constraint and avoid validation failures.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingRequest.java†L8-L11】

## Database Note (FYI)

The `restaurant_ratings` table enforces one rating per order (unique `order_id`) and cascades deletes with the associated restaur
ant, client, or order records. This ensures the mobile app never has to manage duplicate entries or dangling feedback records whe
n orders are cleaned up.【F:src/main/resources/db/migration/V9__create_restaurant_ratings.sql†L1-L16】

