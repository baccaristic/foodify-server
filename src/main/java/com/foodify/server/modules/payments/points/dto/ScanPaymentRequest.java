package com.foodify.server.modules.payments.points.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanPaymentRequest {
    
    @NotBlank(message = "Payment token is required")
    private String paymentToken;
}
