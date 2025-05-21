package com.team.printo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.Messages;
import com.team.printo.dto.ReviewDTO;
import com.team.printo.model.User;
import com.team.printo.service.ReviewService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
	
	private final ReviewService reviewService;
	
	@PostMapping("/product/{productId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> addReview(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long productId,
			@Valid @RequestBody ReviewDTO reviewDTO
			){
		Long userId = ((User) userDetails).getId();
		ReviewDTO newReview = reviewService.addReview(userId, productId, reviewDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_REVIEW,newReview));
	}
	
	@GetMapping("/product/{productId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Long productId){
		List<ReviewDTO> reviews= reviewService.getAllReviewsByProductId(productId);
		return ResponseEntity.ok(reviews);
	}
	
	
	@DeleteMapping("/{reviewId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deleteReview(@PathVariable Long reviewId){
		reviewService.deleteReview(reviewId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_REVIEW));
	}

}
