package com.foodify.server.modules.notifications.repository;

import com.foodify.server.modules.identity.domain.User;
import com.foodify.server.modules.notifications.domain.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findByDeviceToken(String deviceToken);
    List<UserDevice> findByUserId(Long userId);
    void deleteByDeviceToken(String deviceToken);
    void deleteByUserId(Long userId);
}
