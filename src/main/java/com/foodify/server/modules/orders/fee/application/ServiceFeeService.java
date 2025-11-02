package com.foodify.server.modules.orders.fee.application;

import com.foodify.server.modules.orders.fee.domain.ServiceFeeSetting;
import com.foodify.server.modules.orders.fee.repository.ServiceFeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServiceFeeService {

    private static final BigDecimal DEFAULT_FEE = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final ServiceFeeRepository repository;

    @Transactional(value = Transactional.TxType.SUPPORTS)
    public BigDecimal getCurrentFeeAmount() {
        return repository.findById(ServiceFeeSetting.SINGLETON_ID)
                .map(ServiceFeeSetting::getAmount)
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(DEFAULT_FEE);
    }

    @Transactional(value = Transactional.TxType.SUPPORTS)
    public ServiceFeeSetting getCurrentSetting() {
        return repository.findById(ServiceFeeSetting.SINGLETON_ID)
                .map(this::normalizeAmounts)
                .orElseGet(() -> {
                    ServiceFeeSetting setting = new ServiceFeeSetting();
                    setting.setId(ServiceFeeSetting.SINGLETON_ID);
                    setting.setAmount(DEFAULT_FEE);
                    return setting;
                });
    }

    @Transactional
    public ServiceFeeSetting updateServiceFee(BigDecimal amount, String updatedBy) {
        if (amount == null) {
            throw new IllegalArgumentException("Service fee amount is required");
        }

        BigDecimal normalizedAmount = amount.setScale(2, RoundingMode.HALF_UP);

        ServiceFeeSetting setting = repository.findById(ServiceFeeSetting.SINGLETON_ID)
                .orElseGet(() -> {
                    ServiceFeeSetting newSetting = new ServiceFeeSetting();
                    newSetting.setId(ServiceFeeSetting.SINGLETON_ID);
                    return newSetting;
                });

        setting.setAmount(normalizedAmount);
        setting.setUpdatedAt(LocalDateTime.now());
        setting.setUpdatedBy(updatedBy);

        ServiceFeeSetting saved = repository.save(setting);
        return normalizeAmounts(saved);
    }

    private ServiceFeeSetting normalizeAmounts(ServiceFeeSetting setting) {
        if (setting.getAmount() != null) {
            setting.setAmount(setting.getAmount().setScale(2, RoundingMode.HALF_UP));
        } else {
            setting.setAmount(DEFAULT_FEE);
        }
        return setting;
    }
}
