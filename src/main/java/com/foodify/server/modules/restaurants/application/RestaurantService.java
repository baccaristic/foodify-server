package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.delivery.application.DriverDispatchService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.orders.application.OrderLifecycleService;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.orders.support.OrderStatusGroups;
import com.foodify.server.modules.restaurants.domain.MenuCategory;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.MenuOptionGroup;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.dto.MenuItemRequestDTO;
import com.foodify.server.modules.restaurants.dto.OptionGroupDTO;
import com.foodify.server.modules.restaurants.dto.ExtraDTO;
import com.foodify.server.modules.restaurants.dto.MenuCategoryRequestDTO;
import com.foodify.server.modules.restaurants.repository.MenuCategoryRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final DriverDispatchService driverDispatchService;
    private final OrderLifecycleService orderLifecycleService;
    private final OrderNotificationMapper orderNotificationMapper;

    @Transactional(readOnly = true)
    public List<OrderNotificationDTO> getAllOrders(Restaurant restaurant) {
        return this.orderRepository.findAllByRestaurantOrderByDateDesc(restaurant)
                .stream()
                .map(orderNotificationMapper::toRestaurantDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderNotificationDTO> getActiveOrders(Long adminId) {
        return this.orderRepository
                .findAllByRestaurant_Admin_IdAndStatusInAndArchivedAtIsNullOrderByDateDesc(adminId, OrderStatusGroups.RESTAURANT_ACTIVE_STATUSES)
                .stream()
                .map(orderNotificationMapper::toRestaurantDto)
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
            driverDispatchService.beginSearch(savedOrder);
            return loadOrderNotification(savedOrder.getId());
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }
    @Transactional(readOnly = true)
    public OrderNotificationDTO getOrderForRestaurant(Long orderId, Long restaurantId) {
        return orderRepository.findDetailedById(orderId)
                .filter(order -> order.getRestaurant() != null && order.getRestaurant().getId().equals(restaurantId))
                .map(orderNotificationMapper::toRestaurantDto)
                .orElse(null);
    }

    public MenuItem addMenu(MenuItemRequestDTO menuDto, List<MultipartFile> files) throws IOException {
        MenuItem item = new MenuItem();

        Restaurant restaurant = restaurantRepository.findById(menuDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        item.setRestaurant(restaurant);

        populateMenuItemFromDto(item, menuDto, files, restaurant);
        return menuItemRepository.save(item);
    }

    public MenuItem updateMenu(Long menuId, MenuItemRequestDTO menuDto, List<MultipartFile> files) throws IOException {
        MenuItem menuItem = menuItemRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (menuItem.getRestaurant() == null || !menuItem.getRestaurant().getId().equals(menuDto.getRestaurantId())) {
            throw new RuntimeException("Menu item does not belong to restaurant");
        }

        populateMenuItemFromDto(menuItem, menuDto, files, menuItem.getRestaurant());
        return menuItemRepository.save(menuItem);
    }

    private void populateMenuItemFromDto(MenuItem item, MenuItemRequestDTO menuDto, List<MultipartFile> files, Restaurant restaurant) throws IOException {
        item.setName(menuDto.getName());
        item.setDescription(menuDto.getDescription());
        item.setPrice(menuDto.getPrice());
        item.setPopular(menuDto.isPopular());
        item.setPromotionLabel(menuDto.getPromotionLabel());
        item.setPromotionPrice(menuDto.getPromotionPrice());
        item.setPromotionActive(menuDto.isPromotionActive());
        syncCategories(item, menuDto.getCategoryIds(), restaurant);

        List<String> imageUrls = new ArrayList<>();
        if (menuDto.getImageUrls() != null) {
            imageUrls.addAll(menuDto.getImageUrls());
        }
        imageUrls.addAll(storeFiles(files));
        item.setImageUrls(imageUrls);

        syncOptionGroups(item, menuDto.getOptionGroups());
    }

    private void syncCategories(MenuItem item, List<Long> categoryIds, Restaurant restaurant) {
        item.getCategories().clear();
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        List<Long> sanitizedIds = categoryIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (sanitizedIds.isEmpty()) {
            return;
        }

        List<MenuCategory> categories = menuCategoryRepository.findAllById(sanitizedIds);
        Set<Long> foundIds = categories.stream().map(MenuCategory::getId).collect(Collectors.toSet());
        Set<Long> missing = sanitizedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toCollection(HashSet::new));

        if (!missing.isEmpty()) {
            throw new EntityNotFoundException("Some categories were not found: " + missing);
        }

        for (MenuCategory category : categories) {
            if (category.getRestaurant() == null || !category.getRestaurant().getId().equals(restaurant.getId())) {
                throw new RuntimeException("Category does not belong to restaurant");
            }
        }

        item.getCategories().addAll(categories);
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

    @Transactional(readOnly = true)
    public List<MenuCategory> getCategoriesForRestaurant(Long restaurantId) {
        return menuCategoryRepository.findByRestaurant_IdOrderByNameAsc(restaurantId);
    }

    @Transactional
    public MenuCategory createCategory(Long restaurantId, MenuCategoryRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        menuCategoryRepository.findByRestaurant_IdAndNameIgnoreCase(restaurantId, request.getName().trim())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Category already exists");
                });

        MenuCategory category = new MenuCategory();
        category.setName(request.getName().trim());
        category.setRestaurant(restaurant);
        return menuCategoryRepository.save(category);
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
                .map(orderNotificationMapper::toRestaurantDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
