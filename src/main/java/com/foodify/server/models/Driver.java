package com.foodify.server.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Driver extends User {
    private boolean available;

    @OneToMany(mappedBy = "driver")
    private List<Delivery> deliveries;
}