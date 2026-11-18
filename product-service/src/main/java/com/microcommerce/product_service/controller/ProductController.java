package com.microcommerce.product_service.controller;


import com.microcommerce.product_service.dto.ProductRequest;
import com.microcommerce.product_service.dto.ProductResponse;
import com.microcommerce.product_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponse create(@RequestBody @Valid ProductRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }
}