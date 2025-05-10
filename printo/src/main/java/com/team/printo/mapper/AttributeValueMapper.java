package com.team.printo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.AttributeValueDTO;
import com.team.printo.model.AttributeValue;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {

	@Mapping(target = "attributeId", source = "attribute.id")
	@Mapping(target = "productId", source = "product.id")
    AttributeValueDTO toDTO(AttributeValue attributeValue);
    
	@Mapping(target = "attribute", ignore = true)
	@Mapping(target = "product", ignore = true)
    AttributeValue toEntity(AttributeValueDTO attributeValueDTO);
	
	List<AttributeValueDTO> toDTOS(List<AttributeValue> attributeValues);

	
	List<AttributeValue> toEntities(List<AttributeValueDTO> attributeValues);

}
