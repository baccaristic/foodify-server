# Timezone-Aware Restaurant Operating Status

## Overview

The restaurant operating status (`isOpen` field) is determined based on the restaurant's weekly schedule and special days. Since restaurant schedules are stored in their local timezone, clients must provide their current date and time to get accurate operating status.

## Problem Statement

Previously, the server used `LocalDate.now()` and `LocalTime.now()` to determine if a restaurant is open. This caused incorrect "isOpen" status for clients in different timezones than the server.

## Solution

Clients can now optionally provide their current date and time as query parameters in restaurant fetching APIs. The server will use these values to determine the restaurant's operating status based on the client's local time (which should match the restaurant's timezone).

## API Changes

### Updated Endpoints

The following endpoints now accept optional `clientDate` and `clientTime` query parameters:

#### 1. Restaurant Search API
**Endpoint:** `GET /api/client/restaurants/search`

**New Parameters:**
- `clientDate` (optional): Client's current date in ISO 8601 format (e.g., `2024-11-08`)
- `clientTime` (optional): Client's current time in ISO 8601 format (e.g., `14:30:00`)

**Example:**
```
GET /api/client/restaurants/search?lat=36.8065&lng=10.1815&clientDate=2024-11-08&clientTime=14:30:00
```

#### 2. Restaurant Details API
**Endpoint:** `GET /api/client/restaurant/{id}`

**New Parameters:**
- `clientDate` (optional): Client's current date in ISO 8601 format
- `clientTime` (optional): Client's current time in ISO 8601 format

**Example:**
```
GET /api/client/restaurant/123?lat=36.8065&lng=10.1815&clientDate=2024-11-08&clientTime=14:30:00
```

#### 3. Nearby Restaurants APIs
All nearby restaurant endpoints now support these parameters:
- `GET /api/client/nearby/top`
- `GET /api/client/nearby/favorites`
- `GET /api/client/nearby/orders`
- `GET /api/client/nearby/restaurants`
- `GET /api/client/nearby/promotions`
- `GET /api/client/filter/categorie`

**Example:**
```
GET /api/client/nearby/top?lat=36.8065&lng=10.1815&radiusKm=5&clientDate=2024-11-08&clientTime=14:30:00
```

## Client Implementation Guide

### Recommended Approach

1. **Get client's current date/time in restaurant's timezone:**
   - The client application should determine the user's location
   - Convert the user's current date/time to the restaurant's timezone
   - Send both `clientDate` and `clientTime` parameters in the API request

2. **Example Implementation (JavaScript):**
```javascript
// Get current date and time
const now = new Date();

// Format date as YYYY-MM-DD
const clientDate = now.toISOString().split('T')[0];

// Format time as HH:mm:ss
const clientTime = now.toTimeString().split(' ')[0];

// Make API request
const response = await fetch(
  `/api/client/restaurants/search?lat=${lat}&lng=${lng}&clientDate=${clientDate}&clientTime=${clientTime}`
);
```

### Fallback Behavior

If `clientDate` and `clientTime` are not provided, the server will fall back to using server time (`LocalDate.now()` and `LocalTime.now()`). However, this may result in incorrect operating status for clients in different timezones.

## Technical Details

### Date/Time Format
- **Date Format:** ISO 8601 date format (`YYYY-MM-DD`)
- **Time Format:** ISO 8601 time format (`HH:mm:ss`)

### Timezone Considerations
- Restaurant schedules are stored in the restaurant's local timezone
- The client should provide date/time in the **same timezone** as the restaurant
- For international applications, use geolocation to determine the appropriate timezone

### Example Scenarios

**Scenario 1: Client and Restaurant in Same Timezone**
- Client in Tunisia (UTC+1) viewing restaurants in Tunisia
- Client time: 14:30 on 2024-11-08
- Parameters: `clientDate=2024-11-08&clientTime=14:30:00`
- Result: Accurate operating status

**Scenario 2: Server in Different Timezone**
- Server in US (UTC-5), Client in Tunisia (UTC+1)
- Without parameters: Server uses US time → **Incorrect status**
- With parameters: Server uses client time → **Correct status**

## Migration Notes

- All existing API calls will continue to work (backward compatible)
- The new parameters are optional
- Clients should be updated to send these parameters for accurate results
- No database schema changes required
