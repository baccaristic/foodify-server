package com.foodify.server.modules.payments.konnect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KonnectPaymentDetailsResponse(
        Payment payment
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Payment(
            String id,
            String status,
            Long amount,
            Long amountDue,
            Long reachedAmount,
            String token,
            String link,
            String webhook,
            String orderId,
            String type,
            String expirationDate
    ) {
    }
}
