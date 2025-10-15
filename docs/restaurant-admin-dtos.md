# Restaurant Admin DTO Reference

This appendix lists the JSON payloads produced and consumed by the restaurant-facing APIs and WebSocket feeds. Structures map directly to the DTO classes exposed by the Foodify server and are safe to use for serialization in other clients.

## OrderNotificationDTO (WebSocket & REST)
```json
{
  "orderId": 123,
  "deliveryAddress": "123 Main St",
  "paymentMethod": "CARD",
  "date": "2024-05-27T15:23:54.123",
  "items": [OrderItemDTO],
  "savedAddress": SavedAddressSummaryDto,
  "client": ClientSummaryDTO,
  "status": "READY_FOR_PICK_UP",
  "deliveryLocation": LocationDto,
  "restaurant": RestaurantSummary,
  "delivery": DeliverySummary,
  "payment": PaymentSummary,
  "statusHistory": [OrderStatusHistoryDTO]
}
```

The same `OrderNotificationDTO` payload is returned by the restaurant REST endpoints so that list, detail, and lifecycle actions
share an identical schema with WebSocket updates. This guarantees the restaurant dashboard always works with the full, enriched
order context regardless of transport.

### RestaurantSummary
```json
{
  "id": 42,
  "name": "Foodify Test Kitchen",
  "address": "123 Main St",
  "phone": "+15551234567",
  "imageUrl": "https://cdn.example.com/img.png",
  "location": LocationDto
}
```

### DeliverySummary
```json
{
  "id": 17,
  "driver": DriverSummary,
  "estimatedPickupTime": 1716826000,
  "estimatedDeliveryTime": 1716827800,
  "pickupTime": "2024-05-27T15:40:00",
  "deliveredTime": "2024-05-27T16:05:00",
  "driverLocation": LocationDto,
  "address": "123 Main St",
  "location": LocationDto,
  "savedAddress": SavedAddressSummaryDto
}
```

### DriverSummary
```json
{
  "id": 99,
  "name": "Alicia Rider",
  "phone": "+15559876543"
}
```

### PaymentSummary
```json
{
  "subtotal": 25.50,
  "extrasTotal": 2.50,
  "total": 30.00,
  "itemsSubtotal": 25.50,
  "promotionDiscount": 0.00,
  "itemsTotal": 28.00,
  "deliveryFee": 2.00
}
```

### OrderStatusHistoryDTO
```json
{
  "action": "ORDER_ACCEPTED",
  "previousStatus": "PENDING",
  "newStatus": "ACCEPTED",
  "changedBy": "RESTAURANT",
  "reason": null,
  "metadata": null,
  "changedAt": "2024-05-27T15:25:00"
}
```


## OrderItemDTO
```json
{
  "menuItemId": 5,
  "menuItemName": "Burger",
  "quantity": 2,
  "extras": ["Cheese", "Bacon"],
  "specialInstructions": "No onions",
  "unitBasePrice": 10.00,
  "unitPrice": 12.00,
  "unitExtrasPrice": 2.00,
  "lineSubtotal": 20.00,
  "promotionDiscount": 0.00,
  "lineItemsTotal": 24.00,
  "extrasTotal": 4.00,
  "lineTotal": 24.00
}
```

## MenuItemRequestDTO (multipart `menu` JSON)
```json
{
  "id": 13,
  "name": "Chicken Sandwich",
  "description": "Buttermilk fried chicken",
  "price": 11.5,
  "category": "Sandwiches",
  "popular": true,
  "restaurantId": 42,
  "promotionLabel": "Lunch Special",
  "promotionPrice": 9.99,
  "promotionActive": true,
  "imageUrls": ["https://cdn.example.com/menu/chicken-sandwich.png"],
  "optionGroups": [OptionGroupDTO]
}
```

### OptionGroupDTO
```json
{
  "id": 7,
  "name": "Choose your sides",
  "minSelect": 1,
  "maxSelect": 2,
  "required": true,
  "extras": [ExtraDTO]
}
```

### ExtraDTO
```json
{
  "id": 3,
  "name": "Fries",
  "price": 0.0,
  "isDefault": true
}
```

## Supporting DTOs

### LocationDto
```json
{
  "lat": 37.7749,
  "lng": -122.4194
}
```

### ClientSummaryDTO
```json
{
  "id": 9001,
  "name": "Jane Customer"
}
```

### SavedAddressSummaryDto
```json
{
  "id": "7c6c34e3-7c2e-4bb5-a3f2-0acbd4c54321",
  "type": "HOME",
  "label": "Apartment",
  "formattedAddress": "123 Main St, City, ST",
  "placeId": "place-id-123",
  "entrancePreference": "LOBBY",
  "entranceNotes": "Dial 123",
  "directions": "Use north entrance",
  "notes": "Leave at door",
  "primary": true
}
```

These JSON structures can be used as direct serialization references for the restaurant client. Fields marked with IDs are numeric unless otherwise specified, timestamps are ISO-8601 strings, and monetary values are decimals.
