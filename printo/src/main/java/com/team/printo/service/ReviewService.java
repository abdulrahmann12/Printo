package com.team.printo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.team.printo.dto.Messages;
import com.team.printo.dto.ReviewDTO;
import com.team.printo.exception.ProductNotFoundException;
import com.team.printo.exception.ReviewNotFoundException;
import com.team.printo.exception.UserNotFoundException;
import com.team.printo.mapper.ReviewMapper;
import com.team.printo.model.Product;
import com.team.printo.model.Review;
import com.team.printo.model.User;
import com.team.printo.model.User.Role;
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
                .orElseThrow(() -> new ProductNotFoundException());  
        
		User user = userRepository.findById(userId)
				.orElseThrow(()-> new UserNotFoundException());
		
		Review newReview = reviewMapper.toEntity(reviewDTO);
		
		newReview.setProduct(product);
		newReview.setUser(user);
		newReview.setCreatedAt(LocalDateTime.now());
		Review savedReview = reviewRepository.save(newReview);
		
		return reviewMapper.toDTO(savedReview);
	}
	
	public List<ReviewDTO> getAllReviewsByProductId(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());  
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        return reviews.stream()
        		.map(reviewMapper::toDTO)
        		.collect(Collectors.toList());
	}
	
	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException()); 

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName();
	    User currentUser = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException());
	    
	    boolean isAdmin = currentUser.getRole() == Role.ADMIN;

	    
	    boolean isOwner = review.getUser().getId() == (currentUser.getId());

	    if (!isAdmin && !isOwner) {
	        throw new IllegalArgumentException(Messages.ACCESS_DENIED);
	    }
		reviewRepository.delete(review);
	}
}
