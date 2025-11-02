# Order tipping and cash collection preferences

Foodify clients can optionally specify a gratuity percentage and declare the amount of cash they will hand to the driver when placing an order. This guide documents the API surface for mobile and web clients so the experiences stay in sync with the server.

## Checkout request (`POST /api/orders/create`)

Extend the `OrderRequest` payload with the following optional fields:

| Field | Type | Description |
| --- | --- | --- |
| `tipPercentage` | number (decimal) | Percentage-based tip to add on top of the order total. The server clamps the value between `0` and `100` before calculating the gratuity amount. |
| `cashToCollect` | number (decimal) | Explicit cash amount the customer will provide to the driver. Use this to display change due states in the driver app. |

Both values accept up to two decimal places. The request validator rejects negative numbers and percentages above 100%.【F:src/main/java/com/foodify/server/modules/orders/dto/OrderRequest.java†L17-L38】

## Order pricing changes

After applying coupons and delivery fees, the order service calculates tip totals and persists both the percentage and final amount on the order. The overall `order.total` now includes the gratuity so clients should display that number as the amount charged to the customer. The declared cash value is stored alongside the order for downstream workflows.【F:src/main/java/com/foodify/server/modules/orders/application/CustomerOrderService.java†L298-L424】

## Response payloads

`CreateOrderResponse`, `OrderDto`, and `OrderNotificationDTO` expose the tip metadata and cash declaration inside their payment summaries so all clients share consistent UI states:

- `tipPercentage` – normalized percentage applied to the order.
- `tipAmount` – monetary value of the tip.
- `totalBeforeTip` – subtotal prior to gratuity so UIs can show a split breakdown.
- `cashToCollect` – amount of cash the courier expects to collect.

Update local models and serializers to include these fields when reading order payloads.【F:src/main/java/com/foodify/server/modules/orders/dto/OrderDto.java†L29-L41】【F:src/main/java/com/foodify/server/modules/orders/dto/OrderNotificationDTO.java†L11-L63】【F:src/main/java/com/foodify/server/modules/orders/dto/response/CreateOrderResponse.java†L9-L46】

## Database migration

The tipping feature adds three nullable columns to the `orders` table: `tip_percentage`, `tip_amount`, and `cash_to_collect`. Run Flyway migration `V13__add_tip_and_cash_preferences.sql` as part of your deployment to make the schema compatible.【F:src/main/resources/db/migration/V13__add_tip_and_cash_preferences.sql†L1-L4】
