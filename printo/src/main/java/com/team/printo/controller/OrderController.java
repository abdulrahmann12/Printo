package com.team.printo.controller;


import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.Messages;
import com.team.printo.dto.OrderDTO;
import com.team.printo.dto.OrderStatusDTO;
import com.team.printo.model.Order.OrderStatus;
import com.team.printo.model.User;
import com.team.printo.service.OrderService;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
	@PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> createOrder(
    		@AuthenticationPrincipal UserDetails userDetails, 
    		@RequestParam Long addressId) {
		Long userId = ((User) userDetails).getId();
        OrderDTO dto =  orderService.createOrder(userId, addressId);
        return ResponseEntity.ok(dto);
    }
    
	@PutMapping("/{orderId}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusDTO status){
		orderService.updateOrderStatus(orderId, status);
		return ResponseEntity.ok(new BasicResponse(Messages.CHANGE_ORDER_STATUS));
	}
	
	@GetMapping("/user")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<OrderDTO>> getUserOrders(@AuthenticationPrincipal UserDetails userDetails){
		Long userId = ((User) userDetails).getId();
		List<OrderDTO> orders = orderService.getUserOrders(userId);
		return ResponseEntity.ok(orders);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<OrderDTO>> getAllOrders(){
		List<OrderDTO> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}
	
	@GetMapping("/user/{orderId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<OrderDTO> getAllOrders(@PathVariable Long orderId,@AuthenticationPrincipal UserDetails userDetails){
		Long userId = ((User) userDetails).getId();
		OrderDTO orders = orderService.getOneOrder(orderId,userId);
		return ResponseEntity.ok(orders);
	}
	
    @GetMapping("/order-statuses")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderStatus> getAllOrderStatuses() {
        return orderService.getAllOrderStatuses();
    }
    
    
    
}
