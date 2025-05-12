package com.team.printo.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ProductResponseDTO {

	private Long id;
	
    private String name;

    private BigDecimal price;

    private String description;

    private String image;

    private int quantity;

    private boolean active;

    private String categoryName;

    private List<ProductImageDTO> images = new ArrayList<>();

    private Map<String, List<AttributeValueResponseDTO>> attributeValues = new HashMap<>(); 
    
}
