package com.team.printo.dto;

import java.math.BigDecimal;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavoriteResponse {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String image;
    private String description;
    private String categoryName;
    
    public FavoriteResponse(
        Long productId, 
        String productName, 
        BigDecimal price, 
        String image, 
        String description, 
        String categoryName
    ) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.image = image;
        this.description = description;
        this.categoryName = categoryName;
    }
    
}