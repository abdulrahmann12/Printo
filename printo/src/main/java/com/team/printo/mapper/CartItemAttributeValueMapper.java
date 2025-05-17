package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.CartItemAttributeValueDTO;
import com.team.printo.model.CartItemAttributeValue;

@Mapper(componentModel = "spring")
public interface CartItemAttributeValueMapper {

    @Mapping(source = "cartItem.id", target = "cartItemId")
    @Mapping(source = "attributeValue.id", target = "attributeValueId")
    @Mapping(source =  "attributeValue.value", target = "attributeValue")
    @Mapping(source =  "attributeValue.attribute.name", target = "attributeName")
    CartItemAttributeValueDTO toDTO(CartItemAttributeValue entity);

    @Mapping(source = "cartItemId", target = "cartItem.id")
    @Mapping(source = "attributeValueId", target = "attributeValue.id")
    CartItemAttributeValue toEntity(CartItemAttributeValueDTO dto);
    
}