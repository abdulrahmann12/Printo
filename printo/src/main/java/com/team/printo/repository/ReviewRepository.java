package com.team.printo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.team.printo.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

    List<Review> findByProductId(Long productId);
    List<Review> findByUserId(Long userId);

}
