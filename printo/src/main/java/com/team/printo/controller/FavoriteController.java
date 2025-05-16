package com.team.printo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.FavoriteResponse;
import com.team.printo.model.User;
import com.team.printo.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @PostMapping("/add/{productId}")
    public ResponseEntity<BasicResponse> addToFavorites(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user) {
        
        favoriteService.addToFavorites(user.getId(), productId);
        return ResponseEntity.ok(new BasicResponse("Product added successfuly"));
    }
    
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<BasicResponse> removeFromFavorites(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user) {
        
        favoriteService.removeFromFavorites(user.getId(), productId);
        return ResponseEntity.ok(new BasicResponse("Product removed successfuly"));
    }
    
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getUserFavorites(
            @AuthenticationPrincipal User user) {
        
        List<FavoriteResponse> favorites = favoriteService.getUserFavorites(user.getId());
        return ResponseEntity.ok(favorites);
    }
    

}