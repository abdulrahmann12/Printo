package com.team.printo.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.team.printo.dto.CartDTO;
import com.team.printo.dto.CartItemAttributeValueRequestDTO;
import com.team.printo.dto.CartItemRequestDTO;
import com.team.printo.dto.Messages;
import com.team.printo.exception.AttributeNotFoundException;
import com.team.printo.exception.AttributeValueNotFoundException;
import com.team.printo.exception.CartNotFoundException;
import com.team.printo.exception.DesignNotFoundException;
import com.team.printo.exception.InsufficientStockException;
import com.team.printo.exception.ProductNotFoundException;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.exception.UserNotFoundException;
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
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        if (product.getQuantity() < cartItemDTO.getQuantity()) {
            throw new InsufficientStockException(Messages.NOT_ENOUGH_STOCK);
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user, new ArrayList<>());
                    return cartRepository.save(newCart); 
                });

        Optional<CartItem> existingCartItem = cart.getItems().stream()
        	    .filter(item -> {
        	        boolean sameProduct = item.getProduct().getId() == product.getId();
        	        boolean bothDesignNull = (item.getDesign() == null && cartItemDTO.getDesignId() == null);
        	        boolean sameDesign = (item.getDesign() != null && cartItemDTO.getDesignId() != null && item.getDesign().getId() == cartItemDTO.getDesignId());
        	        boolean sameDesignCondition = bothDesignNull || sameDesign;
        	        boolean sameAttributes = areAttributeValuesEqual(item.getAttributeValues(), cartItemDTO.getAttributeValuesId());
        	        return sameProduct && sameDesignCondition && sameAttributes;
        	    })
        	    .findFirst();

        if (existingCartItem.isPresent()) {
            existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem.get());
        } else {
        	CartItem cartItem = new CartItem();
        	cartItem.setCart(cart); 
        	cartItem.setProduct(product);
        	cartItem.setQuantity(cartItemDTO.getQuantity());

        	if (cartItemDTO.getDesignId() != null) {
        	    Design design = designRepository.findById(cartItemDTO.getDesignId())
        	            .orElseThrow(() -> new DesignNotFoundException());
        	    if (design.getUser().getId() != userId) {
        	        throw new IllegalArgumentException(Messages.USER_NOT_OWN_DESIGN);
        	    }
        	    if (!design.getProduct().getId().equals(product.getId())) {
        	        throw new IllegalArgumentException(Messages.PRODUCT_NOT_OWN_DESIGN);
        	    }
        	    cartItem.setDesign(design);
        	}

        	List<CartItemAttributeValue> attributeValueList = new ArrayList<>();

        	if (cartItemDTO.getAttributeValuesId() != null && !cartItemDTO.getAttributeValuesId().isEmpty()) {
        	    for (CartItemAttributeValueRequestDTO attrDto : cartItemDTO.getAttributeValuesId()) {
        	        if (attrDto.getAttributeValueId() == null) {
        	            throw new IllegalArgumentException(Messages.ATTRIBUTE_VALUE_NOT_NULL);
        	        }
        	        AttributeValue attrValue = attributeValueRepository.findById(attrDto.getAttributeValueId())
        	                .orElseThrow(() -> new AttributeValueNotFoundException());

        	        if (!attrValue.getProduct().getId().equals(product.getId())) {
        	            throw new IllegalArgumentException(Messages.PRODUCT_NOT_OWN_ATTRIBUTE);
        	        }
        	        CartItemAttributeValue cartItemAttrVal = new CartItemAttributeValue();
        	        cartItemAttrVal.setCartItem(cartItem);
        	        cartItemAttrVal.setAttributeValue(attrValue);

        	        attributeValueList.add(cartItemAttrVal);
        	    }
        	}
        	
        	CartItem savedCartItem = cartItemRepository.save(cartItem);

        	for (CartItemAttributeValue cartItemAttrVal : attributeValueList) {
        	    cartItemAttrVal.setCartItem(savedCartItem); 
        	    cartItemAttributeValueRepository.save(cartItemAttrVal);
        	}
        	cart.getItems().add(savedCartItem); 
        }
       cartRepository.save(cart);
    }
    
	public CartDTO getCart(Long userId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(()-> new CartNotFoundException());  	
		
	    List<CartItem> filteredItems = cart.getItems().stream()
	            .filter(item -> item.getProduct().isActive() && item.getProduct().getQuantity() > 0)
	            .toList();
	    cart.setItems(filteredItems);
		return cartMapper.toDTO(cart);
	}
	
	public void clearCart(Long userId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(()-> new CartNotFoundException());    
		cart.getItems().clear();
		cartRepository.save(cart);
	}

	private boolean areAttributeValuesEqual(List<CartItemAttributeValue> list1, List<CartItemAttributeValueRequestDTO> list2) {
	    if (list1.size() != list2.size()) return false;
	    Set<Long> ids1 = list1.stream()
	            .map(attr -> attr.getAttributeValue().getId())
	            .collect(Collectors.toSet());
	    Set<Long> ids2 = list2.stream()
	            .map(CartItemAttributeValueRequestDTO::getAttributeValueId)
	            .collect(Collectors.toSet());
	    return ids1.equals(ids2);
	}
		
	public void deleteCartItem(Long userId, Long cartItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
	    Cart cart = cartRepository.findByUserId(user.getId())
	            .orElseThrow(() -> new CartNotFoundException());
	    CartItem cartItem = cartItemRepository.findById(cartItemId)
	            .orElseThrow(() -> new ResourceNotFoundException(Messages.CART_ITEM_NOT_FOUND));
	    if (cartItem.getCart().getId() != cart.getId()) {
	        throw new IllegalArgumentException(Messages.CART_ITEM_NOT_IN_CART);
	    }
	    cartItemRepository.delete(cartItem);
	}
	
}
