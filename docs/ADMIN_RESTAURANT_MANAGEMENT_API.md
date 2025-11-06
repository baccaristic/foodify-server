# Admin Restaurant Management API Endpoints

This document describes all the implemented admin restaurant management endpoints.

## Base URL
All endpoints are under: `/api/admin/restaurants/management`

All endpoints require `ROLE_ADMIN` authentication.

---

## Restaurant Listing & Search

### 1. Get Restaurants (Paginated with Filters)
**GET** `/restaurants?query={query}&cuisine={cuisine}&page={page}&size={size}`

Returns paginated list of restaurants with open/closed status.

**Query Parameters:**
- `query` (optional): Search by restaurant name or location/address
- `cuisine` (optional): Filter by cuisine type (e.g., ITALIAN, ASIAN, PIZZA)
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Response:**
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

**Available Cuisines:**
ASIAN, BAKERY, BREAKFAST, BURGERS, CHICKEN, FAST_FOOD, GRILL, ICE_CREAM, INDIAN, INTERNATIONAL, ITALIAN, MEXICAN, ORIENTAL, PASTA, PIZZA, SALDAS, SADWICH, SEAFOOD, SNACKS, SUSHI, SWEETS, TACOS, TEA_COFFEE, TRADITIONAL, TUNISIAN, TURKISH

---

## Restaurant Metrics Endpoints

### 2. Get Today's Revenue
**GET** `/{restaurantId}/revenue/today`

Returns today's revenue with percentage change from yesterday.

**Response:**
```json
{
  "todayRevenue": 2500.00,
  "yesterdayRevenue": 2000.00,
  "percentageChange": 25.0
}
```

---

### 3. Get Total Orders
**GET** `/{restaurantId}/orders/total`

Returns today's total orders with percentage change from yesterday.

**Response:**
```json
{
  "todayOrders": 45,
  "yesterdayOrders": 40,
  "percentageChange": 12.5
}
```

---

### 4. Get Average Order Value
**GET** `/{restaurantId}/orders/avg-value`

Returns today's average order value with percentage change from yesterday.

**Response:**
```json
{
  "todayAvgOrderValue": 55.56,
  "yesterdayAvgOrderValue": 50.00,
  "percentageChange": 11.12
}
```

---

### 5. Get Average Rating
**GET** `/{restaurantId}/rating/avg`

Returns today's average rating with percentage change from yesterday.

**Response:**
```json
{
  "todayAvgRating": 92.5,
  "yesterdayAvgRating": 87.5,
  "percentageChange": 5.71,
  "todayRatingCount": 40,
  "yesterdayRatingCount": 32
}
```

**Note:** Rating is calculated as percentage of thumbs-up ratings.

---

## Revenue Analysis

### 6. Get Revenue Per Day
**GET** `/{restaurantId}/revenue/daily?startDate={startDate}&endDate={endDate}`

Returns daily revenue breakdown between two dates.

**Query Parameters:**
- `startDate` (required): Start date in ISO format (yyyy-MM-dd)
- `endDate` (required): End date in ISO format (yyyy-MM-dd)

**Response:**
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
  },
  {
    "date": "2025-11-03",
    "revenue": 2300.00,
    "orderCount": 41
  }
]
```

---

## Items & Menu

### 7. Get Top Selling Items
**GET** `/{restaurantId}/items/top-selling?limit={limit}`

Returns the most sold items for the current day.

**Query Parameters:**
- `limit` (default: 10, max: 50): Number of top items to return

**Response:**
```json
[
  {
    "itemId": 123,
    "itemName": "Margherita Pizza",
    "orderCount": 25,
    "price": 12.99,
    "imageUrl": "https://example.com/pizza.jpg"
  },
  {
    "itemId": 124,
    "itemName": "Pepperoni Pizza",
    "orderCount": 20,
    "price": 14.99,
    "imageUrl": "https://example.com/pepperoni.jpg"
  }
]
```

---

### 8. Get Menu Items
**GET** `/{restaurantId}/items?category={category}&page={page}&size={size}`

Returns paginated menu items with availability and category filter.

**Query Parameters:**
- `category` (optional): Filter by menu category name
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Response:**
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
    },
    {
      "id": 124,
      "name": "Pepperoni Pizza",
      "description": "Pizza with pepperoni",
      "price": 14.99,
      "available": true,
      "categories": ["Pizza"],
      "imageUrl": "https://example.com/pepperoni.jpg",
      "hasPromotion": true,
      "promotionPrice": 12.99
    }
  ],
  "totalElements": 50,
  "totalPages": 3,
  "number": 0
}
```

---

## Orders History

### 9. Get Orders History
**GET** `/{restaurantId}/orders/history?date={date}&status={status}&page={page}&size={size}`

Returns orders history for a specific day with pagination and status filter.

**Query Parameters:**
- `date` (required): Date in ISO format (yyyy-MM-dd)
- `status` (optional): Filter by order status
- `page` (default: 0): Page number
- `size` (default: 20, max: 100): Page size

**Available Order Statuses:**
- PENDING
- ACCEPTED
- PREPARING
- READY_FOR_PICK_UP
- IN_DELIVERY
- DELIVERED
- REJECTED
- CANCELED

**Response:**
```json
{
  "content": [
    {
      "id": 1001,
      "client": {
        "id": 50,
        "name": "John Doe"
      },
      "restaurant": {
        "id": 1,
        "name": "Pizza Palace"
      },
      "status": "DELIVERED",
      "orderTime": "2025-11-04T12:30:00",
      "total": 35.50,
      "deliveryAddress": "456 Oak Street",
      "items": [
        {
          "menuItem": {
            "name": "Margherita Pizza"
          },
          "quantity": 2,
          "unitPrice": 12.99
        }
      ]
    }
  ],
  "totalElements": 45,
  "totalPages": 3,
  "number": 0
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
  "message": "Restaurant not found with id: 123"
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

1. **Open/Closed Status**: Calculated dynamically based on current time and restaurant operating hours for the current day
2. **Date Format**: All dates in query parameters should be in ISO-8601 format (yyyy-MM-dd)
3. **Pagination**: All paginated endpoints support standard Spring Data pagination parameters
4. **Authentication**: All endpoints require a valid JWT token with ROLE_ADMIN authority
5. **Rating Calculation**: Restaurant ratings are calculated as percentage of thumbs-up ratings (thumbsUpCount / totalCount * 100)
6. **Revenue**: All revenue values include delivered orders only
7. **Top Selling Items**: Calculated based on order count for the current day only
8. **Time Zone**: All date/time calculations use server's local time zone

## Example Usage

### Get restaurants in Italian cuisine
```
GET /api/admin/restaurants/management/restaurants?cuisine=ITALIAN&page=0&size=20
```

### Get today's revenue for restaurant
```
GET /api/admin/restaurants/management/1/revenue/today
```

### Get revenue for last 7 days
```
GET /api/admin/restaurants/management/1/revenue/daily?startDate=2025-10-28&endDate=2025-11-04
```

### Get top 10 selling items
```
GET /api/admin/restaurants/management/1/items/top-selling?limit=10
```

### Get delivered orders for today
```
GET /api/admin/restaurants/management/1/orders/history?date=2025-11-04&status=DELIVERED&page=0&size=20
```

### Get all pizza items
```
GET /api/admin/restaurants/management/1/items?category=Pizza&page=0&size=20
```
