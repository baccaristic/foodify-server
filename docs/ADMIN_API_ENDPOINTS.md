# Admin API Endpoints Documentation

This document provides a comprehensive overview of all admin endpoints available in the Foodify Server API. All endpoints require `ROLE_ADMIN` authentication.

## Table of Contents

1. [Driver Management](#driver-management)
2. [Driver Finance](#driver-finance)
3. [Driver Ratings](#driver-ratings)
4. [Restaurant Management](#restaurant-management)
5. [Restaurant Operations](#restaurant-operations)
6. [Service Fee Management](#service-fee-management)

---

## Driver Management

Base Path: `/api/admin/drivers/management`

### Dashboard & Overview Endpoints

#### 1. Get Today's Revenue
**GET** `/revenue/today`

Get today's revenue with percentage change from yesterday for all drivers.

**Input:** None

**Output:**
```json
{
  "todayRevenue": 2500.00,
  "yesterdayRevenue": 2000.00,
  "percentageChange": 25.0
}
```

---

#### 2. Get Active Drivers Count
**GET** `/drivers/active`

Get count of currently active (online) drivers.

**Input:** None

**Output:**
```json
{
  "activeDriversCount": 45,
  "totalDriversCount": 100
}
```

---

#### 3. Get Daily Membership Collection
**GET** `/membership/collection`

Get daily membership fee collection status and percentage.

**Input:** None

**Output:**
```json
{
  "totalCollected": 450.00,
  "expectedTotal": 500.00,
  "collectionPercentage": 90.0
}
```

---

### Driver Listing & Filtering

#### 4. Get Drivers (Paginated with Filters)
**GET** `/drivers`

Get paginated list of drivers with advanced filtering.

**Input (Query Parameters):**
- `query` (optional): Search by driver ID, name, or phone
- `paymentStatus` (optional, boolean): Filter by payment status (true = paid, false = outstanding)
- `isOnline` (optional, boolean): Filter by online status
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Output:**
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
      "averageRating": 4.6,
      "totalOrders": 150,
      "unpaidEarnings": 100.00,
      "outstandingDailyFees": 0.00
    }
  ],
  "totalElements": 100,
  "totalPages": 5,
  "number": 0
}
```

---

### Driver Metrics

#### 5. Get Driver Rating
**GET** `/{driverId}/rating`

Get driver's rating summary including all rating categories.

**Input:** 
- `driverId` (path parameter): Driver ID

**Output:**
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

#### 6. Get Total Orders
**GET** `/{driverId}/orders/total`

Get total number of completed orders by driver.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:** Number (e.g., `150`)

---

#### 7. Get On-Time Orders
**GET** `/{driverId}/orders/on-time`

Get number of on-time deliveries by driver.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:** Number (e.g., `140`)

---

#### 8. Get Canceled Orders
**GET** `/{driverId}/orders/canceled`

Get number of canceled orders by driver.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:** Number (e.g., `5`)

---

### Driver Details

#### 9. Get Driver Details
**GET** `/{driverId}/details`

Get comprehensive driver information including all metrics.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
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

#### 10. Get Current Status
**GET** `/{driverId}/status`

Get driver's current online/offline status.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:** Boolean (e.g., `true` for online, `false` for offline)

---

#### 11. Get Current Task
**GET** `/{driverId}/task/current`

Get driver's current active order/task.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
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

#### 12. Get Current Location
**GET** `/{driverId}/location/current`

Get driver's last known GPS location (from Redis cache).

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
```json
{
  "driverId": 1,
  "latitude": 40.7128,
  "longitude": -74.0060,
  "lastUpdated": "2025-11-04T13:18:24"
}
```

Returns 204 No Content if location not available.

---

### Performance Metrics

#### 13. Get Delivery Metrics
**GET** `/{driverId}/metrics/delivery`

Get comprehensive delivery performance metrics.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
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

#### 14. Get Monthly Statistics
**GET** `/{driverId}/stats/monthly`

Get daily statistics for the last 30 days including ratings and on-time percentages.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
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

### Customer Ratings

#### 15. Get Customer Ratings (Paginated)
**GET** `/{driverId}/ratings/customer`

Get paginated customer ratings for a driver.

**Input:**
- `driverId` (path parameter): Driver ID
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Output:**
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

#### 16. Get Rating Distribution
**GET** `/{driverId}/ratings/distribution`

Get distribution of ratings by star count (1-5 stars).

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
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

### Financial Operations

#### 17. Get Shift History
**GET** `/{driverId}/shifts/history`

Get shift history for a specific date with order details.

**Input:**
- `driverId` (path parameter): Driver ID
- `date` (required): Date in ISO format (yyyy-MM-dd)
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Output:**
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

#### 18. Get Today's Earnings
**GET** `/{driverId}/earnings/today`

Get breakdown of today's earnings including cash, card, and commission.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
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

#### 19. Get Daily Subscription Due Date
**GET** `/{driverId}/subscription/due-date`

Get information about daily subscription payments and due dates.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
```json
{
  "lastPaymentDate": "2025-11-03",
  "nextDueDate": "2025-11-04",
  "daysPastDue": 0,
  "amountDue": 0.00
}
```

---

#### 20. Create Payment
**POST** `/{driverId}/payments`

Record a payment from a driver.

**Input:**
- `driverId` (path parameter): Driver ID
- Request Body:
```json
{
  "amount": 100.00,
  "paymentMethod": "CASH",
  "notes": "Daily fee payment"
}
```

**Output:**
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

#### 21. Get Payment History
**GET** `/{driverId}/payments/history`

Get paginated payment history for a driver.

**Input:**
- `driverId` (path parameter): Driver ID
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Output:**
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

## Driver Finance

Base Path: `/api/admin/drivers`

### 1. Get Driver Financial Summary
**GET** `/{driverId}/finance`

Get comprehensive financial summary for a driver.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
```json
{
  "cashOnHand": 500.00,
  "unpaidEarnings": 100.00,
  "outstandingDailyFees": 15.00,
  "depositThreshold": 1000.00,
  "depositRequired": false,
  "hasPendingDeposit": false,
  "nextPayoutAmount": 100.00,
  "feesToDeduct": 15.00
}
```

---

### 2. Preview Driver Deposit
**GET** `/{driverId}/finance/deposits/confirm`

Preview deposit details before confirming.

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
```json
{
  "driverId": 1,
  "depositAmount": 500.00,
  "earningsToPay": 100.00,
  "feesToDeduct": 15.00,
  "netPayout": 85.00
}
```

---

### 3. List All Deposits
**GET** `/deposits`

Get list of all driver deposits, optionally filtered by status.

**Input:**
- `status` (optional): Filter by deposit status (PENDING, CONFIRMED)

**Output:**
```json
[
  {
    "id": 1,
    "driverId": 1,
    "driverName": "John Doe",
    "driverPhone": "+1234567890",
    "depositAmount": 500.00,
    "earningsPaid": 100.00,
    "feesDeducted": 15.00,
    "status": "PENDING",
    "createdAt": "2025-11-04T13:00:00",
    "confirmedAt": null
  }
]
```

---

### 4. Confirm Driver Deposit
**POST** `/{driverId}/finance/deposits/confirm`

Confirm a cash deposit from a driver.

**Input:**
- `driverId` (path parameter): Driver ID
- Authentication context (admin user)

**Output:** Same as deposit details

---

### 5. Confirm Deposit by ID
**POST** `/deposits/{depositId}/confirm`

Confirm a specific deposit by its ID.

**Input:**
- `depositId` (path parameter): Deposit ID
- Authentication context (admin user)

**Output:** Deposit details

---

### 6. Record Daily Fee Payment
**POST** `/{driverId}/finance/daily-fees/pay`

Record a daily fee payment from a driver.

**Input:**
- `driverId` (path parameter): Driver ID
- Request Body:
```json
{
  "daysPaid": 1
}
```

**Output:** Updated financial summary

---

## Driver Ratings

Base Path: `/api/admin/drivers`

### 1. Get Driver Rating Summary
**GET** `/{driverId}/ratings/summary`

Get rating summary for a driver (same as driver management endpoint).

**Input:**
- `driverId` (path parameter): Driver ID

**Output:**
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

### 2. Get Driver Ratings List
**GET** `/{driverId}/ratings`

Get list of ratings for a driver.

**Input:**
- `driverId` (path parameter): Driver ID
- `limit` (default: 20): Maximum number of ratings to return

**Output:** Array of rating objects

---

### 3. Override/Create Rating
**POST** `/orders/{orderId}/ratings`

Create or override a rating for an order (admin override).

**Input:**
- `orderId` (path parameter): Order ID
- Request Body:
```json
{
  "timing": 5,
  "foodCondition": 5,
  "professionalism": 5,
  "overall": 5,
  "comments": "Admin override rating"
}
```

**Output:** Rating details

---

## Restaurant Management

Base Path: `/api/admin/restaurants/management`

### Restaurant Listing

#### 1. Get Restaurants (Paginated with Filters)
**GET** `/restaurants`

Get paginated list of restaurants with filters and open/closed status.

**Input:**
- `query` (optional): Search by restaurant name or location
- `cuisine` (optional): Filter by cuisine type (e.g., ITALIAN, PIZZA)
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Output:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Pizza Palace",
      "address": "123 Main Street",
      "cuisines": ["PIZZA", "ITALIAN"],
      "isOpen": true,
      "overallRating": 4.5,
      "imageUrl": "https://example.com/image.jpg"
    }
  ],
  "totalElements": 50,
  "totalPages": 3,
  "number": 0
}
```

---

### Restaurant Metrics

#### 2. Get Today's Revenue
**GET** `/{restaurantId}/revenue/today`

Get today's revenue with percentage change from yesterday.

**Input:**
- `restaurantId` (path parameter): Restaurant ID

**Output:**
```json
{
  "todayRevenue": 2500.00,
  "yesterdayRevenue": 2000.00,
  "percentageChange": 25.0
}
```

---

#### 3. Get Total Orders
**GET** `/{restaurantId}/orders/total`

Get today's total orders with percentage change from yesterday.

**Input:**
- `restaurantId` (path parameter): Restaurant ID

**Output:**
```json
{
  "todayOrders": 45,
  "yesterdayOrders": 40,
  "percentageChange": 12.5
}
```

---

#### 4. Get Average Order Value
**GET** `/{restaurantId}/orders/avg-value`

Get today's average order value with percentage change from yesterday.

**Input:**
- `restaurantId` (path parameter): Restaurant ID

**Output:**
```json
{
  "todayAvgOrderValue": 55.56,
  "yesterdayAvgOrderValue": 50.00,
  "percentageChange": 11.12
}
```

---

#### 5. Get Average Rating
**GET** `/{restaurantId}/rating/avg`

Get today's average rating with percentage change from yesterday.

**Input:**
- `restaurantId` (path parameter): Restaurant ID

**Output:**
```json
{
  "todayAvgRating": 92.5,
  "yesterdayAvgRating": 87.5,
  "percentageChange": 5.71,
  "todayRatingCount": 40,
  "yesterdayRatingCount": 32
}
```

Note: Rating is calculated as percentage of thumbs-up ratings.

---

### Revenue Analysis

#### 6. Get Revenue Per Day
**GET** `/{restaurantId}/revenue/daily`

Get daily revenue breakdown between two dates.

**Input:**
- `restaurantId` (path parameter): Restaurant ID
- `startDate` (required): Start date in ISO format (yyyy-MM-dd)
- `endDate` (required): End date in ISO format (yyyy-MM-dd)

**Output:**
```json
[
  {
    "date": "2025-11-01",
    "revenue": 2500.00,
    "orderCount": 45
  },
  {
    "date": "2025-11-02",
    "revenue": 2800.00,
    "orderCount": 52
  }
]
```

---

### Items & Menu

#### 7. Get Top Selling Items
**GET** `/{restaurantId}/items/top-selling`

Get the most sold items for the current day.

**Input:**
- `restaurantId` (path parameter): Restaurant ID
- `limit` (default: 10, max: 50): Number of top items

**Output:**
```json
[
  {
    "itemId": 123,
    "itemName": "Margherita Pizza",
    "orderCount": 25,
    "price": 12.99,
    "imageUrl": "https://example.com/pizza.jpg"
  }
]
```

---

#### 8. Get Menu Items
**GET** `/{restaurantId}/items`

Get paginated menu items with availability and category filter.

**Input:**
- `restaurantId` (path parameter): Restaurant ID
- `category` (optional): Filter by menu category name
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Output:**
```json
{
  "content": [
    {
      "id": 123,
      "name": "Margherita Pizza",
      "description": "Classic tomato and mozzarella pizza",
      "price": 12.99,
      "available": true,
      "categories": ["Pizza", "Vegetarian"],
      "imageUrl": "https://example.com/pizza.jpg",
      "hasPromotion": false,
      "promotionPrice": null
    }
  ],
  "totalElements": 50,
  "totalPages": 3
}
```

---

### Orders History

#### 9. Get Orders History
**GET** `/{restaurantId}/orders/history`

Get orders history for a specific day with pagination and filters.

**Input:**
- `restaurantId` (path parameter): Restaurant ID
- `date` (required): Date in ISO format (yyyy-MM-dd)
- `status` (optional): Filter by order status
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Output:** Paginated list of Order objects

**Available Order Statuses:**
PENDING, ACCEPTED, PREPARING, READY_FOR_PICK_UP, IN_DELIVERY, DELIVERED, REJECTED, CANCELED

---

## Restaurant Operations

Base Path: `/api/admin`

### 1. Add Restaurant
**POST** `/restaurant/add` or `/restaurants`

Create a new restaurant with images.

**Input:**
- `restaurant` (multipart/form-data): RestaurantDto JSON
- `image` (optional, file): Restaurant image
- `icon` (optional, file): Restaurant icon

**Output:** Created Restaurant object

---

### 2. Update Restaurant
**PUT** `/restaurants/{restaurantId}`

Update an existing restaurant.

**Input:**
- `restaurantId` (path parameter): Restaurant ID
- `restaurant` (multipart/form-data): RestaurantDto JSON
- `image` (optional, file): New restaurant image
- `icon` (optional, file): New restaurant icon

**Output:** Updated Restaurant object

---

## Service Fee Management

Base Path: `/api/admin/service-fee`

### 1. Update Service Fee
**PUT** `/`

Update the global service fee configuration.

**Input:**
- Request Body:
```json
{
  "feePercentage": 0.15,
  "minimumFee": 1.00,
  "maximumFee": 10.00
}
```

**Output:**
```json
{
  "id": 1,
  "feePercentage": 0.15,
  "minimumFee": 1.00,
  "maximumFee": 10.00,
  "updatedBy": "admin@example.com",
  "updatedAt": "2025-11-04T13:18:24"
}
```

---

## Authentication

All endpoints require:
- Valid JWT token in Authorization header: `Bearer <token>`
- User must have `ROLE_ADMIN` authority

## Error Responses

All endpoints may return:

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
  "message": "Resource not found with id: 123"
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

1. **Date Format**: All dates should be in ISO-8601 format (yyyy-MM-dd)
2. **Pagination**: All paginated endpoints support `page` and `size` parameters
3. **Filtering**: Filter parameters are optional and can be combined
4. **Authentication**: All endpoints require valid JWT token with ROLE_ADMIN
5. **Rate Limiting**: Endpoints may be subject to rate limiting
6. **Caching**: Location data is cached in Redis for performance
7. **Time Zone**: All date/time calculations use server's local time zone

---

## Summary

**Total Endpoints: 42**

- **Driver Management**: 21 endpoints
- **Driver Finance**: 6 endpoints
- **Driver Ratings**: 3 endpoints
- **Restaurant Management**: 9 endpoints
- **Restaurant Operations**: 2 endpoints
- **Service Fee Management**: 1 endpoint

All endpoints are consolidated in the `com.foodify.server.modules.admin.api` package for centralized admin operations management.
