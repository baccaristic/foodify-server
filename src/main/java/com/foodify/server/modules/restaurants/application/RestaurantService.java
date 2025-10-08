package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.delivery.application.DriverService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final DriverLocationService driverLocationService;
    private final StringRedisTemplate redisTemplate;
    private final PushNotificationService pushNotificationService;
    private final UserDeviceService userDeviceService;
    private final DriverService driverService;
    private final WebSocketService webSocketService;
    private final OrderLifecycleService orderLifecycleService;


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
        List<String> driverIds = driverLocationService.findClosestDrivers(
                order.getRestaurant().getLatitude(),
                order.getRestaurant().getLongitude(),
                50, 5
        );

        if (driverIds.isEmpty()) {
           // webSocketService.notifyRestaurant(order.getRestaurant().getId(), "No drivers available");
            return;
        }

        String driverId = driverIds.get(0);
        driverLocationService.markPending(driverId, order.getId());
        order.setPendingDriver(driverService.findById(Long.valueOf(driverId)));
        order = orderRepository.save(order);
        webSocketService.notifyDriverUpcoming(Long.valueOf(driverId), order);
        List<UserDevice> userDevices = userDeviceService.findByUser(Long.valueOf(driverId));
        final Order[] finalOrder = {order};
        userDevices.forEach(userDevice -> {
            try {
                pushNotificationService.sendOrderNotification(
                        userDevice.getDeviceToken(),
                        finalOrder[0].getId(),
                        "You have a new delivery request",
                        "You have recieved a new delivery request. You have 2 minutes to accept or decline.",
                        NotificationType.ORDER_DRIVER_NEW_ORDER
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // ✅ Start 30s timer
        final Order[] finalOrder1 = {order};
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            // Check if still pending
            if (driverLocationService.isAvailable(driverId)) return; // already reset
            String status = redisTemplate.opsForValue().get("driver:status:" + driverId);
            if (finalOrder1[0].getDelivery() == null) {
                // Timeout: release driver + try next one
                driverLocationService.markAvailable(driverId);
                finalOrder1[0].setPendingDriver(null);
                finalOrder1[0] = orderRepository.save(finalOrder1[0]);
                assignDriver(finalOrder1[0]);
            }
        }, 30, TimeUnit.SECONDS);
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
