package com.team.printo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.team.printo.dto.Messages;
import com.team.printo.dto.PaymentCreateDTO;
import com.team.printo.dto.PaymentDTO;
import com.team.printo.dto.PaymentStatusUpdateDTO;
import com.team.printo.exception.OrderNotFoundException;
import com.team.printo.exception.PaymentNotFoundException;
import com.team.printo.mapper.PaymentMapper;
import com.team.printo.model.Order;
import com.team.printo.model.Payment;
import com.team.printo.repository.OrderRepository;
import com.team.printo.repository.PaymentRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    
    public PaymentDTO createPayment(Long userId, PaymentCreateDTO paymentCreateDTO) {
	    Order order = orderRepository.findById(paymentCreateDTO.getOrderId())
	            .orElseThrow(() -> new OrderNotFoundException());
    	
	    validateOrderOwnership(userId, order);
	    
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentCreateDTO.getPaymentMethod());
        payment.setAmount(paymentCreateDTO.getAmount());
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        
 
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toDTO(savedPayment);
        
    }
    
    public PaymentDTO updatePaymentStatus(PaymentStatusUpdateDTO paymentStatusUpdateDTO) {
        Payment payment = paymentRepository.findById(paymentStatusUpdateDTO.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException());

        if (payment.getPaymentStatus() == Payment.PaymentStatus.PAID) {
            throw new IllegalStateException(Messages.PAYMENT_STATUS_ALEADY_PAID);
        }
        
        payment.setPaymentStatus(paymentStatusUpdateDTO.getPaymentStatus());

        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toDTO(updatedPayment);
    }
    
    public PaymentDTO getPaymentById(Long userId ,Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException());
        
        validateOrderOwnership(userId, payment.getOrder());
        
        return paymentMapper.toDTO(payment);
    }
    
    private void validateOrderOwnership(Long userId, Order order) {
        if(order.getUser().getId() !=userId ) {
            throw new IllegalStateException(Messages.ORDER_NOT_BELONG_TO_USER);
        }
    }
    
}
