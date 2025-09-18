package com.foodify.server.repository;

import com.foodify.server.models.User;
import com.foodify.server.models.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findByDeviceToken(String deviceToken);
    List<UserDevice> findByUserId(Long userId);
    void deleteByDeviceToken(String deviceToken);
}
