package com.foodify.server.modules.payments.konnect.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record KonnectInitiatePaymentRequest(
        String receiverWalletId,
        String token,
        Long amount,
        String type,
        String description,
        List<String> acceptedPaymentMethods,
        Integer lifespan,
        Boolean checkoutForm,
        Boolean addPaymentFeesToAmount,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String orderId,
        String webhook,
        String theme
) {
}
