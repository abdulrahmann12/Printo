package com.team.printo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import com.team.printo.dto.Messages;
import com.team.printo.dto.OrderDTO;
import com.team.printo.dto.OrderStatusDTO;
import com.team.printo.exception.AddressNotFoundException;
import com.team.printo.exception.CartNotFoundException;
import com.team.printo.exception.MailSendingException;
import com.team.printo.exception.ProductNotFoundException;
import com.team.printo.exception.UserNotFoundException;
import com.team.printo.mapper.OrderMapper;
import com.team.printo.model.Address;
import com.team.printo.model.Cart;
import com.team.printo.model.Order;
import com.team.printo.model.Order.OrderStatus;
import com.team.printo.model.OrderItem;
import com.team.printo.model.OrderItemAttributeValue;
import com.team.printo.model.Product;
import com.team.printo.model.User;
import com.team.printo.repository.AddressRepository;
import com.team.printo.repository.CartRepository;
import com.team.printo.repository.OrderItemRepository;
import com.team.printo.repository.OrderRepository;
import com.team.printo.repository.ProductRepository;
import com.team.printo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository; 
	private final AddressRepository addressRepository;
	private final OrderItemRepository orderItemRepository;
	private final OrderMapper orderMapper;
	private final CartRepository cartRepository;
	private final CartService cartService;
	private final EmailService emailService;
	
	@Transactional
	public OrderDTO createOrder(Long userId, Long addressId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException());
	    
	    Address address = addressRepository.findById(addressId)
	            .orElseThrow(() -> new AddressNotFoundException());

	    // CHeck confirmation email before place the order
	    if(!user.isEmailConfirmation()) {
	        throw new IllegalStateException(Messages.EMAIL_NOT_CONFIRMED);
	    }

	    // CHeck valid address owned by userId
	    if(address.getUser().getId() != user.getId()) {
	        throw new IllegalStateException(Messages.ADDRESS_NOT_BELONG_TO_USER);
	    }
	    
	    // Get user cart 
	    Cart cart = cartRepository.findByUserId(userId)
	            .orElseThrow(() -> new CartNotFoundException());

	    // Check user cart 
	    if(cart.getItems().isEmpty()) {
	        throw new IllegalStateException(Messages.EMPTY_CART);
	    }

	    // Create order
	    Order newOrder = new Order();
	    newOrder.setAddress(address);
	    newOrder.setUser(user);
	    newOrder.setCreatedAt(LocalDateTime.now());
	    newOrder.setPhoneNumber(user.getPhone());
	    newOrder.setStatus(OrderStatus.PROCESSING);

	    Order savedOrder = orderRepository.save(newOrder);
	   
	    // Create items
	    List<OrderItem> orderItems = createOrderItems(cart, savedOrder);
	    savedOrder.setItems(orderItems);

	    // Calculate total price
	    BigDecimal totalPrice = orderItems.stream()
	            .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
	    savedOrder.setTotalPrice(totalPrice);

	    // Saved order in database
	    Order finalOrder = orderRepository.save(savedOrder);
	    
	    // Clear cart
	    cartService.clearCart(userId);

	    // Send confirmation email to user  
	    try {
	        emailService.sendOrderConfirmationEmail(finalOrder, Messages.ORDER_CONFIRMATION);
	    } catch (MailException e) {
	    	throw new MailSendingException();
	    }

	    return orderMapper.toDTO(finalOrder);
	}
		
	private List<OrderItem> createOrderItems(Cart cart, Order order) {
	    return cart.getItems().stream().map(cartItem -> {
	    	// Check on product 
	        Product product = productRepository.findById(cartItem.getProduct().getId())
					.orElseThrow(()-> new ProductNotFoundException());
			
	        // Check on product quantity
	        if(product.getQuantity() < cartItem.getQuantity()) {
	            throw new IllegalStateException(Messages.NOT_ENOUGH_STOCK);
	        }
	        
	        // Update product quantity
	        product.setQuantity(product.getQuantity() - cartItem.getQuantity());
	        productRepository.save(product);

	        
	        // Calculate total price for one order item
	        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
	        
	        // Create OrderItem
	        OrderItem orderItem = new OrderItem(
	            order,
	            product,
	            cartItem.getQuantity(),
	            totalPrice,
	            cartItem.getDesign()
	        );

	        // Save order item in database
	        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
	        
	        
	        // Copy attributeValues from cart item to order item
	        if(cartItem.getAttributeValues() != null) {
	            List<OrderItemAttributeValue> orderAttrValues = cartItem.getAttributeValues()
	                .stream()
	                .map(cartAttr -> {
	                    OrderItemAttributeValue orderAttr = new OrderItemAttributeValue();
	                    orderAttr.setOrderItem(savedOrderItem);
	                    if(!cartAttr.getAttributeValue().getAvailable()) {
	                    	throw new IllegalStateException(Messages.ATTRIBUTE_VALUE_NOT_AVAILABLE);
	                    }
	                    orderAttr.setAttributeValue(cartAttr.getAttributeValue());
	                    return orderAttr;
	                })
	                .collect(Collectors.toList());
	            
	            // Save attribute value in order item
	            savedOrderItem.getAttributeValues().clear();
	            savedOrderItem.getAttributeValues().addAll(orderAttrValues);

	            // Save order item
	            orderItemRepository.save(savedOrderItem);
	            }

	        return savedOrderItem;
	    }).collect(Collectors.toList());
	}
	
	public List<OrderDTO> getAllOrders(){
		return orderMapper.toDTOs(orderRepository.findAll());
	}
	
	public List<OrderDTO> getUserOrders(Long userId){
		return orderMapper.toDTOs(orderRepository.findByUserId(userId));	
	}
	
	public OrderDTO getOneOrder(Long orderId, Long userId) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new IllegalStateException(Messages.ORDER_NOT_FOUND));
	    if(order.getUser().getId() != userId) {
	    	throw new IllegalStateException(Messages.ORDER_NOT_BELONG_TO_USER);
	    }
	    return orderMapper.toDTO(order);
	}
	
	@Transactional
	public void updateOrderStatus(Long orderId, OrderStatusDTO newStatus) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new IllegalStateException(Messages.ORDER_NOT_FOUND));

	    if(order.getStatus().equals(newStatus.getStatus())) {
	    	throw new IllegalStateException(Messages.SAME_ORDER_STATUS);
	    }
	    order.setStatus(newStatus.getStatus());
	    orderRepository.save(order);

	    try {
	        emailService.sendOrderStatusUpdateEmail(order, Messages.CHANGE_ORDER_STATUS);
	    } catch (MailException e) {
	    	throw new MailSendingException();
	    }
	}
	
	public List<OrderStatus> getAllOrderStatuses(){
		return Arrays.asList(OrderStatus.values());
	}
}
