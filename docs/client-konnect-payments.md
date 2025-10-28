# Konnect Card Payment Flow (Client App)

This note documents the JSON payloads that the consumer-facing UI should handle when a customer pays online via Konnect. The structures match the DTOs emitted by the server so they can be bound directly in the app.

## Order creation response (`POST /api/orders/create`)

When a client places an order, the REST endpoint returns a `CreateOrderResponse`. The `payment` block carries both the pricing breakdown and the Konnect metadata required for online payments.

### Example: card payment
```json
{
  "orderId": 4821,
  "status": "PENDING",
  "restaurant": {
    "id": 17,
    "name": "Foodify Test Kitchen",
    "imageUrl": "https://cdn.foodify.test/restaurants/17/cover.png",
    "iconUrl": "https://cdn.foodify.test/restaurants/17/icon.png"
  },
  "delivery": {
    "address": "123 Main St",
    "location": {
      "lat": 33.5899,
      "lng": -7.6039
    },
    "savedAddress": null
  },
  "payment": {
    "method": "CARD",
    "subtotal": 210.00,
    "extrasTotal": 15.00,
    "total": 235.00,
    "itemsSubtotal": 210.00,
    "promotionDiscount": 0.00,
    "couponDiscount": 0.00,
    "itemsTotal": 225.00,
    "deliveryFee": 10.00,
    "status": "pending",
    "paymentUrl": "https://sandbox.konnect.network/pay/6ce730",
    "paymentReference": "PG-9B9E09",
    "environment": "sandbox"
  },
  "couponCode": null,
  "items": [/* ... */],
  "workflow": [/* ... */]
}
```

Handle card payments by:

1. Detecting that the client selected the `CARD` (or another online synonym) method.
2. Checking that `payment.paymentUrl` is non-null. If present, open it in a web view or the system browser so the customer can complete the Konnect checkout.
3. Displaying `payment.environment` to indicate whether the customer is in `sandbox` or `production`. This value mirrors the server configuration and helps you label test versus live flows.
4. Storing `payment.paymentReference` to display it in receipts or to surface to support staff if the customer reports issues.

The initial `payment.status` is `pending`. Once Konnect hits our webhook endpoint (`GET /api/payments/konnect/webhook?payment_ref=...`) the server refreshes the order with the latest gateway status so the UI can react to changes (e.g., mark the payment as `paid`, `failed`, or `expired`).

### Example: cash payment
```json
{
  "payment": {
    "method": "CASH",
    "subtotal": 195.00,
    "extrasTotal": 0.00,
    "total": 195.00,
    "itemsSubtotal": 195.00,
    "promotionDiscount": 0.00,
    "couponDiscount": 0.00,
    "itemsTotal": 195.00,
    "deliveryFee": 0.00,
    "status": null,
    "paymentUrl": null,
    "paymentReference": null,
    "environment": null
  }
}
```

For cash (or any non-online method), both `paymentUrl` and `paymentReference` are `null`, and the app can move straight to the order-tracking UI.

## Ongoing order polling and push updates

The client app should continue to poll `GET /api/orders/ongoing` or subscribe to the WebSocket channel so the user sees payment updates after redirecting back from Konnect. Both transports reuse the `OrderNotificationDTO` schema, which embeds the same payment structure (including `status`, `paymentUrl`, `paymentReference`, and `environment`).

A payment that succeeds will eventually flip to `status: "paid"`, while failures surface as `failed` and timeouts as `expired`. The UI should watch for these transitions and update the user accordingly.

## Sandbox vs. production behavior

Konnect configuration can point to either the sandbox or production environment. The `environment` string included in responses is the exact name of the active configuration, enabling the UI to:

- Show “Test payment” messaging in sandbox sessions.
- Route customers back to the proper in-app screen once the payment finishes.
- Include the `paymentReference` in support tickets so the operations team can reconcile transactions.

No additional UI changes are required to switch environments; the server toggles the configuration centrally and propagates the current environment via the existing payloads.
