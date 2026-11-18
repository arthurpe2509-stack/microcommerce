package com.microcomerce.order_service.service;


import com.microcomerce.order_service.dto.InventoryResponse;
import com.microcomerce.order_service.dto.InventoryUpdateRequest;
import com.microcomerce.order_service.dto.OrderRequest;
import com.microcomerce.order_service.dto.OrderResponse;
import com.microcomerce.order_service.entity.CustomerOrder;
import com.microcomerce.order_service.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final WebClient webClient;

    public OrderService(OrderRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public OrderResponse create(OrderRequest request) {
        InventoryResponse inventory = webClient.get()
                .uri("http://localhost:8901/inventory/{productId}", request.productId())
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .block();

        if (inventory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable");
        }

        if (request.quantity() > inventory.quantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuffisant");
        }

        CustomerOrder order = new CustomerOrder();
        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setCreatedAt(LocalDateTime.now());

        CustomerOrder saved = repository.save(order);

        int remaining = inventory.quantity() - request.quantity();

        webClient.put()
                .uri("http://localhost:8901/inventory/{productId}", request.productId())
                .bodyValue(new InventoryUpdateRequest(remaining))
                .retrieve()
                .toBodilessEntity()
                .block();

        return new OrderResponse(saved.getId(), saved.getProductId(), saved.getQuantity(), saved.getCreatedAt());
    }
}