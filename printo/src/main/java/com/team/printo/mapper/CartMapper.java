package com.team.printo.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.CartDTO;
import com.team.printo.dto.CartItemDTO;
import com.team.printo.model.Cart;
import com.team.printo.model.CartItem;

	@Mapper(componentModel = "spring")
public interface CartMapper {

	@Mapping(target = "userId", source = "user.id")
	CartDTO toDTO(Cart cart);
	
	@Mapping(target =  "user.id", source = "userId")
	Cart toEntity(CartDTO cartDTO);
	
	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "designId", source = "design.id")
	@Mapping(target = "cartId", source = "cart.id")
	CartItemDTO toDTO(CartItem cartItem);
	
	@Mapping(target = "product.id", source = "productId")
	@Mapping(target = "design.id", source = "designId")
	@Mapping(target = "cart.id", source = "cartId")
	CartItem toEntity(CartItemDTO cartItemDTO);
	
    @IterableMapping(elementTargetType = CartItemDTO.class)
    List<CartItemDTO> toCartItemDTOList(List<CartItem> cartItems);
}
