package com.team.printo.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequestDTO {

	
    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Product description is required")
    private String description;

    @PositiveOrZero(message = "Product quantity must be zero or positive")
    private int quantity;

    private boolean active;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private List<@Valid AttributeValueDTO> attributeValues = new ArrayList<>();
    
}
