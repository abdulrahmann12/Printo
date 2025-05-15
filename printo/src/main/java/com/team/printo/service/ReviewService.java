package com.team.printo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.team.printo.dto.ReviewDTO;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.ReviewMapper;
import com.team.printo.model.Product;
import com.team.printo.model.Review;
import com.team.printo.model.User;
import com.team.printo.repository.ProductRepository;
import com.team.printo.repository.ReviewRepository;
import com.team.printo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewMapper reviewMapper;
	
	
	public ReviewDTO addReview(Long userId, Long productId, ReviewDTO reviewDTO) {
		
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));  
        
		User user = userRepository.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User not found ."));
		
		Review newReview = reviewMapper.toEntity(reviewDTO);
		
		newReview.setProduct(product);
		newReview.setUser(user);
		
		Review savedReview = reviewRepository.save(newReview);
		
		return reviewMapper.toDTO(savedReview);
	}
	
	public List<ReviewDTO> getAllReviewsByProductId(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));  
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        return reviews.stream()
        		.map(reviewMapper::toDTO)
        		.collect(Collectors.toList());
	}
	
	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found!")); 
		reviewRepository.delete(review);
	}
}
