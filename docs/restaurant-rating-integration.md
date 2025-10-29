# Restaurant Rating API (Client Quickstart)

Foodify clients rate restaurants with a thumbs up or thumbs down once an order is delivered. The server converts those votes into a 5-star average and keeps restaurant aggregates in sync. This page lists the endpoints, payloads, and core client behaviors.

## Endpoints

All routes need the standard `Authorization: Bearer <accessToken>` header and only work for the authenticated client’s delivered orders.

| Action | Method & Path | Success | Empty State |
| --- | --- | --- | --- |
| Create or replace the rating for an order | `POST /api/client/restaurants/orders/{orderId}/ratings` | `201 Created` + `RestaurantRatingResponse` | — |
| Read the rating for an order | `GET /api/client/restaurants/orders/{orderId}/ratings` | `200 OK` + `RestaurantRatingResponse` | `204 No Content` when no rating exists |

## Payload definitions

### Request body — `RestaurantRatingRequest`

| Field | Type | Required | Notes |
| --- | --- | --- | --- |
| `thumbsUp` | boolean | ✅ | `true` = thumbs up, `false` = thumbs down. Any other value returns `400 Bad Request`. |
| `comments` | string | ❌ | Optional free text (≤ 1,024 chars). Trim or send `null` when empty. |

Example:

```json
{
  "thumbsUp": true,
  "comments": "Delivery was fast and the food was hot!"
}
```

### Response body — `RestaurantRatingResponse`

Returned for both `POST` and `GET` (when content exists).

| Field | Type | Notes |
| --- | --- | --- |
| `id` | number | Rating identifier. |
| `restaurantId` | number | Restaurant the order belongs to. |
| `orderId` | number | Order that was rated. |
| `clientId` | number | Authenticated user. |
| `thumbsUp` | boolean | Latest vote for this order. |
| `comments` | string or `null` | Stored text feedback. |
| `restaurantAverageRating` | number or `null` | 0.0–5.0 scale, rounded to 1 decimal. `null` when the restaurant has no votes. |
| `totalRatings` | number | Count of votes for the restaurant. |
| `thumbsUpCount` | number | Up-votes contributing to the average. |
| `thumbsDownCount` | number | Down-votes contributing to the average. |
| `createdAt` | string | ISO timestamp when the rating was first stored. |
| `updatedAt` | string | ISO timestamp of the most recent update. |

Example:

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

## Client flow checklist

1. **Show the UI only for delivered orders.** The server rejects other statuses with `409 Conflict`.
2. **Prefill by calling `GET`.** Use `200` responses to populate the form; treat `204` as “no rating yet”.
3. **Submit with `POST`.** A successful response (201) echoes the rating and the updated restaurant aggregates—refresh any local caches with those values.
4. **Handle errors.**
   - `400` validation error → highlight the form input.
   - `403` unauthorized order → hide or disable the rating screen.
   - `404` unknown order → show a toast and navigate back.
   - `409` not delivered → prompt the user to try again after delivery.

## Data hints

- Store the response shape in state so both endpoints share the same TypeScript model.
- Display the aggregate counts alongside the star rating (e.g., “4.6 ★ · 90👍 / 38👎”) so users understand how the score was derived.
- Keep comment inputs within 1,024 characters to match the server constraint.
