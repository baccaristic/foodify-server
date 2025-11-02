package com.foodify.server.modules.orders.fee.repository;

import com.foodify.server.modules.orders.fee.domain.ServiceFeeSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceFeeRepository extends JpaRepository<ServiceFeeSetting, Long> {
}
