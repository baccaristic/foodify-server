package com.foodify.server.modules.notifications.domain;

import com.foodify.server.modules.identity.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "notification_preferences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "notification_type"}))
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 64)
    private NotificationType notificationType;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
