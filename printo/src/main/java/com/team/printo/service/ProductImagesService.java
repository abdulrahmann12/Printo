package com.team.printo.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.Messages;
import com.team.printo.dto.ProductImageDTO;
import com.team.printo.exception.ImageNotFoundException;
import com.team.printo.exception.ProductNotFoundException;
import com.team.printo.mapper.ProductImageMapper;
import com.team.printo.model.Product;
import com.team.printo.model.ProductImage;
import com.team.printo.repository.ProductImageRepository;
import com.team.printo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImagesService {
	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	private final ImageService imageService;
	private final ProductImageMapper productImageMapper;
	
	public ProductImageDTO addImageToProduct(Long productId, MultipartFile image) throws Exception{
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException());
	    
	    
	    ProductImage productImage = new ProductImage();
	    
	    if (image != null && !image.isEmpty()) {
	        try {
	            String imageUrl = imageService.uploadImage(image);
	    	    productImage.setImage(imageUrl);
	        } catch (IOException e) {
	            throw new Exception(Messages.UPLOAD_IMAGE_FAILED + e.getMessage());
	        }
	    }else if (image == null || image.isEmpty()) {
	        throw new IllegalArgumentException(Messages.IMAGE_REQUIRED);
	    }
	    
	    productImage.setProduct(product);
	    
	    ProductImage savedImage = productImageRepository.save(productImage);
	    
		return productImageMapper.toDto(savedImage);
	}
	
	public List<ProductImageDTO> getImagesByProductId(Long productId) {
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException());
	    List<ProductImage> images = productImageRepository.findByProductId(product.getId());
	    return images.stream()
	            .map(productImageMapper::toDto)
	            .collect(Collectors.toList());
	}
	
    public void deleteImage(Long imageId) {
        if (!productImageRepository.existsById(imageId)) {
            throw new ImageNotFoundException();
        }

        productImageRepository.deleteById(imageId);
    }
	
}
