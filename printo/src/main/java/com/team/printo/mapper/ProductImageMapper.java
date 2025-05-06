package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.ProductImageDTO;
import com.team.printo.model.ProductImage;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

	@Mapping(target = "productId", source = "product.id")
    ProductImageDTO toDto(ProductImage productImage);
    
	@Mapping(target = "product.id", source = "productId")
    ProductImage toEntity(ProductImageDTO productImageDTO);
}
