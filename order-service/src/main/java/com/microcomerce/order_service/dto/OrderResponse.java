package com.microcomerce.order_service.dto;

import java.time.LocalDateTime;

public record OrderResponse (Long id, Long productId, Integer quantity, LocalDateTime createdAt) {
}
