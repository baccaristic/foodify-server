package com.foodify.addressesservice.addresses.repository;

import com.foodify.addressesservice.addresses.domain.SavedAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavedAddressRepository extends JpaRepository<SavedAddress, UUID> {
    List<SavedAddress> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<SavedAddress> findByIdAndUserId(UUID id, Long userId);
}
