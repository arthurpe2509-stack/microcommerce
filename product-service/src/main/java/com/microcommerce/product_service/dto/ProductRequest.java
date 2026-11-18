package com.microcommerce.product_service.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductRequest {
    @NotBlank
    public String name;
    @NotBlank
    public String category;
    @NotBlank
    public String description;

}
