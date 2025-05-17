package com.team.printo.service;

import org.springframework.stereotype.Service;

import com.team.printo.dto.CartItemResponseDTO;
import com.team.printo.mapper.CartMapper;
import com.team.printo.repository.AttributeValueRepository;
import com.team.printo.repository.CartItemAttributeValueRepository;
import com.team.printo.repository.CartItemRepository;
import com.team.printo.repository.CartRepository;
import com.team.printo.repository.ProductRepository;
import com.team.printo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private CartRepository cartRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private CartMapper cartMapper;
	
    private final CartItemRepository cartItemRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final CartItemAttributeValueRepository cartItemAttributeValueRepository;
    
    
    public CartItemResponseDTO addToCart(Long userId, CartItemResponseDTO cartItemDTO) {
    	return null;
    }
}
