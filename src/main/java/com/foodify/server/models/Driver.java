package com.foodify.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Driver extends User {
    private boolean available;
    private String phone;

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Delivery> deliveries;
}