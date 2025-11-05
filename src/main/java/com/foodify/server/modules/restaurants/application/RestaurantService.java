package com.foodify.server.modules.restaurants.application;

import com.foodify.server.modules.delivery.application.DriverDispatchService;
import com.foodify.server.modules.notifications.websocket.WebSocketService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.config.OrderViewProperties;
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
import com.foodify.server.modules.restaurants.domain.RestaurantSpecialDay;
import com.foodify.server.modules.restaurants.domain.RestaurantWeeklyOperatingHour;
import com.foodify.server.modules.restaurants.dto.ExtraDTO;
import com.foodify.server.modules.restaurants.dto.MenuCategoryRequestDTO;
import com.foodify.server.modules.restaurants.dto.MenuItemRequestDTO;
import com.foodify.server.modules.restaurants.dto.OperatingHoursResponse;
import com.foodify.server.modules.restaurants.dto.OptionGroupDTO;
import com.foodify.server.modules.restaurants.dto.SaveSpecialDayRequest;
import com.foodify.server.modules.restaurants.dto.UpdateWeeklyScheduleRequest;
import com.foodify.server.modules.restaurants.repository.MenuCategoryRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantOperatingHourRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantSpecialDayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final RestaurantOperatingHourRepository restaurantOperatingHourRepository;
    private final RestaurantSpecialDayRepository restaurantSpecialDayRepository;
    private final DriverDispatchService driverDispatchService;
    private final OrderLifecycleService orderLifecycleService;
    private final OrderNotificationMapper orderNotificationMapper;
    private final OrderViewProperties orderViewProperties;
    private final WebSocketService webSocketService;

    @Transactional(readOnly = true)
    public Page<OrderNotificationDTO> getAllOrders(
            Restaurant restaurant,
            Pageable pageable,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        Pageable effectivePageable;
        if (pageable == null || pageable.isUnpaged()) {
            int defaultSize = Math.max(orderViewProperties.getRestaurantSnapshotLimit(), 1);
            effectivePageable = PageRequest.of(0, defaultSize);
        } else {
            effectivePageable = pageable;
        }

        if (restaurant == null) {
            return Page.empty(effectivePageable);
        }

        LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = toDate != null ? toDate.atTime(LocalTime.MAX) : null;

        Page<Order> orders;

        if (fromDateTime != null && toDateTime != null) {
            orders = this.orderRepository.findAllByRestaurantAndDateBetween(
                    restaurant,
                    fromDateTime,
                    toDateTime,
                    effectivePageable
            );
        } else if (fromDateTime != null) {
            orders = this.orderRepository.findAllByRestaurantAndDateGreaterThanEqual(
                    restaurant,
                    fromDateTime,
                    effectivePageable
            );
        } else if (toDateTime != null) {
            orders = this.orderRepository.findAllByRestaurantAndDateLessThanEqual(
                    restaurant,
                    toDateTime,
                    effectivePageable
            );
        } else {
            orders = this.orderRepository.findAllByRestaurant(restaurant, effectivePageable);
        }

        return orders.map(orderNotificationMapper::toRestaurantDto);
    }

    @Transactional(readOnly = true)
    public List<OrderNotificationDTO> getActiveOrders(Long adminId) {
        if (adminId == null) {
            return List.of();
        }

        int limit = Math.max(orderViewProperties.getRestaurantSnapshotLimit(), 1);
        Slice<Order> slice = this.orderRepository
                .findAllByRestaurant_Admin_IdAndStatusInAndArchivedAtIsNullOrderByDateDesc(
                        adminId,
                        OrderStatusGroups.RESTAURANT_ACTIVE_STATUSES,
                        PageRequest.of(0, limit)
                );

        return slice.getContent().stream()
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

    @Transactional
    public OrderNotificationDTO updatePreparationEstimate(Long orderId, Long userId, Integer minutes) {
        if (minutes == null) {
            throw new IllegalArgumentException("Estimated minutes are required");
        }
        if (minutes < 1) {
            throw new IllegalArgumentException("Estimated minutes must be at least 1");
        }

        return orderRepository.findById(orderId).map(order -> {
            if (order.getRestaurant() == null
                    || order.getRestaurant().getAdmin() == null
                    || !Objects.equals(order.getRestaurant().getAdmin().getId(), userId)) {
                throw new RuntimeException("Unauthorized");
            }
            if (order.getStatus() != OrderStatus.PREPARING) {
                throw new IllegalStateException("Ready time can only be updated while the order is preparing");
            }

            order.setEstimatedReadyAt(LocalDateTime.now().plusMinutes(minutes));
            Order savedOrder = orderRepository.save(order);
            notifyClientOfOrder(savedOrder);
            return loadOrderNotification(savedOrder.getId());
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }


    /**
     * Starts preparing an order by transitioning it from ACCEPTED to PREPARING status.
     * This method should be called by the restaurant when they begin preparing the order
     * after a driver has accepted the delivery.
     *
     * @param orderId the ID of the order to start preparing
     * @param userId the ID of the restaurant admin user
     * @param minutes the estimated time in minutes until the order will be ready
     * @return the updated order notification DTO
     * @throws IllegalArgumentException if minutes is null or less than 1
     * @throws RuntimeException if the user is not authorized to modify this order
     * @throws IllegalStateException if the order is not in ACCEPTED status
     * @throws RuntimeException if the order is not found
     */
    @Transactional
    public OrderNotificationDTO startPreparingOrder(Long orderId, Long userId, Integer minutes) {
        if (minutes == null) {
            throw new IllegalArgumentException("Estimated minutes are required");
        }
        if (minutes < 1) {
            throw new IllegalArgumentException("Estimated minutes must be at least 1");
        }

        return orderRepository.findById(orderId).map(order -> {
            if (order.getRestaurant() == null
                    || order.getRestaurant().getAdmin() == null
                    || !Objects.equals(order.getRestaurant().getAdmin().getId(), userId)) {
                throw new RuntimeException("Unauthorized");
            }
            if (order.getStatus() != OrderStatus.ACCEPTED) {
                throw new IllegalStateException("Only accepted orders can be started for preparation");
            }

            order.setEstimatedReadyAt(LocalDateTime.now().plusMinutes(minutes));
            Order savedOrder = orderLifecycleService.transition(order, OrderStatus.PREPARING,
                    "restaurant:" + userId,
                    "Restaurant started preparing order");
            notifyClientOfOrder(savedOrder);
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
        applyAvailability(item, menuDto.getAvailable());
        syncCategories(item, menuDto.getCategoryIds(), restaurant);

        List<String> imageUrls = new ArrayList<>();
        if (menuDto.getImageUrls() != null) {
            imageUrls.addAll(menuDto.getImageUrls());
        }
        imageUrls.addAll(storeFiles(files));
        item.setImageUrls(imageUrls);

        syncOptionGroups(item, menuDto.getOptionGroups());
    }

    private void applyAvailability(MenuItem item, Boolean available) {
        if (available != null) {
            item.setAvailable(available);
        } else if (item.getId() == null) {
            item.setAvailable(true);
        }
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
    public MenuItem updateMenuAvailability(Long menuId, Long restaurantId, boolean available) {
        MenuItem menuItem = menuItemRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));

        if (menuItem.getRestaurant() == null || !Objects.equals(menuItem.getRestaurant().getId(), restaurantId)) {
            throw new RuntimeException("Menu item does not belong to restaurant");
        }

        menuItem.setAvailable(available);
        return menuItemRepository.save(menuItem);
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

    @Transactional(readOnly = true)
    public OperatingHoursResponse getOperatingHours(Long restaurantId) {
        Map<DayOfWeek, RestaurantWeeklyOperatingHour> weeklyByDay = restaurantOperatingHourRepository
                .findByRestaurant_IdOrderByDayOfWeekAsc(restaurantId)
                .stream()
                .collect(Collectors.toMap(RestaurantWeeklyOperatingHour::getDayOfWeek, Function.identity(), (left, right) -> left));

        List<OperatingHoursResponse.WeeklyScheduleEntry> weeklySchedule = Arrays.stream(DayOfWeek.values())
                .map(day -> {
                    RestaurantWeeklyOperatingHour hour = weeklyByDay.get(day);
                    if (hour == null) {
                        return new OperatingHoursResponse.WeeklyScheduleEntry(day, false, null, null);
                    }
                    return toWeeklyScheduleDto(hour);
                })
                .toList();

        List<OperatingHoursResponse.SpecialDay> specialDays = restaurantSpecialDayRepository
                .findByRestaurant_IdOrderByDateAsc(restaurantId)
                .stream()
                .map(this::toSpecialDayDto)
                .toList();

        return new OperatingHoursResponse(weeklySchedule, specialDays);
    }

    @Transactional
    public OperatingHoursResponse updateWeeklySchedule(Long restaurantId, UpdateWeeklyScheduleRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        restaurant.getOperatingHours().clear();

        if (request != null && request.days() != null) {
            Set<DayOfWeek> seen = new HashSet<>();
            for (UpdateWeeklyScheduleRequest.DaySchedule daySchedule : request.days()) {
                if (daySchedule == null || daySchedule.day() == null) {
                    throw new IllegalArgumentException("Day of week is required for schedule entry");
                }
                if (!seen.add(daySchedule.day())) {
                    throw new IllegalArgumentException("Duplicate day provided in weekly schedule: " + daySchedule.day());
                }

                RestaurantWeeklyOperatingHour hour = new RestaurantWeeklyOperatingHour();
                hour.setDayOfWeek(daySchedule.day());
                hour.setOpen(daySchedule.open());
                if (daySchedule.open()) {
                    if (daySchedule.opensAt() == null || daySchedule.closesAt() == null) {
                        throw new IllegalArgumentException("Open days must include opening and closing times");
                    }
                    if (!daySchedule.opensAt().isBefore(daySchedule.closesAt())) {
                        throw new IllegalArgumentException("Opening time must be before closing time");
                    }
                    hour.setOpensAt(daySchedule.opensAt());
                    hour.setClosesAt(daySchedule.closesAt());
                } else {
                    hour.setOpensAt(null);
                    hour.setClosesAt(null);
                }
                hour.setRestaurant(restaurant);
                restaurant.getOperatingHours().add(hour);
            }
        }

        updateRestaurantSummaryHours(restaurant);
        restaurantRepository.save(restaurant);

        return getOperatingHours(restaurantId);
    }

    @Transactional
    public OperatingHoursResponse.SpecialDay addSpecialDay(Long restaurantId, SaveSpecialDayRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        RestaurantSpecialDay specialDay = new RestaurantSpecialDay();
        specialDay.setRestaurant(restaurant);
        applySpecialDayData(specialDay, request);

        return toSpecialDayDto(restaurantSpecialDayRepository.save(specialDay));
    }

    @Transactional
    public OperatingHoursResponse.SpecialDay updateSpecialDay(Long restaurantId, Long specialDayId, SaveSpecialDayRequest request) {
        RestaurantSpecialDay specialDay = restaurantSpecialDayRepository.findByIdAndRestaurant_Id(specialDayId, restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Special day not found"));

        applySpecialDayData(specialDay, request);
        return toSpecialDayDto(restaurantSpecialDayRepository.save(specialDay));
    }

    @Transactional
    public void deleteSpecialDay(Long restaurantId, Long specialDayId) {
        RestaurantSpecialDay specialDay = restaurantSpecialDayRepository.findByIdAndRestaurant_Id(specialDayId, restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Special day not found"));
        restaurantSpecialDayRepository.delete(specialDay);
    }

    private void notifyClientOfOrder(Order order) {
        if (order == null || order.getClient() == null) {
            return;
        }

        webSocketService.notifyClient(order.getClient().getId(), order);
    }

    private OperatingHoursResponse.SpecialDay toSpecialDayDto(RestaurantSpecialDay specialDay) {
        return new OperatingHoursResponse.SpecialDay(
                specialDay.getId(),
                specialDay.getName(),
                specialDay.getDate(),
                specialDay.isOpen(),
                specialDay.isOpen() ? specialDay.getOpensAt() : null,
                specialDay.isOpen() ? specialDay.getClosesAt() : null
        );
    }

    private OperatingHoursResponse.WeeklyScheduleEntry toWeeklyScheduleDto(RestaurantWeeklyOperatingHour hour) {
        return new OperatingHoursResponse.WeeklyScheduleEntry(
                hour.getDayOfWeek(),
                hour.isOpen(),
                hour.isOpen() ? hour.getOpensAt() : null,
                hour.isOpen() ? hour.getClosesAt() : null
        );
    }

    private void applySpecialDayData(RestaurantSpecialDay specialDay, SaveSpecialDayRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request payload is required");
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Special day name is required");
        }
        if (request.date() == null) {
            throw new IllegalArgumentException("Special day date is required");
        }

        specialDay.setName(request.name().trim());
        specialDay.setDate(request.date());
        specialDay.setOpen(request.open());

        if (request.open()) {
            if (request.opensAt() == null || request.closesAt() == null) {
                throw new IllegalArgumentException("Open special days must include opening and closing times");
            }
            if (!request.opensAt().isBefore(request.closesAt())) {
                throw new IllegalArgumentException("Opening time must be before closing time");
            }
            specialDay.setOpensAt(request.opensAt());
            specialDay.setClosesAt(request.closesAt());
        } else {
            specialDay.setOpensAt(null);
            specialDay.setClosesAt(null);
        }
    }

    private void updateRestaurantSummaryHours(Restaurant restaurant) {
        Optional<RestaurantWeeklyOperatingHour> firstOpen = restaurant.getOperatingHours().stream()
                .filter(RestaurantWeeklyOperatingHour::isOpen)
                .sorted(Comparator.comparing(RestaurantWeeklyOperatingHour::getDayOfWeek))
                .findFirst();

        String opening = firstOpen.map(RestaurantWeeklyOperatingHour::getOpensAt).map(this::formatTime).orElse(null);
        String closing = firstOpen.map(RestaurantWeeklyOperatingHour::getClosesAt).map(this::formatTime).orElse(null);

        restaurant.setOpeningHours(opening);
        restaurant.setClosingHours(closing);
    }

    private String formatTime(LocalTime time) {
        return time == null ? null : time.toString();
    }

    private OrderNotificationDTO loadOrderNotification(Long orderId) {
        return orderRepository.findDetailedById(orderId)
                .map(orderNotificationMapper::toRestaurantDto)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }
}
