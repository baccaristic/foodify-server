# Admin Driver Management API Endpoints

This document describes all the implemented admin driver management endpoints.

## Base URL
All endpoints are under: `/api/admin/drivers/management`

All endpoints require `ROLE_ADMIN` authentication.

---

## Dashboard & Overview Endpoints

### 1. Get Today's Revenue
**GET** `/revenue/today`

Returns today's revenue with percentage change from yesterday.

**Response:**
```json
{
  "todayRevenue": 1500.00,
  "yesterdayRevenue": 1200.00,
  "percentageChange": 25.0
}
```

---

### 2. Get Active Drivers
**GET** `/drivers/active`

Returns count of active (online) drivers vs total drivers.

**Response:**
```json
{
  "activeDriversCount": 45,
  "totalDriversCount": 100
}
```

---

### 3. Get Daily Membership Collection
**GET** `/membership/collection`

Returns daily membership collection status.

**Response:**
```json
{
  "totalCollected": 450.00,
  "expectedTotal": 500.00,
  "collectionPercentage": 90.0
}
```

---

## Driver Management Endpoints

### 4. Get Drivers (Paginated with Filters)
**GET** `/drivers?query={query}&paymentStatus={true|false}&isOnline={true|false}&page={page}&size={size}`

**Query Parameters:**
- `query` (optional): Search by driver ID, name, or phone
- `paymentStatus` (optional): Filter by payment status (true = paid up, false = has outstanding fees)
- `isOnline` (optional): Filter by online status
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "+1234567890",
      "isOnline": true,
      "paymentStatus": true,
      "averageRating": 4.5,
      "totalOrders": 150,
      "unpaidEarnings": 0.00,
      "outstandingDailyFees": 0.00
    }
  ],
  "totalElements": 100,
  "totalPages": 5,
  "number": 0
}
```

---

## Driver Metrics Endpoints

### 5. Get Driver Rating
**GET** `/{driverId}/rating`

Returns driver's rating summary.

**Response:**
```json
{
  "driverId": 1,
  "ratingCount": 150,
  "timingAverage": 4.5,
  "foodConditionAverage": 4.6,
  "professionalismAverage": 4.7,
  "overallAverage": 4.6
}
```

---

### 6. Get Total Orders
**GET** `/{driverId}/orders/total`

Returns total number of completed orders.

**Response:** `150` (number)

---

### 7. Get On-Time Orders
**GET** `/{driverId}/orders/on-time`

Returns number of on-time deliveries.

**Response:** `140` (number)

---

### 8. Get Canceled Orders
**GET** `/{driverId}/orders/canceled`

Returns number of canceled orders.

**Response:** `5` (number)

---

## Driver Details Endpoints

### 9. Get Driver Details
**GET** `/{driverId}/details`

Returns comprehensive driver information.

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "isOnline": true,
  "averageRating": 4.6,
  "totalOrders": 150,
  "onTimeOrders": 140,
  "canceledOrders": 5,
  "unpaidEarnings": 100.00,
  "outstandingDailyFees": 0.00,
  "lastDailyFeeDate": "2025-11-04",
  "joinedAt": null
}
```

---

### 10. Get Current Status
**GET** `/{driverId}/status`

Returns whether driver is online or offline.

**Response:** `true` or `false` (boolean)

---

### 11. Get Current Task
**GET** `/{driverId}/task/current`

Returns driver's current active order (if any).

**Response:**
```json
{
  "orderId": 123,
  "status": "IN_DELIVERY",
  "restaurantName": "Pizza Palace",
  "deliveryAddress": "123 Main St",
  "estimatedDeliveryTime": "2025-11-04T14:30:00",
  "restaurantLatitude": 40.7128,
  "restaurantLongitude": -74.0060,
  "deliveryLatitude": 40.7589,
  "deliveryLongitude": -73.9851
}
```

Returns 204 No Content if driver has no active task.

---

### 12. Get Current Location
**GET** `/{driverId}/location/current`

Returns driver's last known location.

**Response:**
```json
{
  "driverId": 1,
  "latitude": 40.7128,
  "longitude": -74.0060,
  "lastUpdated": "2025-11-04T13:18:24"
}
```

Returns 204 No Content if location is not available.

---

## Performance Metrics Endpoints

### 13. Get Delivery Metrics
**GET** `/{driverId}/metrics/delivery`

Returns comprehensive delivery performance metrics.

**Response:**
```json
{
  "acceptanceRate": 95.0,
  "completionRate": 98.0,
  "onTimeRate": 93.3,
  "avgDeliveryDuration": 28.5,
  "totalOffered": 150,
  "totalAccepted": 150,
  "totalCompleted": 150,
  "totalOnTime": 140
}
```

---

### 14. Get Monthly Statistics
**GET** `/{driverId}/stats/monthly`

Returns daily statistics for the last 30 days.

