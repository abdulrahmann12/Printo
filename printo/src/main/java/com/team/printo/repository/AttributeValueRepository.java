package com.team.printo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team.printo.model.AttributeValue;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long>{

    List<AttributeValue> findByProductId(Long productId);
    List<AttributeValue> findByAttributeId(Long attributeId);
}
