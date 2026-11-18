package com.microcomerce.order_service.controller;

import com.microcomerce.order_service.dto.OrderRequest;
import com.microcomerce.order_service.dto.OrderResponse;
import com.microcomerce.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public OrderResponse create(@RequestBody @Valid OrderRequest request) {
        return service.create(request);
    }
}