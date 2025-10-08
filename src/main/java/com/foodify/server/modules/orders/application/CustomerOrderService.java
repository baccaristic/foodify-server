package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.addresses.application.SavedAddressDirectoryService;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.application.ClientDirectoryService;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.domain.OrderStatus;
import com.foodify.server.modules.orders.domain.catalog.OrderItemCatalogSnapshot;
import com.foodify.server.modules.orders.domain.catalog.OrderItemExtraSnapshot;
import com.foodify.server.modules.orders.domain.catalog.OrderRestaurantSnapshot;
import com.foodify.server.modules.orders.dto.LocationDto;
import com.foodify.server.modules.orders.dto.OrderItemRequest;
import com.foodify.server.modules.orders.dto.OrderRequest;
import com.foodify.server.modules.orders.dto.OrderNotificationDTO;
import com.foodify.server.modules.orders.dto.OrderWorkflowStepDto;
import com.foodify.server.modules.orders.dto.SavedAddressSummaryDto;
import com.foodify.server.modules.orders.dto.response.CreateOrderResponse;
import com.foodify.server.modules.orders.mapper.OrderNotificationMapper;
import com.foodify.server.modules.orders.mapper.SavedAddressSummaryMapper;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.sync.CatalogSnapshotCache;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.application.RestaurantCatalogService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private static final List<OrderStatus> STANDARD_FLOW = List.of(
            OrderStatus.PENDING,
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY,
            OrderStatus.DELIVERED
    );

    private static final Map<OrderStatus, String> WORKFLOW_TITLES = new EnumMap<>(OrderStatus.class);
    private static final Map<OrderStatus, String> WORKFLOW_DESCRIPTIONS = new EnumMap<>(OrderStatus.class);
    private static final List<OrderStatus> ONGOING_STATUSES = List.of(
            OrderStatus.PENDING,
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICK_UP,
            OrderStatus.IN_DELIVERY
    );

    static {
        WORKFLOW_TITLES.put(OrderStatus.PENDING, "Order placed");
        WORKFLOW_TITLES.put(OrderStatus.ACCEPTED, "Restaurant accepted");
        WORKFLOW_TITLES.put(OrderStatus.PREPARING, "Preparing order");
        WORKFLOW_TITLES.put(OrderStatus.READY_FOR_PICK_UP, "Ready for pick-up");
        WORKFLOW_TITLES.put(OrderStatus.IN_DELIVERY, "On the way");
        WORKFLOW_TITLES.put(OrderStatus.DELIVERED, "Delivered");
        WORKFLOW_TITLES.put(OrderStatus.CANCELED, "Order canceled");
        WORKFLOW_TITLES.put(OrderStatus.REJECTED, "Order rejected");

        WORKFLOW_DESCRIPTIONS.put(OrderStatus.PENDING, "We've received your order and will notify the restaurant.");
        WORKFLOW_DESCRIPTIONS.put(OrderStatus.ACCEPTED, "The restaurant confirmed your order and will start preparing it soon.");
        WORKFLOW_DESCRIPTIONS.put(OrderStatus.PREPARING, "The restaurant is carefully preparing your dishes.");
        WORKFLOW_DESCRIPTIONS.put(OrderStatus.READY_FOR_PICK_UP, "Your order is ready for pickup by a driver.");
        WORKFLOW_DESCRIPTIONS.put(OrderStatus.IN_DELIVERY, "A driver is on the way to you with your order.");
        WORKFLOW_DESCRIPTIONS.put(OrderStatus.DELIVERED, "Enjoy your meal! The order has been delivered.");
        WORKFLOW_DESCRIPTIONS.put(OrderStatus.CANCELED, "This order was canceled. Contact support if you need more details.");
        WORKFLOW_DESCRIPTIONS.put(OrderStatus.REJECTED, "The restaurant couldn't fulfill this order.");
    }

    private final OrderRepository orderRepository;
    private final ClientDirectoryService clientDirectoryService;
    private final RestaurantCatalogService restaurantCatalogService;
    private final OrderLifecycleService orderLifecycleService;
    private final SavedAddressDirectoryService savedAddressDirectoryService;
    private final OrderNotificationMapper orderNotificationMapper;
    private final CatalogSnapshotCache catalogSnapshotCache;

    @Transactional
    public CreateOrderResponse placeOrder(Long clientId, OrderRequest request) {
        request.setUserId(clientId);
        Order order = createOrder(clientId, request);
        return mapToResponse(order);
    }

    @Transactional
    public void placeOrder(OrderRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User id is required to place an order");
        }
        createOrder(request.getUserId(), request);
    }

    private Order createOrder(Long clientId, OrderRequest request) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client id is required");
        }

        Client client = clientDirectoryService.getClientOrThrow(clientId);

        if (orderRepository.existsByClient_IdAndStatusInAndArchivedAtIsNull(clientId, ONGOING_STATUSES)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Client already has an ongoing order");
        }

        Long restaurantId = Optional.ofNullable(request.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant id is required"));

        Restaurant restaurant = restaurantCatalogService.getRestaurantOrThrow(restaurantId);

        SavedAddress savedAddress = resolveSavedAddress(clientId, request.getSavedAddressId());

        Order order = new Order();
        order.setClient(client);
        order.setRestaurant(applyRestaurantSnapshot(buildRestaurantSnapshot(restaurant)));
        order.setPaymentMethod(request.getPaymentMethod());
        if (request.getLocation() != null) {
            order.setLat(request.getLocation().getLat());
            order.setLng(request.getLocation().getLng());
        }
        order.setDeliveryAddress(request.getDeliveryAddress());
        if (savedAddress != null) {
            order.setSavedAddress(savedAddress);
            order.setDeliveryAddress(savedAddress.getFormattedAddress());
            LocationDto savedLocation = SavedAddressSummaryMapper.toLocation(savedAddress);
            if (savedLocation != null) {
                order.setLat(savedLocation.getLat());
                order.setLng(savedLocation.getLng());
            }
        }
        order.setStatus(OrderStatus.PENDING);
        order.setOrderTime(LocalDateTime.now());
        order.setDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : Optional.ofNullable(request.getItems()).orElse(Collections.emptyList())) {
            if (itemRequest.getMenuItemId() == null) {
                throw new IllegalArgumentException("Menu item id is required");
            }
            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            MenuItem menuItem = restaurantCatalogService.getMenuItemOrThrow(itemRequest.getMenuItemId());

            if (!Objects.equals(menuItem.getRestaurant().getId(), restaurant.getId())) {
                throw new IllegalArgumentException("Menu item does not belong to the selected restaurant");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            OrderItemCatalogSnapshot catalogSnapshot = buildCatalogSnapshot(menuItem);
            catalogSnapshot = applyMenuItemSnapshot(restaurant.getId(), catalogSnapshot);
            orderItem.setCatalogItem(catalogSnapshot);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSpecialInstructions(itemRequest.getSpecialInstructions());

            List<Long> extraIds = Optional.ofNullable(itemRequest.getExtraIds()).orElse(Collections.emptyList());
            List<MenuItemExtra> extras = extraIds.isEmpty()
                    ? Collections.emptyList()
                    : restaurantCatalogService.getMenuItemExtras(extraIds);

            if (!extras.isEmpty()) {
                validateExtras(menuItem, extras);
            }

            orderItem.setMenuItemExtras(buildExtraSnapshots(menuItem, extras));
            orderItems.add(orderItem);
        }

        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("At least one item is required to place an order");
        }

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        orderLifecycleService.registerCreation(savedOrder, "client:" + clientId);
        return savedOrder;
    }

    @Transactional
    public OrderNotificationDTO getOngoingOrder(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client id is required");
        }

        return orderRepository
                .findFirstByClient_IdAndStatusInAndArchivedAtIsNullOrderByDateDesc(clientId, ONGOING_STATUSES)
                .map(orderNotificationMapper::toDto)
                .orElse(null);
    }

    private void validateExtras(MenuItem menuItem, List<MenuItemExtra> extras) {
        for (MenuItemExtra extra : extras) {
            if (extra.getOptionGroup() == null
                    || extra.getOptionGroup().getMenuItem() == null
                    || !Objects.equals(extra.getOptionGroup().getMenuItem().getId(), menuItem.getId())) {
                throw new IllegalArgumentException("One or more extras do not belong to the selected menu item");
            }
        }
    }

    private CreateOrderResponse mapToResponse(Order order) {
        List<CreateOrderResponse.OrderedItem> orderedItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal extrasTotal = BigDecimal.ZERO;

        for (OrderItem item : Optional.ofNullable(order.getItems()).orElse(Collections.emptyList())) {
            BigDecimal unitPrice = resolveUnitPrice(item);
            BigDecimal extrasPerUnit = Optional.ofNullable(item.getMenuItemExtras()).orElse(Collections.emptyList())
                    .stream()
                    .map(extra -> BigDecimal.valueOf(Optional.ofNullable(extra.getPrice()).orElse(0.0)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal lineSubtotal = unitPrice.multiply(quantity);
            BigDecimal lineExtras = extrasPerUnit.multiply(quantity);
            BigDecimal lineTotal = lineSubtotal.add(lineExtras);

            subtotal = subtotal.add(lineSubtotal);
            extrasTotal = extrasTotal.add(lineExtras);

            List<CreateOrderResponse.Extra> extras = Optional.ofNullable(item.getMenuItemExtras()).orElse(Collections.emptyList())
                    .stream()
                    .map(extra -> new CreateOrderResponse.Extra(
                            extra.getExtraId(),
                            extra.getName(),
                            BigDecimal.valueOf(Optional.ofNullable(extra.getPrice()).orElse(0.0))
                    ))
                    .collect(Collectors.toList());

            orderedItems.add(new CreateOrderResponse.OrderedItem(
                    item.getCatalogItem() != null ? item.getCatalogItem().getMenuItemId() : null,
                    item.getCatalogItem() != null ? item.getCatalogItem().getMenuItemName() : null,
                    item.getQuantity(),
                    unitPrice,
                    extrasPerUnit,
                    lineTotal,
                    extras,
                    item.getSpecialInstructions()
            ));
        }

        BigDecimal total = subtotal.add(extrasTotal);

        CreateOrderResponse.RestaurantSummary restaurantSummary = order.getRestaurant() == null ? null :
                new CreateOrderResponse.RestaurantSummary(
                        order.getRestaurant().getId(),
                        order.getRestaurant().getName(),
                        order.getRestaurant().getImageUrl()
                );

        SavedAddressSummaryDto savedAddress = SavedAddressSummaryMapper.from(order.getSavedAddress());
        CreateOrderResponse.DeliverySummary deliverySummary = new CreateOrderResponse.DeliverySummary(
                order.getDeliveryAddress(),
                new LocationDto(order.getLat(), order.getLng()),
                savedAddress
        );

        CreateOrderResponse.PaymentSummary paymentSummary = new CreateOrderResponse.PaymentSummary(
                order.getPaymentMethod(),
                subtotal,
                extrasTotal,
                total
        );

        return new CreateOrderResponse(
                order.getId(),
                order.getStatus(),
                restaurantSummary,
                deliverySummary,
                paymentSummary,
                orderedItems,
                buildWorkflow(order.getStatus())
        );
    }

    private BigDecimal resolveUnitPrice(OrderItem orderItem) {
        if (orderItem == null || orderItem.getCatalogItem() == null) {
            return BigDecimal.ZERO;
        }

        if (Boolean.TRUE.equals(orderItem.getCatalogItem().getPromotionActive())
                && orderItem.getCatalogItem().getPromotionPrice() != null) {
            return BigDecimal.valueOf(orderItem.getCatalogItem().getPromotionPrice());
        }

        return BigDecimal.valueOf(Optional.ofNullable(orderItem.getCatalogItem().getBasePrice()).orElse(0.0));
    }

    private OrderRestaurantSnapshot buildRestaurantSnapshot(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        return OrderRestaurantSnapshot.builder()
                .id(restaurant.getId())
                .adminId(restaurant.getAdmin() != null ? restaurant.getAdmin().getId() : null)
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .imageUrl(restaurant.getImageUrl())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .build();
    }

    private OrderRestaurantSnapshot applyRestaurantSnapshot(OrderRestaurantSnapshot localSnapshot) {
        if (localSnapshot == null || localSnapshot.getId() == null) {
            return localSnapshot;
        }
        return catalogSnapshotCache.getRestaurant(localSnapshot.getId())
                .map(remote -> localSnapshot.toBuilder()
                        .adminId(firstNonNull(remote.adminId(), localSnapshot.getAdminId()))
                        .name(firstNonNull(remote.name(), localSnapshot.getName()))
                        .address(firstNonNull(remote.address(), localSnapshot.getAddress()))
                        .phone(firstNonNull(remote.phone(), localSnapshot.getPhone()))
                        .imageUrl(firstNonNull(remote.imageUrl(), localSnapshot.getImageUrl()))
                        .latitude(firstNonNull(remote.latitude(), localSnapshot.getLatitude()))
                        .longitude(firstNonNull(remote.longitude(), localSnapshot.getLongitude()))
                        .build())
                .orElse(localSnapshot);
    }

    private OrderItemCatalogSnapshot buildCatalogSnapshot(MenuItem menuItem) {
        if (menuItem == null) {
            return null;
        }
        return OrderItemCatalogSnapshot.builder()
                .menuItemId(menuItem.getId())
                .menuItemName(menuItem.getName())
                .basePrice(menuItem.getPrice())
                .promotionActive(menuItem.getPromotionActive())
                .promotionPrice(menuItem.getPromotionPrice())
                .build();
    }

    private OrderItemCatalogSnapshot applyMenuItemSnapshot(Long restaurantId, OrderItemCatalogSnapshot localSnapshot) {
        if (localSnapshot == null || localSnapshot.getMenuItemId() == null) {
            return localSnapshot;
        }
        return catalogSnapshotCache.getMenuItem(localSnapshot.getMenuItemId())
                .map(remote -> {
                    if (restaurantId != null && remote.restaurantId() != null
                            && !Objects.equals(remote.restaurantId(), restaurantId)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu item does not belong to the selected restaurant");
                    }
                    if (Boolean.FALSE.equals(remote.available())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu item is currently unavailable");
                    }
                    return localSnapshot.toBuilder()
                            .basePrice(firstNonNull(remote.price(), localSnapshot.getBasePrice()))
                            .promotionActive(firstNonNull(remote.promotionActive(), localSnapshot.getPromotionActive()))
                            .promotionPrice(firstNonNull(remote.promotionPrice(), localSnapshot.getPromotionPrice()))
                            .build();
                })
                .orElse(localSnapshot);
    }

    private OrderItemExtraSnapshot buildExtraSnapshot(MenuItemExtra extra) {
        if (extra == null) {
            return null;
        }
        return OrderItemExtraSnapshot.builder()
                .extraId(extra.getId())
                .name(extra.getName())
                .price(extra.getPrice())
                .build();
    }

    private List<OrderItemExtraSnapshot> buildExtraSnapshots(MenuItem menuItem, List<MenuItemExtra> extras) {
        Long menuItemId = menuItem != null ? menuItem.getId() : null;
        return extras.stream()
                .map(extra -> applyExtraSnapshot(buildExtraSnapshot(extra), menuItemId))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private OrderItemExtraSnapshot applyExtraSnapshot(OrderItemExtraSnapshot localSnapshot, Long menuItemId) {
        if (localSnapshot == null || localSnapshot.getExtraId() == null) {
            return localSnapshot;
        }
        return catalogSnapshotCache.getExtra(localSnapshot.getExtraId())
                .map(remote -> {
                    if (menuItemId != null && remote.menuItemId() != null
                            && !Objects.equals(remote.menuItemId(), menuItemId)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more extras do not belong to the selected menu item");
                    }
                    if (Boolean.FALSE.equals(remote.available())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more selected extras are unavailable");
                    }
                    return localSnapshot.toBuilder()
                            .name(firstNonNull(remote.name(), localSnapshot.getName()))
                            .price(firstNonNull(remote.price(), localSnapshot.getPrice()))
                            .build();
                })
                .orElse(localSnapshot);
    }

    private SavedAddress resolveSavedAddress(Long clientId, UUID savedAddressId) {
        if (savedAddressId == null) {
            return null;
        }

        return savedAddressDirectoryService.findByIdAndClient(savedAddressId, clientId)
                .orElseThrow(() -> new EntityNotFoundException("Saved address not found for client"));
    }

    private <T> T firstNonNull(T primary, T fallback) {
        return primary != null ? primary : fallback;
    }

    private List<OrderWorkflowStepDto> buildWorkflow(OrderStatus currentStatus) {
        List<OrderStatus> flow = new ArrayList<>(STANDARD_FLOW);
        if (currentStatus == OrderStatus.CANCELED || currentStatus == OrderStatus.REJECTED) {
            flow.add(currentStatus);
        } else if (!flow.contains(currentStatus)) {
            flow.add(currentStatus);
        }

        int currentIndex = flow.indexOf(currentStatus);
        if (currentIndex == -1) {
            currentIndex = 0;
        }

        List<OrderWorkflowStepDto> steps = new ArrayList<>();
        for (int i = 0; i < flow.size(); i++) {
            OrderStatus status = flow.get(i);
            steps.add(new OrderWorkflowStepDto(
                    status,
                    WORKFLOW_TITLES.getOrDefault(status, humanize(status)),
                    WORKFLOW_DESCRIPTIONS.getOrDefault(status, ""),
                    i < currentIndex,
                    i == currentIndex
            ));
        }

        return steps;
    }

    private String humanize(OrderStatus status) {
        String name = status.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
