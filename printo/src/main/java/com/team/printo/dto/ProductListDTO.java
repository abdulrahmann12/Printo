package com.team.printo.dto;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductListDTO {

	
	private Long id;
	
    private String name;

    private BigDecimal price;

    private String description;

    private String image;

    private int quantity;

    private boolean active;

    private Long categoryId;
    
    public ProductListDTO(Long id, String name, String description, BigDecimal price, int quantity, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }
    
	
}