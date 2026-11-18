package com.microcommerce.product_service.dto;

public record InventoryCreateRequest (Long productId, Integer quantity) {
}
