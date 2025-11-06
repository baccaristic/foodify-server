# Business Model Update Summary

This document summarizes the business model changes implemented for the Foodify platform.

## Changes Implemented

### 1. Daily Fee Reduction
- **Previous**: Drivers paid 20 DT per day
- **New**: Drivers now pay 5 DT per day
- **Implementation**: Updated `DriverFinancialService.DAILY_FEE` constant

### 2. Driver Earnings Model
The driver earnings formula is: **Delivery Fee + (Order Total excluding delivery fee) × 12% + Tips**

**Example Calculation:**
- Order items total: 100 DT
- Delivery fee: 10 DT
- Tips: 5 DT
- Driver earnings = 10 + (100 × 0.12) + 5 = 10 + 12 + 5 = **27 DT**

**Implementation:**
```java
BigDecimal commission = itemsTotal.multiply(COMMISSION_RATE).setScale(2, RoundingMode.HALF_UP);
BigDecimal driverEarnings = commission.add(deliveryFee).add(tipAmount);
```

### 3. Restaurant Commission Model
- **Requirement**: Restaurant commission rate (x) must be greater than 18%
- **Foodify's Share**: (x - 12)% of order + service fee
- **Default Rate**: 20% for new restaurants (previously 17%)
- **Database Constraint**: Added validation to ensure commission rate > 18%

**Example:**
- Restaurant commission rate: 20%
- Order total (excluding delivery): 100 DT
- Driver gets: 12 DT (12% of 100 DT)
- Foodify gets: 8 DT (20% - 12% = 8% of 100 DT) + service fee

### 4. Cash Deposit Warning System
- **Threshold**: 250 DT cash on hand
- **Warning Mechanism**: When driver's cash reaches ≥250 DT:
  - Push notification sent to driver's mobile device
  - WebSocket message sent to driver's active session
  - Timestamp recorded in database (`deposit_warning_sent_at`)
  
**Warning Message:**
> "You have reached 250dt cash on hand. Please deposit the cash within 24 hours to continue working."

### 5. 24-Hour Deposit Deadline
- **Deadline**: Drivers have 24 hours from the warning to deposit cash
- **Enforcement**: After 24 hours:
  - Driver cannot accept new orders
  - Driver cannot start new shifts
  - Driver is marked as unavailable automatically
  
**Implementation:**
- `DriverFinancialService.hasDepositDeadlinePassed()` checks if deadline exceeded
- `DriverFinancialService.assertCanWork()` blocks work if deadline passed
- `DriverAvailabilityService.refreshAvailability()` updates driver availability status

### 6. Reset After Deposit
When a driver successfully deposits cash:
- Cash on hand reset to 0
- Warning timestamp cleared (`deposit_warning_sent_at = null`)
- Driver can resume work immediately
- Availability status refreshed

## Database Changes

### Migration V15: Add Deposit Warning Timestamp
```sql
ALTER TABLE driver ADD COLUMN IF NOT EXISTS deposit_warning_sent_at TIMESTAMP;
```

### Migration V16: Update Restaurant Commission Rate
```sql
-- Update default to 20% (must be > 18%)
ALTER TABLE restaurant ALTER COLUMN restaurant_share_rate SET DEFAULT 0.2000;

-- Add constraint to ensure minimum rate
ALTER TABLE restaurant ADD CONSTRAINT check_commission_rate_minimum 
    CHECK (restaurant_share_rate IS NULL OR restaurant_share_rate > 0.18);
```

## Testing
Comprehensive unit tests added in `DriverFinancialServiceTest`:
- Daily fee verification (5 DT)
- Deposit threshold verification (250 DT)
- Deposit deadline verification (24 hours)
- Driver earnings calculation
- Cash on hand tracking for cash orders
- Deposit requirement logic
- Deadline passage checking
- Warning notification triggering

## API Impact
No breaking API changes. Existing endpoints continue to work as before, but with updated business logic:
- Driver financial summary endpoint returns updated values
- Deposit endpoints clear warning timestamps
- Driver availability checks enforce new deadline rules

## Event System
New event: `DriverDepositWarningEvent`
- Published when driver reaches deposit threshold
- Handled by `DriverDepositWarningListener` to send notifications

Existing event: `DriverDepositConfirmedEvent`
- Enhanced to clear warning timestamp and refresh availability
