package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.ProductTemplateDTO;
import com.team.printo.model.ProductTemplate;

@Mapper(componentModel = "spring")
public interface ProductTemplateMapper {
	
	@Mapping(target = "productId", source = "product.id")
    ProductTemplateDTO toDto(ProductTemplate productTemplate);
    
	@Mapping(target = "product.id", source = "productId")
    ProductTemplate toEntity(ProductTemplateDTO productTemplateDTO);
}
