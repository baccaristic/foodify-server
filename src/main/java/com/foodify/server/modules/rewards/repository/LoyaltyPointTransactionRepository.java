package com.foodify.server.modules.rewards.repository;

import com.foodify.server.modules.rewards.domain.LoyaltyPointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyPointTransactionRepository extends JpaRepository<LoyaltyPointTransaction, Long> {
    List<LoyaltyPointTransaction> findByClient_IdOrderByCreatedAtDesc(Long clientId);
}
