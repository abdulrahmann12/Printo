package com.team.printo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.OrderDTO;
import com.team.printo.dto.OrderItemDTO;
import com.team.printo.model.Order;
import com.team.printo.model.OrderItem;



@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "orderItems", source = "items")
    OrderDTO toDTO(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "address.id", source = "addressId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDTO orderDTO);

    
    List<OrderDTO> toDTOs(List<Order> orders);
    
    
    List<Order> toEntities(List<OrderDTO> orderDTOS);
    
    
    
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "designId", source = "design.id")
    OrderItemDTO toDTO(OrderItem orderItem);

    
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "order.id", source = "orderId")
    @Mapping(target = "design.id", source = "designId")
    OrderItem toEntity(OrderItemDTO orderItemDTO);
    
    
    List<OrderItemDTO> toOrderItemsDTO(List<OrderItem> ordersItems);
    
    
    List<OrderItem> toOrderItemEntity(List<OrderItemDTO> orderItemsDTO);
}