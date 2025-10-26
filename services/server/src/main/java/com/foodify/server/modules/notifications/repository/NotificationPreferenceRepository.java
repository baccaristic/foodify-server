package com.foodify.server.modules.notifications.repository;

import com.foodify.server.modules.notifications.domain.NotificationPreference;
import com.foodify.server.modules.notifications.domain.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    List<NotificationPreference> findByUserId(Long userId);
    Optional<NotificationPreference> findByUserIdAndNotificationType(Long userId, NotificationType notificationType);
}
