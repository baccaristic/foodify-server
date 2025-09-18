package com.foodify.server.service;

import com.foodify.server.models.Client;
import com.foodify.server.models.Order;
import com.foodify.server.repository.ClientRepository;
import com.foodify.server.repository.OrderRepository;
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
