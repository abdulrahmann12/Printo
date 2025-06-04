package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.DesignDTO;
import com.team.printo.model.Design;

@Mapper(componentModel = "spring")
public interface DesignMapper {
	
	
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "productId", source = "product.id")
    DesignDTO toDto(Design design);
    
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "product.id", source = "productId")
	@Mapping(target = "cartItems", ignore = true)
    Design toEntity(DesignDTO designDTO);
}
