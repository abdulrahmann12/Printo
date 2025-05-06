package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.AttributeDTO;
import com.team.printo.model.Attribute;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
	
	@Mapping(target = "categoryId", source = "category.id")
    AttributeDTO toDto(Attribute attribute);
    
	
	@Mapping(target = "category.id", source = "categoryId")
	@Mapping(target = "values",ignore = true)
    Attribute toEntity(AttributeDTO attributeDTO);
}
