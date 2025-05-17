package com.team.printo.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.CartDTO;
import com.team.printo.dto.CartItemAttributeValueDTO;
import com.team.printo.dto.CartItemDTO;
import com.team.printo.model.Cart;
import com.team.printo.model.CartItem;
import com.team.printo.model.CartItemAttributeValue;

	@Mapper(componentModel = "spring")
public interface CartMapper {

	@Mapping(target = "userId", source = "user.id")
	CartDTO toDTO(Cart cart);
	
	@Mapping(target =  "user.id", source = "userId")
	Cart toEntity(CartDTO cartDTO);
	
	@Mapping(target = "productName", source = "product.name")
	@Mapping(target = "productImage", source = "product.image")
	@Mapping(target = "design", source = "design.image")
	@Mapping(target = "cartId", source = "cart.id")
	CartItemDTO toDTO(CartItem cartItem);
	
	@Mapping(target = "product.name", source = "productName")
	@Mapping(target = "product.image", source = "productImage")
	@Mapping(target = "design.image", source = "design")
	@Mapping(target = "cart.id", source = "cartId")
	@Mapping(target = "attributeValues", source = "attributeValues")
	CartItem toEntity(CartItemDTO cartItemDTO);
	
    @IterableMapping(elementTargetType = CartItemDTO.class)
    List<CartItemDTO> toCartItemDTOList(List<CartItem> cartItems);
    
    @Mapping(source = "cartItem.id", target = "cartItemId")
    @Mapping(source = "attributeValue.id", target = "attributeValueId")
    @Mapping(source =  "attributeValue.value", target = "attributeValue")
    @Mapping(source =  "attributeValue.attribute.name", target = "attributeName")
    CartItemAttributeValueDTO toDTO(CartItemAttributeValue entity);

    @Mapping(source = "cartItemId", target = "cartItem.id")
    @Mapping(source = "attributeValueId", target = "attributeValue.id")
    CartItemAttributeValue toEntity(CartItemAttributeValueDTO dto);
    
}
    

