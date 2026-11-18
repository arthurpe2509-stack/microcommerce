package com.microcomerce.inventory_service.dto;

public record InventoryResponse(Long productId, String name, String category, Integer quantity) {
    }

