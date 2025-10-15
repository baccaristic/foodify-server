package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.delivery.application.DriverAssignmentService;
import com.foodify.server.modules.delivery.location.DriverLocationService;
import com.foodify.server.modules.notifications.application.NotificationPreferenceService;
import com.foodify.server.modules.notifications.application.PushNotificationService;
import com.foodify.server.modules.notifications.application.UserDeviceService;
import com.foodify.server.modules.notifications.domain.NotificationType;
import com.foodify.server.modules.notifications.domain.UserDevice;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.orders.application.OrderLifecycleService;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.MenuOptionGroup;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.MenuItemRequestDTO;
import com.foodify.server.modules.restaurants.dto.OptionGroupDTO;
import com.foodify.server.modules.restaurants.dto.ExtraDTO;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private final OrderNotificationMapper orderNotificationMapper;
    private final NotificationPreferenceService notificationPreferenceService;

    private static final String ORDER_ATTEMPTED_DRIVERS_KEY_PREFIX = "order:drivers:attempted:";


    @Transactional(readOnly = true)
    public List<OrderNotificationDTO> getAllOrders(Restaurant restaurant) {
        return this.orderRepository.findAllByRestaurantOrderByDateDesc(restaurant)
                .stream()
                .map(orderNotificationMapper::toDto)
                .toList();
    }

    @Transactional
    public OrderNotificationDTO acceptOrder(Long id, Long userId) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getRestaurant().getAdmin().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized");
            }

            var savedOrder = orderLifecycleService.transition(order, OrderStatus.ACCEPTED,
                    "restaurant:" + userId,
                    "Restaurant accepted order");
            assignDriver(savedOrder);
            return loadOrderNotification(savedOrder.getId());
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
        if (!notificationPreferenceService.isEnabled(driverId, NotificationType.ORDER_UPDATES)) {
            return;
        }
        List<UserDevice> userDevices = userDeviceService.findByUser(driverId);
        userDevices.forEach(userDevice -> {
            try {
                pushNotificationService.sendOrderNotification(
                        userDevice.getDeviceToken(),
                        order.getId(),
                        "You have a new delivery request",
                        "You have recieved a new delivery request. You have 2 minutes to accept or decline.",
                        NotificationType.ORDER_UPDATES
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
    public OrderNotificationDTO getOrderForRestaurant(Long orderId, Long restaurantId) {
        return orderRepository.findDetailedById(orderId)
                .filter(order -> order.getRestaurant() != null && order.getRestaurant().getId().equals(restaurantId))
                .map(orderNotificationMapper::toDto)
                .orElse(null);
    }

    public MenuItem addMenu(MenuItemRequestDTO menuDto, List<MultipartFile> files) throws IOException {
        MenuItem item = new MenuItem();

        Restaurant restaurant = restaurantRepository.findById(menuDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        item.setRestaurant(restaurant);

        populateMenuItemFromDto(item, menuDto, files);
        return menuItemRepository.save(item);
    }

    public MenuItem updateMenu(Long menuId, MenuItemRequestDTO menuDto, List<MultipartFile> files) throws IOException {
        MenuItem menuItem = menuItemRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (menuItem.getRestaurant() == null || !menuItem.getRestaurant().getId().equals(menuDto.getRestaurantId())) {
            throw new RuntimeException("Menu item does not belong to restaurant");
        }

        populateMenuItemFromDto(menuItem, menuDto, files);
        return menuItemRepository.save(menuItem);
    }

    private void populateMenuItemFromDto(MenuItem item, MenuItemRequestDTO menuDto, List<MultipartFile> files) throws IOException {
        item.setName(menuDto.getName());
        item.setDescription(menuDto.getDescription());
        item.setPrice(menuDto.getPrice());
        item.setCategory(menuDto.getCategory());
        item.setPopular(menuDto.isPopular());
        item.setPromotionLabel(menuDto.getPromotionLabel());
        item.setPromotionPrice(menuDto.getPromotionPrice());
        item.setPromotionActive(menuDto.isPromotionActive());

        List<String> imageUrls = new ArrayList<>();
        if (menuDto.getImageUrls() != null) {
            imageUrls.addAll(menuDto.getImageUrls());
        }
        imageUrls.addAll(storeFiles(files));
        item.setImageUrls(imageUrls);

        syncOptionGroups(item, menuDto.getOptionGroups());
    }

    private List<String> storeFiles(List<MultipartFile> files) throws IOException {
        List<String> storedFiles = new ArrayList<>();
        if (files == null) {
            return storedFiles;
        }

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads").resolve(filename);
                Files.createDirectories(path.getParent());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                storedFiles.add(filename);
            }
        }
        return storedFiles;
    }

    private void syncOptionGroups(MenuItem item, List<OptionGroupDTO> optionGroupDtos) {
        Map<Long, MenuOptionGroup> existingGroups = item.getOptionGroups().stream()
                .filter(group -> group.getId() != null)
                .collect(Collectors.toMap(MenuOptionGroup::getId, group -> group, (g1, g2) -> g1, HashMap::new));

        List<MenuOptionGroup> updatedGroups = new ArrayList<>();
        if (optionGroupDtos != null) {
            for (OptionGroupDTO groupDto : optionGroupDtos) {
                MenuOptionGroup group = null;
                if (groupDto.getId() != null) {
                    group = existingGroups.remove(groupDto.getId());
                }
                if (group == null) {
                    group = new MenuOptionGroup();
                }

                group.setName(groupDto.getName());
                group.setMinSelect(groupDto.getMinSelect());
                group.setMaxSelect(groupDto.getMaxSelect());
                group.setRequired(groupDto.isRequired());
                group.setMenuItem(item);

                syncExtras(group, groupDto.getExtras());
                updatedGroups.add(group);
            }
        }

        item.getOptionGroups().clear();
        item.getOptionGroups().addAll(updatedGroups);
    }

    private void syncExtras(MenuOptionGroup group, List<ExtraDTO> extraDtos) {
        Map<Long, MenuItemExtra> existingExtras = group.getExtras().stream()
                .filter(extra -> extra.getId() != null)
                .collect(Collectors.toMap(MenuItemExtra::getId, extra -> extra, (e1, e2) -> e1, HashMap::new));

        List<MenuItemExtra> updatedExtras = new ArrayList<>();
        if (extraDtos != null) {
            for (ExtraDTO extraDto : extraDtos) {
                MenuItemExtra extra = null;
                if (extraDto.getId() != null) {
                    extra = existingExtras.remove(extraDto.getId());
                }
                if (extra == null) {
                    extra = new MenuItemExtra();
                }

                extra.setName(extraDto.getName());
                extra.setPrice(extraDto.getPrice());
                extra.setDefault(extraDto.isDefault());
                extra.setOptionGroup(group);
                updatedExtras.add(extra);
            }
        }

        group.getExtras().clear();
        group.getExtras().addAll(updatedExtras);
    }


    @Transactional
    public OrderNotificationDTO markOrderReady(Long orderId, Long userId) {
        return orderRepository.findById(orderId).map(order -> {
            if (!order.getRestaurant().getAdmin().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized");
            }
            var updated = orderLifecycleService.transition(order, OrderStatus.READY_FOR_PICK_UP,
                    "restaurant:" + userId,
                    "Order ready for pickup");
            return loadOrderNotification(updated.getId());
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private OrderNotificationDTO loadOrderNotification(Long orderId) {
        return orderRepository.findDetailedById(orderId)
                .map(orderNotificationMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