**Response:**
```json
{
  "dailyStats": [
    {
      "date": "2025-11-04",
      "averageRating": 4.6,
      "onTimePercentage": 95.0
    },
    {
      "date": "2025-11-03",
      "averageRating": 4.5,
      "onTimePercentage": 92.0
    }
  ]
}
```

---

## Ratings Endpoints

### 15. Get Customer Ratings
**GET** `/{driverId}/ratings/customer?page={page}&size={size}`

Returns paginated customer ratings for the driver.

**Query Parameters:**
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Response:**
```json
{
  "content": [
    {
      "orderId": 123,
      "deliveryId": 456,
      "driverId": 1,
      "clientId": 789,
      "clientName": "Jane Smith",
      "timing": 5,
      "foodCondition": 5,
      "professionalism": 5,
      "overall": 5,
      "comments": "Great service!",
      "createdAt": "2025-11-04T12:00:00",
      "updatedAt": "2025-11-04T12:00:00"
    }
  ],
  "totalElements": 150,
  "totalPages": 8
}
```

---

### 16. Get Rating Distribution
**GET** `/{driverId}/ratings/distribution`

Returns distribution of ratings by star count.

**Response:**
```json
{
  "oneStarCount": 2,
  "twoStarCount": 3,
  "threeStarCount": 10,
  "fourStarCount": 45,
  "fiveStarCount": 90,
  "totalRatings": 150
}
```

---

## Financial Endpoints

### 17. Get Shift History
**GET** `/{driverId}/shifts/history?date={date}&page={page}&size={size}`

Returns shift history for a specific date with order details.

**Query Parameters:**
- `date` (required): Date in ISO format (yyyy-MM-dd)
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Response:**
```json
{
  "content": [
    {
      "shiftId": 1,
      "startTime": "2025-11-04T08:00:00",
      "endTime": "2025-11-04T16:00:00",
      "totalEarnings": 250.00,
      "totalOrders": 15,
      "orders": [
        {
          "orderId": 123,
          "restaurantName": "Pizza Palace",
          "status": "DELIVERED",
          "earnings": 15.00,
          "deliveryTime": "2025-11-04T09:30:00"
        }
      ]
    }
  ]
}
```

---

### 18. Get Today's Earnings
**GET** `/{driverId}/earnings/today`

Returns breakdown of today's earnings.

**Response:**
```json
{
  "totalEarnings": 250.00,
  "cashEarnings": 75.00,
  "cardEarnings": 175.00,
  "commission": 37.50,
  "numberOfDeliveries": 15
}
```

---

### 19. Get Daily Subscription Due Date
**GET** `/{driverId}/subscription/due-date`

Returns information about daily subscription payments.

**Response:**
```json
{
  "lastPaymentDate": "2025-11-03",
  "nextDueDate": "2025-11-04",
  "daysPastDue": 0,
  "amountDue": 0.00
}
```

---

### 20. Create Payment
**POST** `/{driverId}/payments`

Creates a payment record from a driver.

**Request Body:**
```json
{
  "amount": 100.00,
  "paymentMethod": "CASH",
  "notes": "Daily fee payment"
}
```

**Response:**
```json
{
  "id": 1,
  "driverId": 1,
  "driverName": "John Doe",
  "driverPhone": "+1234567890",
  "depositAmount": 100.00,
  "earningsPaid": 0.00,
  "feesDeducted": 0.00,
  "status": "PENDING",
  "createdAt": "2025-11-04T13:18:24",
  "confirmedAt": null
}
```

---

### 21. Get Payment History
**GET** `/{driverId}/payments/history?page={page}&size={size}`

Returns paginated payment history for a driver.

**Query Parameters:**
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "amount": 100.00,
      "paymentMethod": "CASH",
      "notes": "Deposit",
      "createdAt": "2025-11-04T13:18:24",
      "confirmedByAdminName": "Admin User"
    }
  ],
  "totalElements": 50,
  "totalPages": 3
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 401 Unauthorized
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication required"
}
```

### 403 Forbidden
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied. Admin role required."
}
```

### 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Driver not found with id: 123"
}
```

### 400 Bad Request
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request parameters"
}
```

---

## Notes

1. **Pagination**: All paginated endpoints support standard Spring Data pagination parameters (page, size).
2. **Date Format**: Dates in query parameters should be in ISO-8601 format (yyyy-MM-dd).
3. **Authentication**: All endpoints require a valid JWT token with ROLE_ADMIN authority.
4. **Rate Limiting**: Endpoints may be subject to rate limiting based on application configuration.
5. **Caching**: Location data is cached in Redis for performance.
6. **Known Limitations**:
   - Cash/Card earnings breakdown is estimated based on configurable ratios
   - Acceptance rate tracking requires additional order assignment tracking
   - joinedAt field is not currently available in the Driver entity
