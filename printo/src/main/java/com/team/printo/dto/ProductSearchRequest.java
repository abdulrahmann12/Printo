package com.team.printo.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductSearchRequest {

    private String name;         
    private String categoryName; 
    private BigDecimal minPrice; 
    private BigDecimal maxPrice;  
}
