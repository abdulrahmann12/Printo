package com.team.printo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.team.printo.model.ProductTemplate;

@Repository
public interface ProductTemplateRepository extends JpaRepository<ProductTemplate, Long>{

    List<ProductTemplate> findByProductId(Long productId);

}
