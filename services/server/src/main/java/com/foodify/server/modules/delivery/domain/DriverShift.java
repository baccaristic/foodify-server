package com.foodify.server.modules.delivery.domain;

import com.foodify.server.modules.identity.domain.Driver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver_shifts")
@Getter
@Setter
public class DriverShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private DriverShiftStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime finishableAt;

    private LocalDateTime endedAt;

    @OneToOne(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DriverShiftBalance balance;

    @OneToMany(mappedBy = "shift")
    private List<Delivery> deliveries = new ArrayList<>();
}
