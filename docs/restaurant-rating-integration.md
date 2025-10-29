# Restaurant Rating Integration Guide

This guide explains how the Foodify mobile client (React Native) should consume the restaurant rating APIs. It is derived directly from the Spring controllers, services, DTOs, and migration scripts that power the feature, so it reflects the current server implementation.

## Feature Overview

- Clients can rate restaurants once an order has been delivered. Subsequent submissions overwrite the previous feedback for the same order so edits are supported.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L31-L87】
- Ratings capture a simple thumbs up/down decision per delivered order, alongside an optional free-text comment capped at 1,024 characters.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingRequest.java†L6-L9】【F:src/main/resources/db/migration/V9__create_restaurant_ratings.sql†L1-L16】
- Every time feedback is saved, the restaurant’s aggregated 5-star average, total rating count, and thumb breakdown are recalculated from the stored votes and returned in the response.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L63-L125】
- Only authenticated clients (`ROLE_CLIENT`) can access these endpoints, and they can interact solely with their own delivered orders.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L21-L43】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L39-L110】

## Endpoints

Both endpoints live under the existing client restaurant namespace and require the usual `Authorization: Bearer <accessToken>` header.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L15-L43】

| Purpose | Method & Path | Success Codes | Empty State |
| --- | --- | --- | --- |
| Submit or update a rating for an order | `POST /api/client/restaurants/orders/{orderId}/ratings` | `201 Created` with `RestaurantRatingResponse` | — |
| Fetch the authenticated client’s rating for an order | `GET /api/client/restaurants/orders/{orderId}/ratings` | `200 OK` with `RestaurantRatingResponse` | `204 No Content` when no rating exists |

### Request Payload (`RestaurantRatingRequest`)

```json
{
  "thumbsUp": true,
  "comments": "Delivery was fast and the food was hot!"
}
```

- `thumbsUp` is required and must be a boolean (`true` for an up-vote, `false` for a down-vote). Validation errors return HTTP `400` with a message from the server.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingRequest.java†L6-L9】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L47-L70】
- `comments` is optional. Send `null` or omit the field entirely when no text feedback is provided. The backend trims whitespace before storing it.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L55-L60】

### Response Payload (`RestaurantRatingResponse`)

All successful responses share the same shape, whether the rating was freshly created, updated, or fetched.

```json
{
  "id": 8821,
  "restaurantId": 42,
  "orderId": 12345,
  "clientId": 777,
  "thumbsUp": true,
  "comments": "Delivery was fast and the food was hot!",
  "restaurantAverageRating": 4.6,
  "totalRatings": 128,
  "thumbsUpCount": 90,
  "thumbsDownCount": 38,
  "createdAt": "2024-05-17T13:22:45.103",
  "updatedAt": "2024-05-17T13:25:09.588"
}
```

Field notes:

- `restaurantAverageRating` is rounded to one decimal place using half-up rounding and will be `null` when no ratings exist yet. Use this value to refresh any local restaurant detail caches or badges without making an extra fetch.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L63-L124】
- `totalRatings`, `thumbsUpCount`, and `thumbsDownCount` reflect the number of votes the restaurant has received and how they split between positive and negative feedback. Display them alongside the 5-star conversion if desired.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L63-L125】
- `createdAt` / `updatedAt` timestamps use the server’s timezone and allow the UI to indicate when feedback was last modified.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L73-L124】【F:src/main/resources/db/migration/V9__create_restaurant_ratings.sql†L1-L16】

## Error Handling & Edge Cases

- `403 Forbidden` is returned when the authenticated client does not own the order. The React Native app should treat this as a non-recoverable state (e.g., hide the rating UI or show a generic error).【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L39-L43】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L97-L101】
- `409 Conflict` is emitted if the order has not reached `DELIVERED` yet. Prevent this by only enabling the rating screen once the order timeline indicates delivery. If it occurs, show an inline message and allow retry after the status changes.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L44-L45】
- `404 Not Found` appears when an invalid `orderId` is supplied or the order no longer exists. Display an inline toast and navigate back to the orders list.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L39-L40】
- Duplicate submissions for the same order simply update the stored feedback; no extra handling is required on the client.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L51-L87】
- When `GET` returns `204 No Content`, render the unrated state (e.g., prompt to pick thumbs up/down). This response means the order is eligible for rating but no vote has been stored yet.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L33-L43】

## React Native Integration Steps

1. **Eligibility Check** – Gate the rating button on the order detail screen so it only appears when `order.status === 'DELIVERED'` and the authenticated user is the original purchaser. This matches backend enforcement to avoid unnecessary failures.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L41-L46】
2. **Prefill Existing Feedback** – When navigating to the rating screen, call the `GET` endpoint. If it returns `200`, populate the UI with the existing `thumbsUp` value and `comments`. If `204`, keep the form empty. Cache the `restaurantAverageRating`, `totalRatings`, and thumb counts to refresh any previously stored restaurant summaries.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L33-L43】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L103-L125】
3. **Submit Flow** – On form submission, POST the body shown above. Treat `201` as success, close the modal/screen, and surface a success toast. Update local caches (order history, restaurant cards, favorites) with the returned `restaurantAverageRating`/`totalRatings`/thumb counts so the UI reflects the new aggregate immediately.【F:src/main/java/com/foodify/server/modules/restaurants/api/RestaurantRatingController.java†L21-L31】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L63-L87】
4. **Error Messaging** – Map `400`, `403`, `404`, and `409` responses to user-friendly copy as described above. For transient network errors, allow retry without clearing the in-progress rating.
5. **Optimistic Updates (Optional)** – If you optimistically update the local restaurant rating, make sure to reconcile with the server response to respect the rounded average, counts, and thumb breakdown returned by the backend.【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L63-L125】

## Data Modeling in the App

- Extend the order detail slice/state with an optional `restaurantRating` object mirroring `RestaurantRatingResponse` so both GET and POST handlers can reuse the same TypeScript interface.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingResponse.java†L5-L18】
- Update any restaurant list/detail caches to store `averageRating`, `totalRatings`, and the thumb counts so the UI can render strings like “4.6 ★ (90👍 / 38👎)”. Backend `Restaurant` entities already expose a nullable `rating` field that stays in sync when the aggregate changes.【F:src/main/java/com/foodify/server/modules/restaurants/domain/Restaurant.java†L28-L74】【F:src/main/java/com/foodify/server/modules/restaurants/application/RestaurantRatingService.java†L68-L83】
- Keep the comment field limited to 1,024 characters on the client to match the server constraint and avoid validation failures.【F:src/main/java/com/foodify/server/modules/restaurants/dto/RestaurantRatingRequest.java†L6-L9】

## Database Note (FYI)

The `restaurant_ratings` table enforces one rating per order (unique `order_id`) and cascades deletes with the associated restaurant, client, or order records. This ensures the mobile app never has to manage duplicate entries or dangling feedback records when orders are cleaned up.【F:src/main/resources/db/migration/V9__create_restaurant_ratings.sql†L1-L16】
