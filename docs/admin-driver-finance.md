# Admin Driver Finance API

This document describes the REST endpoints that administrators use to review driver cash deposits and release payouts. Deposits
are made in person at the partner bureau; the back office only validates the paperwork and releases the driver’s earnings.

## Overview
- Drivers must deposit cash when their on-hand balance reaches **250 DT**. While the balance stays above the threshold they are automatically marked unavailable for dispatch.
- Every new shift applies a **20 DT** daily fee that is deducted from the driver’s next payout. Fees accrue per day and are
  always deducted in **20 DT** increments. If a driver has five unpaid days the next payout deducts **100 DT** (5 × 20) as soon
  as their earnings cover that amount.
- A deposit records two amounts:
  - `depositAmount`: cash returned by the driver.
  - `earningsPaid`: the 12% commission plus delivery fees owed to the driver after deducting outstanding daily fees.

## Endpoints

### 1. Fetch driver finance snapshot
- **Endpoint**: `GET /api/admin/drivers/{driverId}/finance`
- **Auth**: `ROLE_ADMIN`.
- **Response**: Same payload as `/api/driver/finance/summary` so the back office can display cash-on-hand, unpaid earnings, outstanding fees, and whether a deposit is required.

### 2. List deposits
- **Endpoint**: `GET /api/admin/drivers/deposits`
- **Query parameters**:
  - `status` *(optional)* – filter by `PENDING` or `CONFIRMED`.
- **Response**: Array of deposit records ordered newest first. Each record includes driver identifiers (id, name, phone), deposit amount, earnings paid, fees deducted, status, and timestamps.
- **Usage**: Poll with `status=PENDING` to power an approval queue in the admin UI.

### 3. Preview deposit payout
- **Endpoint**: `GET /api/admin/drivers/{driverId}/finance/deposits/confirm`
- **Auth**: `ROLE_ADMIN`.
- **Response**: Returns the calculated deposit amounts (cash on hand, unpaid earnings, fees to deduct, and payout to release)
  so finance staff can review the figures before finalizing the deposit.
- **Behavior**: Applies any outstanding daily fees to ensure the snapshot matches what the confirmation endpoint will persist.
- **Errors**:
  - `404 Not Found` if the driver does not exist.
  - `409 Conflict` when no cash is available to deposit or an unconfirmed deposit already exists for the driver.

### 4. Confirm a deposit
- **Endpoint**: `POST /api/admin/drivers/deposits/{depositId}/confirm`
- **Auth**: `ROLE_ADMIN`.
- **Behavior**: Marks the deposit as `CONFIRMED`, stores the admin id and confirmation timestamp, and returns the updated deposit payload. Attempting to confirm a non-pending deposit returns `409 Conflict`.
- **Side effects**:
  - Clears the driver’s unpaid earnings captured in the deposit record and deducts the recorded daily fees.
  - Keeps the cash-on-hand balance reduced so dispatch can immediately reuse the driver after the confirmation.

### 5. Record a daily-fee payment
- **Endpoint**: `POST /api/admin/drivers/{driverId}/finance/daily-fees/pay`
- **Auth**: `ROLE_ADMIN`.
- **Request body**:
  ```json
  {
    "daysPaid": 1
  }
  ```
- **Behavior**: Reduces the driver’s outstanding daily-fee balance by the provided number of days. The amount removed is `daysPaid × 20 DT`.
- **Response**: Updated finance snapshot identical to the summary endpoint so the UI can refresh balances immediately.

## Implementation Tips for the Admin UI
- Present deposits in three columns: cash returned, payout released, and fees deducted so finance teams can reconcile totals quickly.
- Include the `depositRequired` flag from the summary endpoint to highlight drivers that are currently blocked.
- When the confirmation call succeeds, refetch both the summary and the pending queue to keep totals in sync.
