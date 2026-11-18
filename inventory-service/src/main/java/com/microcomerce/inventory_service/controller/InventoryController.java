package com.microcomerce.inventory_service.controller;

import com.microcomerce.inventory_service.dto.InventoryCreateRequest;
import com.microcomerce.inventory_service.dto.InventoryResponse;
import com.microcomerce.inventory_service.dto.InventoryUpdateRequest;
import com.microcomerce.inventory_service.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create (@RequestBody InventoryCreateRequest request) {
        inventoryService.create(request);
    }

    @GetMapping("/{productId}")
    public InventoryResponse getByProductId(@PathVariable Long productId){
        return inventoryService.getByProductId(productId);
    }

    @PutMapping("/{productId}")
    public InventoryResponse update(@PathVariable Long productId, @RequestBody InventoryUpdateRequest request){
        return inventoryService.update(productId, request);
    }

}
