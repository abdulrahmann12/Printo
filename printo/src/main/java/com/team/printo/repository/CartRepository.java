package com.team.printo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team.printo.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	List<Cart> findByUserId(Long id);

}
