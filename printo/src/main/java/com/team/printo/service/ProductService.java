package com.team.printo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.AttributeValueDTO;
import com.team.printo.dto.AttributeValueResponseDTO;
import com.team.printo.dto.ProductImageDTO;
import com.team.printo.dto.ProductRequestDTO;
import com.team.printo.dto.ProductListDTO;
import com.team.printo.dto.ProductResponseDTO;
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
	private final ProductImagesService productImagesService;
	
	
	public ProductResponseDTO createProduct(ProductRequestDTO productDTO, MultipartFile image) throws Exception {
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
        
        ProductResponseDTO dto = productMapper.toResponseDTO(finalProduct);
        dto.setCategoryName(category.getName());

        Map<String, List<AttributeValueResponseDTO>> attributeValuesMap = mapAttributeValues(product);
        dto.setAttributeValues(attributeValuesMap);
        
        return dto;
	}
	
	public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productDTO, MultipartFile image) throws Exception {
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

        List<AttributeValue> attributeValues = createAttributeValues(existingProduct, category, productDTO);
        existingProduct.getAttributeValues().clear();
        existingProduct.getAttributeValues().addAll(attributeValues);
	    
        Product finalProduct = productRepository.save(existingProduct);
        
        ProductResponseDTO dto = productMapper.toResponseDTO(finalProduct);
        dto.setCategoryName(category.getName());

        Map<String, List<AttributeValueResponseDTO>> attributeValuesMap = mapAttributeValues(finalProduct);
        dto.setAttributeValues(attributeValuesMap);
        
        return dto;
	}
	
    public List<ProductListDTO> getAllProducts(){    	
    	return productRepository.findAllWithoutReviews();
    }
	
	public ProductResponseDTO getProductById(Long productId) {
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
	    ProductResponseDTO dto = productMapper.toResponseDTO(product);
	    dto.setCategoryName(product.getCategory().getName());
	    dto.setAttributeValues(mapAttributeValues(product));
	    
	    List<ProductImageDTO> images = productImagesService.getImagesByProductId(productId);
	    dto.setImages(images);
	    
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
	
	@Cacheable(value = "products", key = "#keyword")
	public List<ProductListDTO> fastSearch(String keyword) {
        return productRepository.fastSearch(keyword);
    }
	
	public List<ProductListDTO> getTopSellingProducts(int limit) {
	    Pageable pageable = PageRequest.of(0, limit);
	    return productRepository.findByOrderBySalesCountDesc(pageable);
	}
	
    @Cacheable(value = "products", key = "#minPrice + '-' + #maxPrice")
    public List<ProductListDTO> fastSearchByPriceRange(double minPrice, double maxPrice) {
        return productRepository.fastSearchByPriceRange(minPrice, maxPrice);
    }
	
	// to get list of AttributeValue and used to assign it to product 
	private List<AttributeValue> createAttributeValues(Product product, Category category, ProductRequestDTO productDTO){
        List<AttributeValue> attributeValues = new ArrayList<>();
        
	    if (productDTO.getAttributeValues() != null && !productDTO.getAttributeValues().isEmpty()) {
	        for (AttributeValueDTO attrValueDTO : productDTO.getAttributeValues()) {
	            Attribute attribute = attributeRepository.findById(attrValueDTO.getAttributeId())
	                    .orElseThrow(() -> new ResourceNotFoundException("Attribute not found"));
	            
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
	    return product.getAttributeValues().stream()
	        .map(attrValue -> new AttributeValueResponseDTO(
	                attrValue.getId(),
	                attrValue.getAttribute().getId(),
	                attrValue.getAttribute().getName(),
	                attrValue.getValue(),
	                attrValue.getAvailable()))
	        .collect(Collectors.groupingBy(AttributeValueResponseDTO::getAttributeName));
	}

	
	public void countSelling(Long productId) {
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
	    product.setSalesCount(product.getSalesCount()+1);
	    productRepository.save(product);
	}
	
	
}
