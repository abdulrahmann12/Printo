package com.team.printo.service;


import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.team.printo.dto.CartDTO;
import com.team.printo.dto.CartItemAttributeValueRequestDTO;
import com.team.printo.dto.CartItemRequestDTO;
import com.team.printo.exception.InsufficientStockException;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.CartMapper;
import com.team.printo.model.AttributeValue;
import com.team.printo.model.Cart;
import com.team.printo.model.CartItem;
import com.team.printo.model.CartItemAttributeValue;
import com.team.printo.model.Design;
import com.team.printo.model.Product;
import com.team.printo.model.User;
import com.team.printo.repository.AttributeValueRepository;
import com.team.printo.repository.CartItemAttributeValueRepository;
import com.team.printo.repository.CartItemRepository;
import com.team.printo.repository.CartRepository;
import com.team.printo.repository.DesignRepository;
import com.team.printo.repository.ProductRepository;
import com.team.printo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CartMapper cartMapper;
	private final DesignRepository designRepository;
    private final CartItemRepository cartItemRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final CartItemAttributeValueRepository cartItemAttributeValueRepository;

    
    public void addToCart(Long userId, CartItemRequestDTO cartItemDTO) {
        // التحقق من القيم الأساسية
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (cartItemDTO.getProductId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (product.getQuantity() < cartItemDTO.getQuantity()) {
            throw new InsufficientStockException("Not enough available stock.");
        }

        // احصل على سلة التسوق أو أنشئ جديدة واحفظها أولاً
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user, new ArrayList<>());
                    return cartRepository.save(newCart); // حفظ السلة أولاً
                });

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst();

        if (existingCartItem.isPresent()) {
            existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem.get());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart); // السلة محفوظة الآن
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDTO.getQuantity());

            if (cartItemDTO.getDesignId() != null) {
                Design design = designRepository.findById(cartItemDTO.getDesignId())
                        .orElseThrow(() -> new ResourceNotFoundException("Design not found!"));
                cartItem.setDesign(design);
            }

            CartItem savedCartItem = cartItemRepository.save(cartItem); // حفظ العنصر

            if (cartItemDTO.getAttributeValuesId() != null && !cartItemDTO.getAttributeValuesId().isEmpty()) {
                for (CartItemAttributeValueRequestDTO attrDto : cartItemDTO.getAttributeValuesId()) {
                    if (attrDto.getAttributeValueId() == null) {
                        throw new IllegalArgumentException("Attribute value ID must not be null");
                    }

                    AttributeValue attrValue = attributeValueRepository.findById(attrDto.getAttributeValueId())
                            .orElseThrow(() -> new ResourceNotFoundException("AttributeValue not found with id: " + attrDto.getAttributeValueId()));

                    CartItemAttributeValue cartItemAttrVal = new CartItemAttributeValue();
                    cartItemAttrVal.setCartItem(savedCartItem);
                    cartItemAttrVal.setAttributeValue(attrValue);

                    cartItemAttributeValueRepository.save(cartItemAttrVal);
                }
            }

            cart.getItems().add(savedCartItem);
        }

       cartRepository.save(cart);
    }
    
	public CartDTO getCart(Long userId) {
		
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(()-> new ResourceNotFoundException("Cart not Found "));  
		
		return cartMapper.toDTO(cart);
	}
	
	public void clearCart(Long userId) {
		
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(()-> new ResourceNotFoundException("Cart not Found "));    
		
		cart.getItems().clear();
		cartRepository.save(cart);
	}

}
