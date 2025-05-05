package com.team.printo.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDTO {
    @NotNull(message = "Cart ID is required")
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    private List<CartItemDTO> items;
}