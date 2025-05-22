package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.OrderItemAttributeValueResponseDTO;
import com.team.printo.model.OrderItemAttributeValue;

@Mapper(componentModel = "spring")
public interface OrderItemAttributeValueMapper {

    @Mapping(source = "orderItem.id", target = "orderItemId")
    @Mapping(source = "attributeValue.id", target = "attributeValueId")
    @Mapping(source =  "attributeValue.value", target = "attributeValue")
    @Mapping(source =  "attributeValue.attribute.name", target = "attributeName")
    OrderItemAttributeValueResponseDTO toResponseDTO(OrderItemAttributeValue entity);

    @Mapping(source = "orderItemId", target = "orderItem.id")
    @Mapping(source = "attributeValueId", target = "attributeValue.id")
    OrderItemAttributeValue toResponseEntity(OrderItemAttributeValueResponseDTO dto);
    
}