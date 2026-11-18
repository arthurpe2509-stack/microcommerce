package com.microcomerce.inventory_service.service;


import com.microcomerce.inventory_service.dto.InventoryCreateRequest;
import com.microcomerce.inventory_service.dto.InventoryResponse;
import com.microcomerce.inventory_service.dto.InventoryUpdateRequest;
import com.microcomerce.inventory_service.dto.ProductResponse;
import com.microcomerce.inventory_service.entity.Inventory;
import com.microcomerce.inventory_service.repository.InventoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InventoryService {
    private final InventoryRepository repository;
    private final WebClient webClient;

    public InventoryService(InventoryRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public void create(InventoryCreateRequest request) {
        Inventory inventory = repository.findByProductId(request.productId())
                .orElseGet(Inventory::new);

        inventory.setProductId(request.productId());
        inventory.setQuantity(request.quantity());

        repository.save(inventory);
    }

    public InventoryResponse update(Long productId, InventoryUpdateRequest request) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock introuvable"));

        inventory.setQuantity(request.quantity());
        repository.save(inventory);

        ProductResponse product = webClient.get()
                .uri("http://localhost:8900/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();

        return new InventoryResponse(productId, product.name(), product.category(), inventory.getQuantity());
    }

    public InventoryResponse getByProductId(Long productId) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock introuvable"));

        ProductResponse product = webClient.get()
                .uri("http://localhost:8900/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();

        return new InventoryResponse(productId, product.name(), product.category(), inventory.getQuantity());
    }
}