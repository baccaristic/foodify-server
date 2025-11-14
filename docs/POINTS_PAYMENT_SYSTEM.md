# Points Payment System

## Overview
This feature allows restaurants to accept payments from clients using Foodify loyalty points. Restaurants can generate QR codes for payment requests, and clients can scan these codes to pay with their points balance.

## Conversion Rate
**1 Foodify Point = 0.01 TND**

Which means: **1 TND = 100 Foodify Points**

Example: A 100 TND payment requires 10,000 Foodify Points

## API Endpoints

### Restaurant Endpoints

#### Create Payment
**POST** `/api/payments/points/restaurant/create?restaurantId={restaurantId}`

Creates a payment request and generates a QR code.

**Request Body:**
```json
{
  "amountTnd": 100.00
}
```

**Response:**
```json
{
  "id": 1,
  "restaurantId": 123,
  "restaurantName": "Pizza Paradise",
  "amountTnd": 100.00,
  "pointsAmount": 10000.00,
  "paymentToken": "uuid-token",
  "status": "PENDING",
  "qrCodeImage": "base64-encoded-qr-code",
  "createdAt": "2025-11-14T10:00:00",
  "expiresAt": "2025-11-14T10:30:00"
}
```

**Authentication Required:** RESTAURANT_ADMIN or RESTAURANT_CASHIER

#### Get Payment History
**GET** `/api/payments/points/restaurant/{restaurantId}/history`

Returns all payments for a restaurant.

**Authentication Required:** RESTAURANT_ADMIN or RESTAURANT_CASHIER

#### Cancel Payment
**DELETE** `/api/payments/points/restaurant/{restaurantId}/payments/{paymentId}`

Cancels a pending payment.

**Authentication Required:** RESTAURANT_ADMIN or RESTAURANT_CASHIER

### Client Endpoints

#### Scan and Pay
**POST** `/api/payments/points/client/scan`

Scans a QR code and processes the payment.

**Request Body:**
```json
{
  "paymentToken": "uuid-token-from-qr-code"
}
```

**Response:**
```json
{
  "id": 1,
  "restaurantId": 123,
  "restaurantName": "Pizza Paradise",
  "clientId": 456,
  "clientName": "John Doe",
  "amountTnd": 100.00,
  "pointsAmount": 10000.00,
  "paymentToken": "uuid-token",
  "status": "COMPLETED",
  "createdAt": "2025-11-14T10:00:00",
  "completedAt": "2025-11-14T10:05:00",
  "expiresAt": "2025-11-14T10:30:00"
}
```

**Authentication Required:** CLIENT

#### Get Payment History
**GET** `/api/payments/points/client/history`

Returns all payments made by a client.

**Authentication Required:** CLIENT

## Payment Flow

1. **Restaurant creates payment:**
   - Restaurant inputs amount in TND
   - System calculates points amount (amount * 100)
   - System generates unique payment token
   - System creates QR code containing the token
   - Payment expires after 30 minutes

2. **Client scans QR code:**
   - Client's app extracts payment token from QR code
   - Client sends payment token to API
   - System validates:
     - Payment exists and is pending
     - Payment hasn't expired
     - Client has sufficient points
   - System transfers points from client to restaurant
   - System records transaction in loyalty history
   - Payment is marked as completed

## Validations

- **Amount validation:** Amount must be greater than 0
- **Restaurant validation:** Restaurant must exist
- **Client validation:** Client must exist
- **Balance validation:** Client must have sufficient points
- **Status validation:** Payment must be in PENDING status
- **Expiration validation:** Payment must not be expired (30-minute window)
- **Authorization validation:** Only the restaurant that created a payment can cancel it

## Database Schema

### points_payments Table
- `id`: Primary key
- `restaurant_id`: Foreign key to restaurant
- `client_id`: Foreign key to client (null until payment is completed)
- `amount_tnd`: Amount in TND
- `points_amount`: Amount in Foodify points
- `payment_token`: Unique token for payment identification
- `status`: PENDING, COMPLETED, CANCELLED, or EXPIRED
- `created_at`: When payment was created
- `completed_at`: When payment was completed (nullable)
- `expires_at`: When payment expires

### restaurant Table Addition
- `points_balance`: Restaurant's accumulated Foodify points

## Error Handling

Common error responses:

- **404 Not Found:** Restaurant, client, or payment not found
- **400 Bad Request:**
  - Invalid amount (zero or negative)
  - Insufficient points
  - Payment already processed
  - Payment expired
  - Payment not in pending status
- **403 Forbidden:** Not authorized to cancel payment
- **500 Internal Server Error:** Failed to generate QR code

## Security Considerations

- All endpoints require authentication
- Role-based access control (RBAC)
- Payment tokens are UUIDs (secure random)
- Payments automatically expire after 30 minutes
- Balance checks prevent overdrafts
- Transactions are atomic (all-or-nothing)
- Loyalty transaction records created for audit trail
