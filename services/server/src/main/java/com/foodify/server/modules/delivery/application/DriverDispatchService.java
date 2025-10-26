package com.foodify.server.modules.delivery.application;

import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.notifications.application.NotificationPreferenceService;
import com.foodify.server.modules.notifications.application.PushNotificationService;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class DriverDispatchService {

    private static final Duration PENDING_DRIVER_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DRIVER_REATTEMPT_COOLDOWN = Duration.ofMinutes(2);
    private static final Duration[] RETRY_DELAYS = {
            Duration.ofSeconds(5),
            Duration.ofSeconds(15),
            Duration.ofSeconds(30),
            Duration.ofMinutes(1),
            Duration.ofMinutes(2)
    };
    private static final Duration MAX_RETRY_DELAY = Duration.ofMinutes(5);
    private static final String ORDER_ATTEMPTED_DRIVERS_KEY_PREFIX = "order:drivers:attempted:";
    private static final Set<OrderStatus> PENDING_ORDER_STATUSES =
            Collections.unmodifiableSet(EnumSet.of(OrderStatus.ACCEPTED));

    private final DriverAssignmentService driverAssignmentService;
    private final DriverLocationService driverLocationService;
    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;
    private final PushNotificationService pushNotificationService;
    private final UserDeviceService userDeviceService;
    private final WebSocketService webSocketService;
    private final NotificationPreferenceService notificationPreferenceService;
    private final TransactionTemplate transactionTemplate;
    private final TaskScheduler taskScheduler;
    @Qualifier("notificationDispatchExecutor")
    private final TaskExecutor notificationExecutor;

    private final Map<Long, DispatchState> states = new ConcurrentHashMap<>();

    public DriverDispatchService(DriverAssignmentService driverAssignmentService,
                                 DriverLocationService driverLocationService,
                                 OrderRepository orderRepository,
                                 StringRedisTemplate redisTemplate,
                                 PushNotificationService pushNotificationService,
                                 UserDeviceService userDeviceService,
                                 WebSocketService webSocketService,
                                 NotificationPreferenceService notificationPreferenceService,
                                 PlatformTransactionManager transactionManager,
                                 @Qualifier("driverDispatchTaskScheduler") TaskScheduler taskScheduler,
                                 @Qualifier("notificationDispatchExecutor") TaskExecutor notificationExecutor) {
        this.driverAssignmentService = driverAssignmentService;
        this.driverLocationService = driverLocationService;
        this.orderRepository = orderRepository;
        this.redisTemplate = redisTemplate;
        this.pushNotificationService = pushNotificationService;
        this.userDeviceService = userDeviceService;
        this.webSocketService = webSocketService;
        this.notificationPreferenceService = notificationPreferenceService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.taskScheduler = taskScheduler;
        this.notificationExecutor = notificationExecutor;
    }

    public void beginSearch(Order order) {
        if (order == null || order.getId() == null) {
            return;
        }
        DispatchState state = states.compute(order.getId(), (id, existing) -> {
            if (existing != null) {
                existing.reset();
                return existing;
            }
            return new DispatchState();
        });
        state.reset();
        scheduleImmediate(order.getId());
    }

    public void handleDriverDecline(Long orderId, Long driverId) {
        if (orderId == null || driverId == null) {
            return;
        }
        transactionTemplate.execute(status -> {
            orderRepository.findById(orderId).ifPresentOrElse(order -> {
                if (order.getDelivery() != null) {
                    log.debug("Ignoring decline for order {} because a driver is already assigned", orderId);
                    clearState(orderId);
                    return;
                }
                if (order.getPendingDriver() == null || !Objects.equals(order.getPendingDriver().getId(), driverId)) {
                    log.debug("Driver {} is not pending for order {}, skipping decline handling", driverId, orderId);
                    return;
                }

                order.setPendingDriver(null);
                orderRepository.save(order);
                driverLocationService.markAvailable(String.valueOf(driverId));
                rememberAttemptedDriver(orderId, driverId);
                DispatchState state = states.computeIfAbsent(orderId, id -> new DispatchState());
                state.clearPendingTimeout();
                log.info("Driver {} declined order {}. Searching for a new driver", driverId, orderId);
                scheduleRetry(orderId, state);
            }, () -> clearState(orderId));
            return null;
        });
    }

    public void triggerSearchForPendingOrders() {
        List<Long> pendingOrderIds = orderRepository.findIdsNeedingDriver(PENDING_ORDER_STATUSES);
        if (pendingOrderIds.isEmpty()) {
            return;
        }

        for (Long orderId : pendingOrderIds) {
            scheduleImmediate(orderId);
        }
    }

    public void markDriverAccepted(Long orderId) {
        if (orderId == null) {
            return;
        }
        states.computeIfPresent(orderId, (id, state) -> {
            state.clearPendingTimeout();
            return null;
        });
    }

    private void scheduleImmediate(Long orderId) {
        transactionTemplate.execute(status -> {
            attemptDriverAssignment(orderId);
            return null;
        });
    }

    private void attemptDriverAssignment(Long orderId) {
        orderRepository.findDetailedById(orderId).ifPresentOrElse(order -> {
            if (order.getDelivery() != null) {
                log.debug("Order {} already has a driver assigned. Stopping search", orderId);
                clearState(orderId);
                return;
            }

            OrderStatus status = order.getStatus();
            if (status == OrderStatus.CANCELED || status == OrderStatus.REJECTED) {
                log.debug("Order {} is {}. Stopping driver search", orderId, status);
                clearState(orderId);
                return;
            }

            if (order.getPendingDriver() != null) {
                log.debug("Order {} still awaiting response from driver {}", orderId, order.getPendingDriver().getId());
                return;
            }

            Set<Long> excludedDriverIds = loadAttemptedDrivers(orderId);
            Optional<DriverAssignmentService.DriverMatch> match = driverAssignmentService
                    .findBestDriver(order, excludedDriverIds);

            match.ifPresentOrElse(driverMatch -> {
                Long driverId = driverMatch.driver().getId();
                rememberAttemptedDriver(orderId, driverId);
                driverLocationService.markPending(String.valueOf(driverId), orderId);
                order.setPendingDriver(driverMatch.driver());
                Order persisted = orderRepository.save(order);
                notifyDriverAboutOrder(persisted, driverId);
                schedulePendingTimeout(orderId, driverId);
            }, () -> {
                log.info("No eligible drivers found for order {} (excluded: {}). Will retry", orderId, excludedDriverIds.size());
                DispatchState state = states.computeIfAbsent(orderId, id -> new DispatchState());
                scheduleRetry(orderId, state);
            });
        }, () -> clearState(orderId));
    }

    private void scheduleRetry(Long orderId, DispatchState state) {
        int attempt = state.incrementAttempt();
        Duration delay = computeDelay(attempt);
        taskScheduler.schedule(() -> transactionTemplate.execute(status -> {
                    attemptDriverAssignment(orderId);
                    return null;
                }),
                Instant.now().plus(delay));
        log.debug("Scheduled driver search retry {} for order {} in {} seconds", attempt, orderId, delay.toSeconds());
    }

    private void schedulePendingTimeout(Long orderId, Long driverId) {
        DispatchState state = states.computeIfAbsent(orderId, id -> new DispatchState());
        state.clearPendingTimeout();
        ScheduledFuture<?> future = taskScheduler.schedule(() -> transactionTemplate.execute(status -> {
                    handlePendingDriverTimeout(orderId, driverId);
                    return null;
                }),
                Instant.now().plus(PENDING_DRIVER_TIMEOUT));
        state.setPendingTimeout(future);
    }

    private void handlePendingDriverTimeout(Long orderId, Long driverId) {
        orderRepository.findById(orderId).ifPresentOrElse(order -> {
            if (order.getDelivery() != null) {
                clearState(orderId);
                return;
            }
            if (order.getPendingDriver() == null || !Objects.equals(order.getPendingDriver().getId(), driverId)) {
                log.debug("Pending driver {} already cleared for order {}", driverId, orderId);
                return;
            }

            log.info("Pending driver {} did not respond for order {} in time. Releasing and retrying", driverId, orderId);
            order.setPendingDriver(null);
            orderRepository.save(order);
            driverLocationService.markAvailable(String.valueOf(driverId));
            rememberAttemptedDriver(orderId, driverId);
            DispatchState state = states.computeIfAbsent(orderId, id -> new DispatchState());
            scheduleRetry(orderId, state);
        }, () -> clearState(orderId));
    }

    private Duration computeDelay(int attempt) {
        if (attempt <= 0) {
            return Duration.ZERO;
        }
        if (attempt <= RETRY_DELAYS.length) {
            return RETRY_DELAYS[attempt - 1];
        }
        return MAX_RETRY_DELAY;
    }

    private Set<Long> loadAttemptedDrivers(Long orderId) {
        String key = ORDER_ATTEMPTED_DRIVERS_KEY_PREFIX + orderId;
        long now = Instant.now().toEpochMilli();
        long freshnessThreshold = now - DRIVER_REATTEMPT_COOLDOWN.toMillis();
        redisTemplate.opsForZSet().removeRangeByScore(key, Double.NEGATIVE_INFINITY, freshnessThreshold - 1);
        Set<String> rawValues = redisTemplate.opsForZSet().rangeByScore(key, freshnessThreshold, Double.POSITIVE_INFINITY);
        if (rawValues == null || rawValues.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Long> result = new HashSet<>(rawValues.size());
        for (String value : rawValues) {
            if (value == null) {
                continue;
            }
            try {
                result.add(Long.valueOf(value));
            } catch (NumberFormatException ignored) {
                log.debug("Ignoring unparsable driver id {} for order {}", value, orderId);
            }
        }
        return result;
    }

    private void rememberAttemptedDriver(Long orderId, Long driverId) {
        String key = ORDER_ATTEMPTED_DRIVERS_KEY_PREFIX + orderId;
        redisTemplate.opsForZSet().add(key, String.valueOf(driverId), Instant.now().toEpochMilli());
        redisTemplate.expire(key, Duration.ofHours(6));
    }

    private void notifyDriverAboutOrder(Order order, Long driverId) {
        webSocketService.notifyDriverUpcoming(driverId, order);
        if (!notificationPreferenceService.isEnabled(driverId, NotificationType.ORDER_UPDATES)) {
            return;
        }
        Long orderId = order.getId();
        for (UserDevice userDevice : userDeviceService.findByUser(driverId)) {
            notificationExecutor.execute(() -> {
                try {
                    pushNotificationService.sendOrderNotification(
                            userDevice.getDeviceToken(),
                            orderId,
                            "You have a new delivery request",
                            "You have recieved a new delivery request. You have 2 minutes to accept or decline.",
                            NotificationType.ORDER_UPDATES
                    );
                } catch (Exception ex) {
                    log.warn("Failed to send push notification to driver {} for order {}", driverId, orderId, ex);
                }
            });
        }
    }

    private void clearState(Long orderId) {
        states.computeIfPresent(orderId, (id, state) -> {
            state.clearPendingTimeout();
            return null;
        });
    }

    private static class DispatchState {
        private int attempt;
        private ScheduledFuture<?> pendingTimeout;

        void reset() {
            attempt = 0;
            clearPendingTimeout();
        }

        int incrementAttempt() {
            if (attempt < Integer.MAX_VALUE) {
                attempt++;
            }
            return attempt;
        }

        void setPendingTimeout(ScheduledFuture<?> future) {
            this.pendingTimeout = future;
        }

        void clearPendingTimeout() {
            if (pendingTimeout != null) {
                pendingTimeout.cancel(false);
                pendingTimeout = null;
            }
        }
    }
}

