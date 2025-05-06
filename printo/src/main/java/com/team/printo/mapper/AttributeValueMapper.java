package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.AttributeValueDTO;
import com.team.printo.model.AttributeValue;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {

	@Mapping(target = "attributeId", source = "attribute.id")
	@Mapping(target = "productId", source = "product.id")
    AttributeValueDTO toDto(AttributeValue attributeValue);
    
	@Mapping(target = "attribute.id", source = "attributeId")
	@Mapping(target = "product.id", source = "productId")
    AttributeValue toEntity(AttributeValueDTO attributeValueDTO);
}
