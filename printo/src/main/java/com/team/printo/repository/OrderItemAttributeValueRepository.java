package com.team.printo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team.printo.model.OrderItemAttributeValue;

public interface OrderItemAttributeValueRepository extends JpaRepository<OrderItemAttributeValue, Long> {
    List<OrderItemAttributeValue> findByOrderItemId(Long orderItemId);
    
}