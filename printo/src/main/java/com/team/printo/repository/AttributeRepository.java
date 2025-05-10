package com.team.printo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team.printo.model.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long>{
    List<Attribute> findByCategoryId(Long categoryId);
    boolean existsByNameAndCategoryId(String name, Long categoryId);
    boolean existsByNameAndCategoryIdAndIdNot(String name, Long categoryId, Long id);

}
