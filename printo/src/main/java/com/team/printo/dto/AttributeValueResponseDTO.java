package com.team.printo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValueResponseDTO {

	private Long id;
	
	private Long attributeId;
	
	private String attributeName;
	
	private String value;
	
	private Boolean available; 
}
