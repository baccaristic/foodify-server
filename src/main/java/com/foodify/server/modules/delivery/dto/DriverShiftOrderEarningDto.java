package com.foodify.server.modules.delivery.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class DriverShiftOrderEarningDto {
    Long orderId;
    Long deliveryId;
    String pickUpLocation;
    String deliveryLocation;
    BigDecimal orderTotal;
    BigDecimal driverEarningFromOrder;
    BigDecimal deliveryFee;
    String restaurantName;
    Integer orderItemsCount;
}
