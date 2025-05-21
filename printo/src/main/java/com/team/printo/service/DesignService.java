package com.team.printo.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.DesignDTO;
import com.team.printo.dto.Messages;
import com.team.printo.exception.DesignNotFoundException;
import com.team.printo.exception.ProductNotFoundException;
import com.team.printo.exception.UserNotFoundException;
import com.team.printo.mapper.DesignMapper;
import com.team.printo.model.Design;
import com.team.printo.model.Product;
import com.team.printo.model.User;
import com.team.printo.repository.DesignRepository;
import com.team.printo.repository.ProductRepository;
import com.team.printo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DesignService {
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final DesignRepository designRepository;
	private final DesignMapper designMapper;
	private final ImageService imageService;
	
	public DesignDTO addDesign(Long userId, Long productId ,MultipartFile image) throws Exception{
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException());
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException());
	    
	    Design newDesign = new Design();
	    
	    if (image != null && !image.isEmpty()) {
	        try {
	            String imageUrl = imageService.uploadImage(image);
	            newDesign.setImage(imageUrl);
	        } catch (IOException e) {
	            throw new Exception(Messages.UPLOAD_IMAGE_FAILED + e.getMessage());
	        }
	    }else if (image == null || image.isEmpty()) {
	        throw new IllegalArgumentException(Messages.IMAGE_REQUIRED);
	    }
	    
	    newDesign.setProduct(product);
	    newDesign.setUser(user);
	    
	    Design savedDesign = designRepository.save(newDesign);
	    
	    return designMapper.toDto(savedDesign);
	}
	
	public List<DesignDTO> getDesignsByUserId(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException());
	    List<Design> designs = designRepository.findByUserId(user.getId());
	    return designs.stream()
	            .map(designMapper::toDto)
	            .collect(Collectors.toList());
	}
	
	public void deleteDesign(Long designId) {
	    Design design = designRepository.findById(designId)
	            .orElseThrow(() -> new DesignNotFoundException());
	    designRepository.delete(design);
	}
	
}
