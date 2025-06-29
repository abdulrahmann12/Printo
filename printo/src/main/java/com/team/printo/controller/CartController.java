package com.team.printo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.CartDTO;
import com.team.printo.dto.CartItemRequestDTO;
import com.team.printo.dto.Messages;
import com.team.printo.model.User;
import com.team.printo.service.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cart Controller", description = "API for managing the user's shopping cart.")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> addToCart(@AuthenticationPrincipal UserDetails userDetails,
											 @Valid @RequestBody CartItemRequestDTO cartItemDTO ){
	
		Long userId = ((User) userDetails).getId();	
		cartService.addToCart(userId, cartItemDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_TO_CART));
	}
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails){
		Long userId = ((User) userDetails).getId();	
		
		return ResponseEntity.ok(cartService.getCart(userId));
	}
	
	
	@DeleteMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> clearCart(@AuthenticationPrincipal UserDetails userDetails){
		Long userId = ((User) userDetails).getId();	
		cartService.clearCart(userId);
 		return ResponseEntity.ok(new BasicResponse(Messages.CLEAR_CART));
	}
	
	@DeleteMapping("/{cartitemId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> deleteCartItem(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long cartitemId
			){
		Long userId = ((User) userDetails).getId();	
		cartService.deleteCartItem(userId, cartitemId);
 		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_CART_ITEM));
	}
}
