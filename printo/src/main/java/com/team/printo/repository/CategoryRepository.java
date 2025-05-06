package com.team.printo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team.printo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	Optional<Category> findByName(String name);
    Optional<Category> findFirstByName(String name);
    List<Category> findAllByName(String name);
    List<Category> findByParentId(Long parentId);
    List<Category> findByParentIsNull();

}
