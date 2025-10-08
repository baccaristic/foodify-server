package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.delivery.application.DriverAssignmentService;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.notifications.application.PushNotificationService;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderDto;
import com.foodify.server.modules.orders.mapper.OrderMapper;
import com.foodify.server.modules.orders.application.OrderLifecycleService;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.MenuOptionGroup;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.MenuItemRequestDTO;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final DriverLocationService driverLocationService;
    private final DriverAssignmentService driverAssignmentService;
    private final StringRedisTemplate redisTemplate;
    private final PushNotificationService pushNotificationService;
    private final UserDeviceService userDeviceService;
    private final WebSocketService webSocketService;
    private final OrderLifecycleService orderLifecycleService;

    private static final String ORDER_ATTEMPTED_DRIVERS_KEY_PREFIX = "order:drivers:attempted:";


    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders(Restaurant restaurant) {
        return this.orderRepository.findAllByRestaurantOrderByDateDesc(restaurant)
                .stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional
    public OrderDto acceptOrder(Long id, Long userId) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getRestaurant().getAdmin().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized");
            }

            var savedOrder = orderLifecycleService.transition(order, OrderStatus.ACCEPTED,
                    "restaurant:" + userId,
                    "Restaurant accepted order");
            assignDriver(savedOrder);
            return loadOrderDto(savedOrder.getId());
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void assignDriver(Order order) {
        Set<Long> excludedDriverIds = loadAttemptedDrivers(order.getId());

        driverAssignmentService.findBestDriver(order, excludedDriverIds).ifPresentOrElse(match -> {
            Long driverId = match.driver().getId();
            rememberAttemptedDriver(order.getId(), driverId);

            driverLocationService.markPending(String.valueOf(driverId), order.getId());
            order.setPendingDriver(match.driver());
            Order persistedOrder = orderRepository.save(order);

            notifyDriverAboutOrder(persistedOrder, driverId);
            scheduleDriverTimeout(persistedOrder.getId(), driverId);
        }, () -> {
            log.info("No eligible drivers found for order {} after evaluating {} previous attempts", order.getId(), excludedDriverIds.size());
        });
    }

    private void notifyDriverAboutOrder(Order order, Long driverId) {
        webSocketService.notifyDriverUpcoming(driverId, order);
        List<UserDevice> userDevices = userDeviceService.findByUser(driverId);
        userDevices.forEach(userDevice -> {
            try {
                pushNotificationService.sendOrderNotification(
                        userDevice.getDeviceToken(),
                        order.getId(),
                        "You have a new delivery request",
                        "You have recieved a new delivery request. You have 2 minutes to accept or decline.",
                        NotificationType.ORDER_DRIVER_NEW_ORDER
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void scheduleDriverTimeout(Long orderId, Long driverId) {
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            try {
                String status = redisTemplate.opsForValue().get("driver:status:" + driverId);
                if (!("PENDING:" + orderId).equals(status)) {
                    return;
                }

                orderRepository.findById(orderId).ifPresent(pendingOrder -> {
                    if (pendingOrder.getDelivery() != null) {
                        return;
                    }

                    driverLocationService.markAvailable(String.valueOf(driverId));
                    pendingOrder.setPendingDriver(null);
                    orderRepository.save(pendingOrder);
                    assignDriver(pendingOrder);
                });
            } finally {
                scheduler.shutdown();
            }
        }, 30, TimeUnit.SECONDS);
    }

    private Set<Long> loadAttemptedDrivers(Long orderId) {
        String key = ORDER_ATTEMPTED_DRIVERS_KEY_PREFIX + orderId;
        Set<String> rawValues = redisTemplate.opsForSet().members(key);
        if (rawValues == null || rawValues.isEmpty()) {
            return new HashSet<>();
        }
        return rawValues.stream()
                .map(value -> {
                    try {
                        return Long.valueOf(value);
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                })
                .filter(id -> id != null)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private void rememberAttemptedDriver(Long orderId, Long driverId) {
        String key = ORDER_ATTEMPTED_DRIVERS_KEY_PREFIX + orderId;
        redisTemplate.opsForSet().add(key, String.valueOf(driverId));
        redisTemplate.expire(key, Duration.ofHours(6));
    }


    @Transactional(readOnly = true)
    public OrderDto getOrderForRestaurant(Long orderId, Long restaurantId) {
        return orderRepository.findDetailedById(orderId)
                .filter(order -> order.getRestaurant() != null && order.getRestaurant().getId().equals(restaurantId))
                .map(OrderMapper::toDto)
                .orElse(null);
    }

    public MenuItem addMenu(MenuItemRequestDTO menuDto, List<MultipartFile> files) throws IOException {

        // 1. Handle image uploads
        List<String> imageFilenames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads").resolve(filename);
                Files.createDirectories(path.getParent());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                imageFilenames.add(filename);
            }
        }

        // 2. Create MenuItem
        MenuItem item = new MenuItem();
        item.setName(menuDto.getName());
        item.setDescription(menuDto.getDescription());
        item.setPrice(menuDto.getPrice());
        item.setCategory(menuDto.getCategory());
        item.setPopular(menuDto.isPopular());
        item.setImageUrls(imageFilenames);
        item.setPromotionLabel(menuDto.getPromotionLabel());
        item.setPromotionPrice(menuDto.getPromotionPrice());
        item.setPromotionActive(menuDto.isPromotionActive());

        Restaurant restaurant = restaurantRepository.findById(menuDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        item.setRestaurant(restaurant);

        // 3. Map option groups
        List<MenuOptionGroup> optionGroups = menuDto.getOptionGroups().stream().map(groupDto -> {
            MenuOptionGroup group = new MenuOptionGroup();
            group.setName(groupDto.getName());
            group.setMinSelect(groupDto.getMinSelect());
            group.setMaxSelect(groupDto.getMaxSelect());
            group.setRequired(groupDto.isRequired());
            group.setMenuItem(item); // bidirectional

            // 4. Map extras inside group
            List<MenuItemExtra> extras = groupDto.getExtras().stream().map(extraDto -> {
                MenuItemExtra extra = new MenuItemExtra();
                extra.setName(extraDto.getName());
                extra.setPrice(extraDto.getPrice());
                extra.setDefault(extraDto.isDefault());
                extra.setOptionGroup(group); // bidirectional
                return extra;
            }).collect(Collectors.toList());

            group.setExtras(extras);
            return group;
        }).collect(Collectors.toList());

        item.setOptionGroups(optionGroups);

        // 5. Save and return
        return menuItemRepository.save(item);
    }


    @Transactional
    public OrderDto markOrderReady(Long orderId, Long userId) {
        return orderRepository.findById(orderId).map(order -> {
            if (!order.getRestaurant().getAdmin().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized");
            }
            var updated = orderLifecycleService.transition(order, OrderStatus.READY_FOR_PICK_UP,
                    "restaurant:" + userId,
                    "Order ready for pickup");
            return loadOrderDto(updated.getId());
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private OrderDto loadOrderDto(Long orderId) {
        return orderRepository.findDetailedById(orderId)
                .map(OrderMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
