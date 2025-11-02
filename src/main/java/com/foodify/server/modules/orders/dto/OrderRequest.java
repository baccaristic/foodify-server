package com.foodify.server.modules.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotEmpty(message = "Please provide at least one item for the order")
    private List<@Valid OrderItemRequest> items;

    @NotNull(message = "Delivery location is required")
    @Valid
    private LocationDto location;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Restaurant id is required")
    private Long restaurantId;

    private Long userId;

    private UUID savedAddressId;

    private String couponCode;

    @DecimalMin(value = "0.00", message = "Tip percentage cannot be negative")
    @DecimalMax(value = "100.00", message = "Tip percentage cannot exceed 100%")
    private BigDecimal tipPercentage;

    @DecimalMin(value = "0.00", message = "Cash to collect cannot be negative")
    private BigDecimal cashToCollect;
}
