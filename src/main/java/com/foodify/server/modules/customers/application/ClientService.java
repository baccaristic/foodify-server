package com.foodify.server.modules.customers.application;

import com.foodify.server.modules.identity.domain.Client;
import com.foodify.server.modules.orders.domain.Order;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrderRepository  orderRepository;


    public List<Order> getMyOrders(Client client) {
        return this.orderRepository.findAllByClientOrderByDateDesc(client);
    }

    public Order getMyOrder(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }
}
