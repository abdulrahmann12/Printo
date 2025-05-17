package com.team.printo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.team.printo.model.CartItemAttributeValue;

public interface CartItemAttributeValueRepository extends JpaRepository<CartItemAttributeValue, Long> {
    List<CartItemAttributeValue> findByCartItemId(Long cartItemId);
    
}