# Restaurant Analytics API

This document describes the analytics APIs available to restaurant administrators and cashiers.

## Overview

The analytics APIs provide comprehensive insights into restaurant performance across three main areas:
1. **General Overview** - Key metrics with period-over-period comparisons
2. **Sales Trend** - Time-series data for charting
3. **Top Dishes** - Popular menu items analytics

All endpoints support three time periods:
- `TODAY` - Current day vs. previous day (default)
- `WEEK` - Current week (Monday-Sunday) vs. previous week
- `MONTH` - Current month vs. previous month

## Authentication

All analytics endpoints require authentication with one of the following roles:
- `ROLE_RESTAURANT_ADMIN`
- `ROLE_RESTAURANT_CASHIER`

The APIs automatically associate the authenticated user with their restaurant.

## Endpoints

### 1. General Overview

**Endpoint:** `GET /api/restaurant/analytics/overview`

**Query Parameters:**
- `period` (optional): `TODAY` | `WEEK` | `MONTH` (default: `TODAY`)

**Response:**
```json
{
  "period": "TODAY",
  "revenue": {
    "current": 1500.50,
    "previous": 1200.00,
    "change": 300.50,
    "percentageChange": 25.04
  },
  "orders": {
    "current": 45,
    "previous": 38,
    "change": 7,
    "percentageChange": 18.42
  },
  "preparationTime": {
    "currentMinutes": 22.50,
    "previousMinutes": 25.00,
    "changeMinutes": -2.50,
    "percentageChange": -10.00
  },
  "rating": {
    "currentStars": 4.2,
    "previousStars": 3.8,
    "changeStars": 0.4,
    "percentageChange": 10.53
  }
}
```

**Metrics:**
- **Revenue**: Total revenue from delivered orders in the period
  - Includes all order components (items, extras, delivery fee, service fee)
  - Only counts orders with status `DELIVERED`

- **Orders**: Count of completed/delivered orders
  - Only counts orders with status `DELIVERED`

- **Preparation Time**: Average time from order acceptance to ready-for-pickup
  - Calculated from `delivery.timeToPickUp` field
  - Measured in minutes
  - Only includes orders that were delivered

- **Rating**: Customer satisfaction on a 5-star scale
  - Based on thumbs up/down ratings converted to 5-star scale
  - Formula: `(thumbsUpCount / totalRatings) * 5`
  - Only includes ratings created during the period

**Example Usage:**
```bash
# Get today's overview
curl -H "Authorization: Bearer <token>" \
  https://api.foodify.com/api/restaurant/analytics/overview

# Get this week's overview
curl -H "Authorization: Bearer <token>" \
  https://api.foodify.com/api/restaurant/analytics/overview?period=WEEK

# Get this month's overview
curl -H "Authorization: Bearer <token>" \
  https://api.foodify.com/api/restaurant/analytics/overview?period=MONTH
```

### 2. Sales Trend

**Endpoint:** `GET /api/restaurant/analytics/sales-trend`

**Query Parameters:**
- `period` (optional): `TODAY` | `WEEK` | `MONTH` (default: `TODAY`)

**Response:**
```json
{
  "period": "WEEK",
  "data": [
    {
      "date": "2024-01-15",
      "revenue": 1500.50,
      "orderCount": 45
    },
    {
      "date": "2024-01-16",
      "revenue": 1750.25,
      "orderCount": 52
    },
    {
      "date": "2024-01-17",
      "revenue": 1200.00,
      "orderCount": 35
    }
  ]
}
```

**Data Points:**
- One data point per day in the period
- Missing dates are filled with zero values
- Only includes delivered orders
- Revenue includes all order components

**Example Usage:**
```bash
# Get today's hourly sales (returns single data point for today)
curl -H "Authorization: Bearer <token>" \
  https://api.foodify.com/api/restaurant/analytics/sales-trend

# Get this week's daily sales
curl -H "Authorization: Bearer <token>" \
  https://api.foodify.com/api/restaurant/analytics/sales-trend?period=WEEK
```

### 3. Top Dishes

**Endpoint:** `GET /api/restaurant/analytics/top-dishes`

**Query Parameters:**
- `period` (optional): `TODAY` | `WEEK` | `MONTH` (default: `TODAY`)

**Response:**
```json
{
  "period": "WEEK",
  "topDishes": [
    {
      "menuItemId": 123,
      "menuItemName": "Margherita Pizza",
      "orderCount": 45,
      "quantitySold": 67
    },
    {
      "menuItemId": 456,
      "menuItemName": "Caesar Salad",
      "orderCount": 38,
      "quantitySold": 52
    },
    {
      "menuItemId": 789,
      "menuItemName": "Pasta Carbonara",
      "orderCount": 32,
      "quantitySold": 45
    }
  ]
}
```

**Response Details:**
- Returns top 3 menu items by quantity sold
- `orderCount`: Number of distinct orders containing this item
- `quantitySold`: Total quantity of this item across all orders
- Only includes delivered orders
- Sorted by quantity sold (descending)

**Example Usage:**
```bash
# Get today's top dishes
curl -H "Authorization: Bearer <token>" \
  https://api.foodify.com/api/restaurant/analytics/top-dishes

# Get this month's top dishes
curl -H "Authorization: Bearer <token>" \
  https://api.foodify.com/api/restaurant/analytics/top-dishes?period=MONTH
```

## Period Calculations

### TODAY
- **Current**: Start of today (00:00:00) to end of today (23:59:59)
- **Previous**: Start of yesterday (00:00:00) to end of yesterday (23:59:59)

### WEEK
- **Current**: Start of current week (Monday 00:00:00) to end of current week (Sunday 23:59:59)
- **Previous**: Start of previous week (Monday 00:00:00) to end of previous week (Sunday 23:59:59)

### MONTH
- **Current**: First day of current month (00:00:00) to last day of current month (23:59:59)
- **Previous**: First day of previous month (00:00:00) to last day of previous month (23:59:59)

## Error Handling

All endpoints return standard HTTP status codes:
- `200 OK`: Success
- `401 Unauthorized`: Missing or invalid authentication token
- `403 Forbidden`: User does not have required role
- `500 Internal Server Error`: Server error

## Performance Considerations

- All queries are optimized with proper indexing on date fields
- Aggregations are performed at the database level
- Results are computed on-demand (no caching)
- For high-traffic restaurants, consider implementing caching at the application or API gateway level

## Use Cases

### Dashboard Display
Use the general overview endpoint to populate a dashboard showing:
- Revenue trends with up/down indicators
- Order volume changes
- Preparation time improvements
- Customer satisfaction scores

### Chart Visualizations
Use the sales trend endpoint to create:
- Line charts showing revenue over time
- Bar charts comparing daily performance
- Combined charts showing revenue and order count correlation

### Menu Optimization
Use the top dishes endpoint to:
- Identify popular items for promotions
- Understand which items drive revenue
- Make inventory planning decisions
- Create targeted marketing campaigns

## Implementation Notes

1. **Timezone**: All timestamps are in server timezone. Consider adding timezone support for international deployments.

2. **Real-time Updates**: Analytics data is computed on-demand from the database. For real-time updates, poll the endpoints periodically or implement WebSocket notifications.

3. **Data Privacy**: Analytics data is automatically filtered by restaurant. Each user can only see their own restaurant's data.

4. **Null Handling**: When no data exists for a period, the API returns zero values rather than null to simplify client-side handling.

5. **Rating System**: The rating metric uses a thumbs up/down system converted to a 5-star scale. This is calculated as `(thumbsUpCount / totalRatings) * 5` rounded to one decimal place.
