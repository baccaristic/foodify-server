package com.foodify.server.service;

import com.foodify.server.Redis.DriverLocationService;
import com.foodify.server.dto.MenuItemRequestDTO;
import com.foodify.server.enums.NotificationType;
import com.foodify.server.enums.OrderStatus;
import com.foodify.server.firebase.PushNotificationService;
import com.foodify.server.models.*;
import com.foodify.server.repository.MenuItemRepository;
import com.foodify.server.repository.OrderRepository;
import com.foodify.server.repository.RestaurantRepository;
import com.foodify.server.repository.UserRepository;
import com.foodify.server.ws.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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


    public List<Order> getAllOrders(Restaurant restaurant) {
        return this.orderRepository.findAllByRestaurantOrderByDateDesc(restaurant);
    }

    public Order acceptOrder(Long id, Long userId) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getRestaurant().getAdmin().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized");
            }

            order.setStatus(OrderStatus.ACCEPTED);
            Order savedOrder = orderRepository.save(order);
            List<UserDevice> userDevices = userDeviceService.findByUser(savedOrder.getClient().getId());
            userDevices.forEach(userDevice -> {
                try {
                    this.pushNotificationService.sendOrderNotification(
                            userDevice.getDeviceToken(), savedOrder.getId(),
                            "Your order have been accepted",
                            "Order is accepted by restaurant, tap to track it",
                            NotificationType.ORDER_CLIENT_ORDER_ACCEPTED
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            assignDriver(savedOrder);

            return savedOrder;
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
        orderRepository.save(order);
        List<UserDevice> userDevices = userDeviceService.findByUser(Long.valueOf(driverId));
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

        // ✅ Start 30s timer
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            // Check if still pending
            if (driverLocationService.isAvailable(driverId)) return; // already reset
            String status = redisTemplate.opsForValue().get("driver:status:" + driverId);
            if (status != null && status.equals("PENDING:" + order.getId())) {
                // Timeout: release driver + try next one
                driverLocationService.markAvailable(driverId);
                assignDriver(order);
            }
        }, 30, TimeUnit.SECONDS);
    }


    public Order getOrderById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    public MenuItem addMenu(MenuItemRequestDTO menuDto, MultipartFile file) throws IOException {

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads").resolve(filename);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);


        MenuItem item = new MenuItem();
        item.setName(menuDto.getName());
        item.setDescription(menuDto.getDescription());
        item.setPrice(menuDto.getPrice());
        item.setCategory(menuDto.getCategory());
        item.setPopular(menuDto.isPopular());
        item.setImageUrl(filename);

        Restaurant restaurant = restaurantRepository.findById(menuDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        item.setRestaurant(restaurant);

        // Map Extras
        List<MenuItemExtra> extras = menuDto.getExtras().stream().map(dto -> {
            MenuItemExtra extra = new MenuItemExtra();
            extra.setName(dto.getName());
            extra.setPrice(dto.getPrice());
            extra.setRequired(dto.isRequired());
            extra.setMenuItem(item); // bidirectional
            return extra;
        }).collect(Collectors.toList());
        item.setExtras(extras);

        return menuItemRepository.save(item);
    }
}
