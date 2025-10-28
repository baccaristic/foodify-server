package com.foodify.server.modules.payments.konnect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectInitiatePaymentResponse(
        String payUrl,
        String paymentRef
) {
}
