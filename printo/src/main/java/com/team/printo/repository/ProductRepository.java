package com.team.printo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team.printo.dto.FavoriteResponse;
import com.team.printo.dto.ProductListDTO;
import com.team.printo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{


	@Query("SELECT new com.team.printo.dto.ProductListDTO(p.id, p.name, p.price, p.description, p.image, p.quantity, p.active, p.category.id) FROM Product p WHERE p.category.id = :categoryId AND p.active = true")
	List<ProductListDTO> findAllByCategoryId(Long categoryId);

	
	@Query("SELECT new com.team.printo.dto.ProductListDTO(p.id, p.name, p.price, p.description, p.image, p.quantity, p.active, p.category.id) FROM Product p WHERE p.active = true")
	List<ProductListDTO> findAllWithoutReviews();
	
	@Query("SELECT new com.team.printo.dto.ProductListDTO(p.id, p.name, p.price, p.description, p.image, p.quantity, p.active, p.category.id) FROM Product p WHERE p.active = true ORDER BY p.salesCount DESC")
	List<ProductListDTO> findByOrderBySalesCountDesc(Pageable pageable);
	
	@Query("SELECT new com.team.printo.dto.ProductListDTO(p.id, p.name, p.price, p.description, p.image, p.quantity, p.active, p.category.id) FROM Product p WHERE LOWER(p.name) LIKE LOWER(concat('%', :keyword, '%'))")
	List<ProductListDTO> fastSearch(@Param("keyword") String keyword);

	@Query("SELECT new com.team.printo.dto.ProductListDTO(p.id, p.name, p.price, p.description, p.image, p.quantity, p.active, p.category.id) FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
	List<ProductListDTO> fastSearchByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);
	
	@Query("SELECT new com.team.printo.dto.FavoriteResponse(p.id, p.name, p.price, p.image, p.description, c.name) " +
		       "FROM Product p LEFT JOIN p.category c " +
		       "WHERE p IN :favorites")
		List<FavoriteResponse> findFavoriteProductsDetails(@Param("favorites") Set<Product> favorites);
}
