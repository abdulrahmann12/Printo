package com.team.printo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.team.printo.dto.FavoriteResponse;
import com.team.printo.dto.Messages;
import com.team.printo.exception.InsufficientStockException;
import com.team.printo.exception.ProductNotFoundException;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.exception.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException());
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());
        
        if (user.getFavoriteProducts().contains(product)) {
            throw new InsufficientStockException(Messages.ALREADY_EXIST_IN_FAVORITE);
        }
        
        user.getFavoriteProducts().add(product);
        userRepository.save(user);
    }
    
    public void removeFromFavorites(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());
        
        if (!user.getFavoriteProducts().contains(product)) {
            throw new ResourceNotFoundException(Messages.PRODUCT_NOT_EXIST_IN_FAVORITE);
        }
        
        user.getFavoriteProducts().remove(product);
        userRepository.save(user);
    }
    
    public List<FavoriteResponse> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        
        return productRepository.findFavoriteProductsDetails(user.getFavoriteProducts());
    }
}
