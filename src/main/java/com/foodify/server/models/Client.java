package com.foodify.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Client extends User {
    private String address;
    private String phoneNumber;
    private Boolean phoneVerified;
    private Boolean emailVerified;
    private String googleId;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private List<Order> orders;
}