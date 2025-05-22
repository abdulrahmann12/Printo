package com.team.printo.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDTO {

	private long id;
	
	private ProductListDTO productDTO;
	
	private long orderId;
	
	private String design;
	
	@Positive
	private int quantity;
	
	@Positive
	private BigDecimal price;
	
    private List<OrderItemAttributeValueResponseDTO> attributeValues;

}
