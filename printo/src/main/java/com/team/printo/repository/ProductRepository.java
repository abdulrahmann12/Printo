package com.team.printo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team.printo.dto.ProductListDTO;
import com.team.printo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByCategoryId(Long categoryId);

	@Query("SELECT new com.team.printo.dto.ProductListDTO(p.id, p.name, p.price, p.description, p.image, p.quantity, p.active, p.salesCount, p.category.id) FROM Product p WHERE p.category.id = :categoryId")
	List<ProductListDTO> findAllByCategoryId(Long categoryId);

	
	@Query("SELECT new com.team.printo.dto.ProductListDTO(p.id, p.name, p.price, p.description, p.image, p.quantity, p.active, p.salesCount, p.category.id) FROM Product p")
	List<ProductListDTO> findAllWithoutComment();
}
