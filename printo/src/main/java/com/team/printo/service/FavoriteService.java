package com.team.printo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.team.printo.dto.FavoriteResponse;
import com.team.printo.exception.InsufficientStockException;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.model.Product;
import com.team.printo.model.User;
import com.team.printo.repository.ProductRepository;
import com.team.printo.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public void addToFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        if (user.getFavoriteProducts().contains(product)) {
            throw new InsufficientStockException("Product already in favorites");
        }
        
        user.getFavoriteProducts().add(product);
        userRepository.save(user);
    }
    
    public void removeFromFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        if (!user.getFavoriteProducts().contains(product)) {
            throw new ResourceNotFoundException("Product not in favorites");
        }
        
        user.getFavoriteProducts().remove(product);
        userRepository.save(user);
    }
    
    public List<FavoriteResponse> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return productRepository.findFavoriteProductsDetails(user.getFavoriteProducts());
    }
}
