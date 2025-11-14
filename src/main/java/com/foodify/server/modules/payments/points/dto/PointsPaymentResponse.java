package com.foodify.server.modules.payments.points.dto;

import com.foodify.server.modules.payments.points.domain.PointsPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsPaymentResponse {
    
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private Long clientId;
    private String clientName;
    private BigDecimal amountTnd;
    private BigDecimal pointsAmount;
    private String paymentToken;
    private PointsPaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime expiresAt;
    private String qrCodeImage; // Base64 encoded QR code image
}
