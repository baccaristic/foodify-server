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
import com.foodify.server.modules.orders.dto.OrderWorkflowStepDto;
import com.foodify.server.modules.orders.dto.SavedAddressSummaryDto;
import com.foodify.server.modules.orders.dto.response.CreateOrderResponse;
import com.foodify.server.modules.orders.mapper.SavedAddressSummaryMapper;
import com.foodify.server.modules.orders.repository.OrderRepository;
import com.foodify.server.modules.restaurants.domain.MenuItem;
import com.foodify.server.modules.restaurants.domain.MenuItemExtra;
import com.foodify.server.modules.restaurants.domain.Restaurant;
import com.foodify.server.modules.restaurants.repository.MenuItemExtraRepository;
import com.foodify.server.modules.restaurants.repository.MenuItemRepository;
import com.foodify.server.modules.restaurants.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

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

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : Optional.ofNullable(request.getItems()).orElse(Collections.emptyList())) {
            if (itemRequest.getMenuItemId() == null) {
                throw new IllegalArgumentException("Menu item id is required");
            }
            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));

            if (!Objects.equals(menuItem.getRestaurant().getId(), restaurant.getId())) {
                throw new IllegalArgumentException("Menu item does not belong to the selected restaurant");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSpecialInstructions(itemRequest.getSpecialInstructions());

            List<Long> extraIds = Optional.ofNullable(itemRequest.getExtraIds()).orElse(Collections.emptyList());
            List<MenuItemExtra> extras = extraIds.isEmpty()
                    ? Collections.emptyList()
                    : menuItemExtraRepository.findAllById(extraIds);

            if (!extras.isEmpty()) {
                validateExtras(menuItem, extras);
            }

            orderItem.setMenuItemExtras(new ArrayList<>(extras));
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
            BigDecimal unitPrice = BigDecimal.valueOf(item.getMenuItem().getPrice());
            BigDecimal extrasPerUnit = Optional.ofNullable(item.getMenuItemExtras()).orElse(Collections.emptyList())
                    .stream()
                    .map(extra -> BigDecimal.valueOf(extra.getPrice()))
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
                            extra.getId(),
                            extra.getName(),
                            BigDecimal.valueOf(extra.getPrice())
                    ))
                    .collect(Collectors.toList());

            orderedItems.add(new CreateOrderResponse.OrderedItem(
                    item.getMenuItem().getId(),
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    unitPrice,
                    extrasPerUnit,
                    lineTotal,
                    extras,
                    item.getSpecialInstructions()
            ));
        }

        BigDecimal total = subtotal.add(extrasTotal);

        CreateOrderResponse.RestaurantSummary restaurantSummary = new CreateOrderResponse.RestaurantSummary(
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
