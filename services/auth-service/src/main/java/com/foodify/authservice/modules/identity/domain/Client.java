package com.foodify.authservice.modules.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Client extends User {
    private String address;
    private String phoneNumber;
    private Boolean phoneVerified;
    private Boolean emailVerified;
    private String googleId;
    private LocalDate dateOfBirth;

    @Column(
            name = "loyalty_points_balance",
            precision = 19,
            scale = 2,
            nullable = false,
            columnDefinition = "numeric(19,2) not null default 0"
    )
    private BigDecimal loyaltyPointsBalance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

}
