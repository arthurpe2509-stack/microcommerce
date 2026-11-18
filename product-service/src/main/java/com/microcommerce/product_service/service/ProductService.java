package com.microcommerce.product_service.service;


import com.microcommerce.product_service.dto.InventoryCreateRequest;
import com.microcommerce.product_service.dto.ProductRequest;
import com.microcommerce.product_service.dto.ProductResponse;
import com.microcommerce.product_service.entity.Product;
import com.microcommerce.product_service.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final WebClient webClient;

    public ProductService(ProductRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name);
        product.setCategory(request.category);
        product.setDescription(request.description);

        Product saved = repository.save(product);

        webClient.post()
                .uri("http://localhost:8901/inventory")
                .bodyValue(new InventoryCreateRequest(saved.getId(), 0))
                .retrieve()
                .toBodilessEntity()
                .block();

        return new ProductResponse(saved.getId(), saved.getName(), saved.getCategory(), saved.getDescription());
    }

    public ProductResponse findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable"));

        return new ProductResponse(product.getId(), product.getName(), product.getCategory(), product.getDescription());
    }
}