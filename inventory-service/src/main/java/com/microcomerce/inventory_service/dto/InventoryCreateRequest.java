package com.microcomerce.inventory_service.dto;

public record InventoryCreateRequest(Long productId, Integer quantity) {
}
