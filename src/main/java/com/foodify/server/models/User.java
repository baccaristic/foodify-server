package com.foodify.server.models;

import com.foodify.server.enums.AuthProvider;
import com.foodify.server.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider; // GOOGLE, LOCAL

    private String name;
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    private Role role;
}
