package com.team.printo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.OrderDTO;
import com.team.printo.dto.OrderItemDTO;
import com.team.printo.model.Design;
import com.team.printo.model.Order;
import com.team.printo.model.OrderItem;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AddressMapper.class, OrderItemAttributeValueMapper.class})
public interface OrderMapper {

	@Mapping(target = "userDTO", source = "user")
	@Mapping(target = "addressDTO", source = "address")
	@Mapping(target = "orderItems", source = "items")
    OrderDTO toDTO(Order order);
	
	
    List<OrderDTO> toDTOs(List<Order> orders);     
    
    @Mapping(target = "productDTO.categoryId", source = "product.category.id")
    @Mapping(target = "productDTO", source = "product")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "design", source = "design.image")
    OrderItemDTO toDTO(OrderItem orderItem);

    @Mapping(target = "product", source = "productDTO")
    @Mapping(target = "order.id", source = "orderId")
    @Mapping(target = "design.image", source = "design")
    OrderItem toEntity(OrderItemDTO orderItemDTO);
    
    
    List<OrderItemDTO> toOrderItemsDTO(List<OrderItem> ordersItems);
      
    List<OrderItem> toOrderItemEntity(List<OrderItemDTO> orderItemsDTO);
      
}