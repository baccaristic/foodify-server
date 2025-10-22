package com.foodify.server.modules.orders.application;

import com.foodify.server.modules.addresses.domain.SavedAddress;
import com.foodify.server.modules.addresses.repository.SavedAddressRepository;
import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.orders.domain.OrderItem;
import com.foodify.server.modules.orders.domain.OrderStatus;
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
import com.foodify.server.modules.orders.support.OrderPricingCalculator;
import com.foodify.server.modules.orders.support.OrderPricingCalculator.OrderItemPricing;
import com.foodify.server.modules.orders.support.OrderPricingCalculator.OrderPricingBreakdown;
import com.foodify.server.modules.restaurants.application.DeliveryFeeCalculator;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import io.micrometer.core.annotation.Timed;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.math.RoundingMode;

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
    private final ClientRepository clientRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemExtraRepository menuItemExtraRepository;
    private final OrderLifecycleService orderLifecycleService;
    private final SavedAddressRepository savedAddressRepository;
    private final OrderNotificationMapper orderNotificationMapper;
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    @Transactional
    @Timed(value = "orders.place.sync", description = "Time spent placing orders via API", histogram = true)
    public CreateOrderResponse placeOrder(Long clientId, OrderRequest request) {
        request.setUserId(clientId);
        Order order = createOrder(clientId, request);
        return mapToResponse(order);
    }

    @Transactional
    @Timed(value = "orders.place.async", description = "Time spent processing queued order requests", histogram = true)
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

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (orderRepository.existsByClient_IdAndStatusInAndArchivedAtIsNull(clientId, ONGOING_STATUSES)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Client already has an ongoing order");
        }

        Long restaurantId = Optional.ofNullable(request.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant id is required"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        SavedAddress savedAddress = resolveSavedAddress(clientId, request.getSavedAddressId());

        Order order = new Order();
        order.setClient(client);
        order.setRestaurant(restaurant);
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

        List<OrderItemRequest> itemRequests = Optional.ofNullable(request.getItems()).orElse(Collections.emptyList());
        if (itemRequests.isEmpty()) {
            throw new IllegalArgumentException("At least one item is required to place an order");
        }

        Set<Long> menuItemIds = new HashSet<>();
        Set<Long> requestedExtraIds = new HashSet<>();
        for (OrderItemRequest itemRequest : itemRequests) {
            if (itemRequest.getMenuItemId() == null) {
                throw new IllegalArgumentException("Menu item id is required");
            }
            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
            menuItemIds.add(itemRequest.getMenuItemId());
            Optional.ofNullable(itemRequest.getExtraIds())
                    .orElse(Collections.emptyList())
                    .forEach(requestedExtraIds::add);
        }

        Map<Long, MenuItem> menuItemsById = menuItemRepository.findAllById(menuItemIds)
                .stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));

        if (menuItemsById.size() != menuItemIds.size()) {
            throw new EntityNotFoundException("Menu item not found");
        }

        Map<Long, MenuItemExtra> extrasById = requestedExtraIds.isEmpty()
                ? Collections.emptyMap()
                : menuItemExtraRepository.findAllById(requestedExtraIds)
                        .stream()
                        .collect(Collectors.toMap(MenuItemExtra::getId, Function.identity(), (existing, replacement) -> existing, HashMap::new));

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : itemRequests) {
            MenuItem menuItem = menuItemsById.get(itemRequest.getMenuItemId());
            if (menuItem == null) {
                throw new EntityNotFoundException("Menu item not found");
            }

            if (!Objects.equals(menuItem.getRestaurant().getId(), restaurant.getId())) {
                throw new IllegalArgumentException("Menu item does not belong to the selected restaurant");
            }

            if (!menuItem.isAvailable()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more menu items are unavailable");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSpecialInstructions(itemRequest.getSpecialInstructions());

            List<MenuItemExtra> extras = Optional.ofNullable(itemRequest.getExtraIds()).orElse(Collections.emptyList())
                    .stream()
                    .map(extrasById::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!extras.isEmpty()) {
                validateExtras(menuItem, extras);
            }

            orderItem.setMenuItemExtras(new ArrayList<>(extras));

            OrderItemPricing itemPricing = OrderPricingCalculator.calculateItemPricing(orderItem);
            orderItem.setUnitBasePrice(itemPricing.unitBasePrice());
            orderItem.setUnitPrice(itemPricing.unitPrice());
            orderItem.setUnitExtrasPrice(itemPricing.unitExtrasPrice());
            orderItem.setLineSubtotal(itemPricing.lineSubtotal());
            orderItem.setPromotionDiscount(itemPricing.promotionDiscount());
            orderItem.setLineItemsTotal(itemPricing.lineItemsTotal());
            orderItem.setExtrasTotal(itemPricing.extrasTotal());
            orderItem.setLineTotal(itemPricing.lineTotal());

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        OrderPricingBreakdown pricing = OrderPricingCalculator.calculatePricing(orderItems);
        order.setItemsSubtotal(pricing.itemsSubtotal());
        order.setExtrasTotal(pricing.extrasTotal());
        order.setPromotionDiscount(pricing.promotionDiscount());
        order.setItemsTotal(pricing.itemsTotal());

        Double clientLatitude = order.getLat();
        Double clientLongitude = order.getLng();
        if (clientLatitude != null && clientLongitude != null
                && clientLatitude == 0.0 && clientLongitude == 0.0) {
            clientLatitude = null;
            clientLongitude = null;
        }

        BigDecimal deliveryFee = deliveryFeeCalculator.calculateFee(
                        clientLatitude,
                        clientLongitude,
                        restaurant.getLatitude(),
                        restaurant.getLongitude())
                .map(BigDecimal::valueOf)
                .map(amount -> amount.setScale(2, RoundingMode.HALF_UP))
                .orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

        order.setDeliveryFee(deliveryFee);
        order.setTotal(pricing.itemsTotal().add(deliveryFee).setScale(2, RoundingMode.HALF_UP));

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
                .map(orderNotificationMapper::toClientDto)
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

        for (OrderItem item : Optional.ofNullable(order.getItems()).orElse(Collections.emptyList())) {
            OrderItemPricing pricing = resolveItemPricing(item);

            List<CreateOrderResponse.Extra> extras = Optional.ofNullable(item.getMenuItemExtras()).orElse(Collections.emptyList())
                    .stream()
                    .map(extra -> new CreateOrderResponse.Extra(
                            extra.getId(),
                            extra.getName(),
                            BigDecimal.valueOf(extra.getPrice())
                    ))
                    .collect(Collectors.toList());

            orderedItems.add(new CreateOrderResponse.OrderedItem(
                    item.getMenuItem().getId(),
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    pricing.unitBasePrice(),
                    pricing.unitPrice(),
                    pricing.unitExtrasPrice(),
                    pricing.lineSubtotal(),
                    pricing.promotionDiscount(),
                    pricing.lineItemsTotal(),
                    pricing.extrasTotal(),
                    pricing.lineTotal(),
                    extras,
                    item.getSpecialInstructions()
            ));
        }

        OrderPricingBreakdown pricing = OrderPricingCalculator.calculatePricing(order);
        BigDecimal itemsSubtotal = Optional.ofNullable(order.getItemsSubtotal()).orElse(pricing.itemsSubtotal())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal extrasTotal = Optional.ofNullable(order.getExtrasTotal()).orElse(pricing.extrasTotal())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal promotionDiscount = Optional.ofNullable(order.getPromotionDiscount()).orElse(pricing.promotionDiscount())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal itemsTotal = Optional.ofNullable(order.getItemsTotal()).orElse(pricing.itemsTotal())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal deliveryFee = Optional.ofNullable(order.getDeliveryFee())
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal orderTotal = Optional.ofNullable(order.getTotal())
                .orElse(itemsTotal.add(deliveryFee))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal promotionalSubtotal = itemsSubtotal.subtract(promotionDiscount);
        if (promotionalSubtotal.compareTo(BigDecimal.ZERO) < 0) {
            promotionalSubtotal = BigDecimal.ZERO;
        }
        promotionalSubtotal = promotionalSubtotal.setScale(2, RoundingMode.HALF_UP);

        CreateOrderResponse.RestaurantSummary restaurantSummary = new CreateOrderResponse.RestaurantSummary(
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getRestaurant().getImageUrl(),
                order.getRestaurant().getIconUrl()
        );

        SavedAddressSummaryDto savedAddress = SavedAddressSummaryMapper.from(order.getSavedAddress());
        OrderNotificationDTO.DeliverySummary deliverySummary = new OrderNotificationDTO.DeliverySummary(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                order.getDeliveryAddress(),
                new LocationDto(order.getLat(), order.getLng()),
                savedAddress
        );

        CreateOrderResponse.PaymentSummary paymentSummary = new CreateOrderResponse.PaymentSummary(
                order.getPaymentMethod(),
                promotionalSubtotal,
                extrasTotal,
                orderTotal,
                itemsSubtotal,
                promotionDiscount,
                itemsTotal,
                deliveryFee
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

    private OrderItemPricing resolveItemPricing(OrderItem item) {
        if (item == null) {
            return OrderItemPricing.empty();
        }

        BigDecimal unitBasePrice = Optional.ofNullable(item.getUnitBasePrice())
                .orElse(null);
        BigDecimal unitPrice = Optional.ofNullable(item.getUnitPrice())
                .orElse(null);
        BigDecimal unitExtrasPrice = Optional.ofNullable(item.getUnitExtrasPrice())
                .orElse(null);
        BigDecimal lineSubtotal = Optional.ofNullable(item.getLineSubtotal())
                .orElse(null);
        BigDecimal lineItemsTotal = Optional.ofNullable(item.getLineItemsTotal())
                .orElse(null);
        BigDecimal extrasTotal = Optional.ofNullable(item.getExtrasTotal())
                .orElse(null);
        BigDecimal promotionDiscount = Optional.ofNullable(item.getPromotionDiscount())
                .orElse(null);
        BigDecimal lineTotal = Optional.ofNullable(item.getLineTotal())
                .orElse(null);

        if (unitBasePrice != null && unitPrice != null && unitExtrasPrice != null
                && lineSubtotal != null && lineItemsTotal != null && extrasTotal != null
                && promotionDiscount != null && lineTotal != null) {
            return new OrderItemPricing(
                    unitBasePrice.setScale(2, RoundingMode.HALF_UP),
                    unitPrice.setScale(2, RoundingMode.HALF_UP),
                    unitExtrasPrice.setScale(2, RoundingMode.HALF_UP),
                    lineSubtotal.setScale(2, RoundingMode.HALF_UP),
                    lineItemsTotal.setScale(2, RoundingMode.HALF_UP),
                    extrasTotal.setScale(2, RoundingMode.HALF_UP),
                    promotionDiscount.setScale(2, RoundingMode.HALF_UP),
                    lineTotal.setScale(2, RoundingMode.HALF_UP)
            );
        }

        return OrderPricingCalculator.calculateItemPricing(item);
    }

    private SavedAddress resolveSavedAddress(Long clientId, UUID savedAddressId) {
        if (savedAddressId == null) {
            return null;
        }

        return savedAddressRepository.findByIdAndUserId(savedAddressId, clientId)
                .orElseThrow(() -> new EntityNotFoundException("Saved address not found for client"));
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
