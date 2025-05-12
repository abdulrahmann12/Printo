package com.team.printo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.AttributeValueDTO;
import com.team.printo.dto.AttributeValueResponseDTO;
import com.team.printo.dto.ProductDTO;
import com.team.printo.dto.ProductListDTO;
import com.team.printo.dto.ProductRespnseDTO;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.ProductMapper;
import com.team.printo.model.Attribute;
import com.team.printo.model.AttributeValue;
import com.team.printo.model.Category;
import com.team.printo.model.Product;
import com.team.printo.repository.AttributeRepository;
import com.team.printo.repository.CategoryRepository;
import com.team.printo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final ImageService imageService;
	private final CategoryRepository categoryRepository;
	private final AttributeRepository attributeRepository;
	
	public ProductRespnseDTO createProduct(ProductDTO productDTO, MultipartFile image) throws Exception {
	    Category category = categoryRepository.findById(productDTO.getCategoryId())
	            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
	    Product product = productMapper.toEntity(productDTO);
	 
        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = imageService.uploadImage(image);
                product.setImage(imageUrl);
            } catch (IOException e) {
                throw new Exception("Error occurred while saving image: " + e.getMessage());
            }
        }
        
        Product savedProduct = productRepository.save(product);
        
        List<AttributeValue> attributeValues = createAttributeValues(savedProduct, category, productDTO);
        savedProduct.getAttributeValues().clear();
        savedProduct.getAttributeValues().addAll(attributeValues);
	    
        Product finalProduct = productRepository.save(savedProduct);
        
        ProductRespnseDTO dto = productMapper.toResponseDTO(finalProduct);
        dto.setCategoryName(category.getName());

        Map<String, List<AttributeValueResponseDTO>> attributeValuesMap = mapAttributeValues(product);
        dto.setAttributeValues(attributeValuesMap);
        
        return dto;
	}
	
	public ProductRespnseDTO updateProduct(Long productId, ProductDTO productDTO, MultipartFile image) throws Exception {

	    Product existingProduct = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	    
	    Category category = categoryRepository.findById(productDTO.getCategoryId())
	            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

	    existingProduct.setName(productDTO.getName());
	    existingProduct.setDescription(productDTO.getDescription());
	    existingProduct.setPrice(productDTO.getPrice());
	    existingProduct.setQuantity(productDTO.getQuantity());
	    existingProduct.setActive(productDTO.isActive());
	    existingProduct.setCategory(category);
	    
	    if (image != null && !image.isEmpty()) {
	        try {
	            String imageUrl = imageService.uploadImage(image);
	            existingProduct.setImage(imageUrl);
	        } catch (IOException e) {
	            throw new Exception("Error occurred while saving image: " + e.getMessage());
	        }
	    }
        Product updatedProduct = productRepository.save(existingProduct);

        List<AttributeValue> attributeValues = createAttributeValues(updatedProduct, category, productDTO);
        updatedProduct.getAttributeValues().clear();
        updatedProduct.getAttributeValues().addAll(attributeValues);
	    
        Product finalProduct = productRepository.save(updatedProduct);
        
        ProductRespnseDTO dto = productMapper.toResponseDTO(finalProduct);
        dto.setCategoryName(category.getName());

        Map<String, List<AttributeValueResponseDTO>> attributeValuesMap = mapAttributeValues(finalProduct);
        dto.setAttributeValues(attributeValuesMap);
        
        return dto;
	}
	
    public List<ProductListDTO> getAllProducts(){    	
    	return productRepository.findAllWithoutComment();
    }
	
	public ProductRespnseDTO getProductById(Long productId) {
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
	    ProductRespnseDTO dto = productMapper.toResponseDTO(product);
	    dto.setCategoryName(product.getCategory().getName());
	    dto.setAttributeValues(mapAttributeValues(product));
	    return dto;
	}


	

	
	public void deleteProduct(Long productId) {
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
	    
	    productRepository.delete(product);
	}
	
	public List<ProductListDTO> getProductsByCategoryId(Long categoryId) {
	    Category category = categoryRepository.findById(categoryId)
	            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

	    return productRepository.findAllByCategoryId(category.getId());
	}
	
	public List<ProductListDTO> getTopSellingProducts(int limit) {
	    Pageable pageable = PageRequest.of(0, limit);
	    return productRepository.findByOrderBySalesCountDesc(pageable);
	}
	
	// to get list of AttributeValue and used to assign it to product 
	private List<AttributeValue> createAttributeValues(Product product, Category category, ProductDTO productDTO){
        List<AttributeValue> attributeValues = new ArrayList<>();
        
	    if (productDTO.getAttributeValues() != null && !productDTO.getAttributeValues().isEmpty()) {
	        for (AttributeValueDTO attrValueDTO : productDTO.getAttributeValues()) {
	            Attribute attribute = attributeRepository.findById(attrValueDTO.getAttributeId())
	                    .orElseThrow(() -> new IllegalArgumentException("Attribute not found"));
	            
	            if (!attribute.getCategory().getId().equals(category.getId())) {
	                throw new IllegalArgumentException("Attribute does not belong to this category");
	            }
	            
	            AttributeValue attributeValue = new AttributeValue();
	            attributeValue.setAttribute(attribute);
	            attributeValue.setValue(attrValueDTO.getValue());
	            attributeValue.setProduct(product);
	            attributeValue.setAvailable(attrValueDTO.isAvailable());
	            
	            attributeValues.add(attributeValue);
	        }
	    }
	    return attributeValues;
	}

	// to get Map of AttributeValue and used in response (every attributeName have list of AttributeValue)
    private Map<String, List<AttributeValueResponseDTO>> mapAttributeValues(Product product) {
    	Map<String, List<AttributeValueResponseDTO>> attributeValuesMap = new HashMap<>();

    	for (AttributeValue attributeValue : product.getAttributeValues()) {
    	    String attributeName = attributeValue.getAttribute().getName();
    	    AttributeValueResponseDTO attributeValueResponseDTO = new AttributeValueResponseDTO(
    	            attributeValue.getId(),
    	            attributeValue.getAttribute().getId(),
    	            attributeValue.getAttribute().getName(),
    	            attributeValue.getValue(),
    	            attributeValue.getAvailable()
    	    );
    	    
    	    attributeValuesMap.computeIfAbsent(attributeName, k -> new ArrayList<>()).add(attributeValueResponseDTO);
    	}
        return attributeValuesMap;
    }

}
