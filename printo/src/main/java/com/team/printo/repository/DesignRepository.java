package com.team.printo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team.printo.model.Design;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long>{

    List<Design> findByUserId(Long userId);


}
