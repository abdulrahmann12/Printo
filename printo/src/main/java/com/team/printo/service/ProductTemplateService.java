package com.team.printo.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.Messages;
import com.team.printo.dto.ProductTemplateDTO;
import com.team.printo.exception.ProductNotFoundException;
import com.team.printo.exception.TemplateNotFoundException;
import com.team.printo.mapper.ProductTemplateMapper;
import com.team.printo.model.Product;
import com.team.printo.model.ProductTemplate;
import com.team.printo.repository.ProductRepository;
import com.team.printo.repository.ProductTemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductTemplateService {
	
	private final ProductRepository productRepository;
	private final ProductTemplateRepository productTemplateRepository;
	private final ImageService imageService;
	private final ProductTemplateMapper productTemplateMapper;
	
	public ProductTemplateDTO addTempleteToProduct(Long productId, MultipartFile image) throws Exception{
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException());
	    
	    ProductTemplate productTemplate = new ProductTemplate();
	    
	    if (image != null && !image.isEmpty()) {
	        try {
	            String imageUrl = imageService.uploadImage(image);
	            productTemplate.setImage(imageUrl);
	        } catch (IOException e) {
	            throw new Exception(Messages.UPLOAD_IMAGE_FAILED + e.getMessage());
	        }
	    }
	    
	    productTemplate.setProduct(product);
	    
	    ProductTemplate savedTemplete = productTemplateRepository.save(productTemplate);
	    
		return productTemplateMapper.toDto(savedTemplete);
	}
	
	public List<ProductTemplateDTO> getTempletesByProductId(Long productId) {
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ProductNotFoundException());
	    List<ProductTemplate> images = productTemplateRepository.findByProductId(product.getId());
	    return images.stream()
	            .map(productTemplateMapper::toDto)
	            .collect(Collectors.toList());
	}
	
    public void deleteTemplate(Long imageId) {
        if (!productTemplateRepository.existsById(imageId)) {
            throw new TemplateNotFoundException();
        }

        productTemplateRepository.deleteById(imageId);
    }
	
}
