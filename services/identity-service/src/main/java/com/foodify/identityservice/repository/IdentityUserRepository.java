package com.foodify.identityservice.repository;

import com.foodify.identityservice.domain.IdentityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentityUserRepository extends JpaRepository<IdentityUser, Long> {
    Optional<IdentityUser> findByEmail(String email);
    Optional<IdentityUser> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
