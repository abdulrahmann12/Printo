package com.team.printo.mapper;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.AttributeValueDTO;
import com.team.printo.dto.AttributeValueResponseDTO;
import com.team.printo.dto.ProductListDTO;
import com.team.printo.dto.ProductRequestDTO;
import com.team.printo.dto.ProductResponseDTO;
import com.team.printo.dto.ReviewDTO;
import com.team.printo.model.AttributeValue;
import com.team.printo.model.Product;
import com.team.printo.model.Review;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId",  source = "category.id")
    @Mapping(target = "attributeValues", source = "attributeValues")
    @Mapping(target = "review.id", ignore = true)
    ProductRequestDTO toDTO(Product product);
	
    
    @Mapping(target = "category.id",  source = "categoryId")
    @Mapping(target = "attributeValues", source = "attributeValues")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "templates", ignore = true)
    @Mapping(target = "designs", ignore = true)
	Product toEntity(ProductRequestDTO productDTO);
    
    
    // Product to ProductRespnseDTO
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "images", source = "images") 
    @Mapping(target = "id", source = "id") 
    @Mapping(target = "attributeValues", source = "attributeValues") 
    ProductResponseDTO toResponseDTO(Product product);
    

    default Map<String, List<AttributeValueResponseDTO>> groupByAttributeName(List<AttributeValue> attributeValues) {
        if (attributeValues == null || attributeValues.isEmpty()) {
            return Collections.emptyMap();
        }

        return attributeValues.stream()
                .map(attrValue -> new AttributeValueResponseDTO(
                        attrValue.getId(),
                        attrValue.getAttribute().getId(),
                        attrValue.getAttribute().getName(),
                        attrValue.getValue(),
                        attrValue.getAvailable()
                ))
                .collect(Collectors.groupingBy(AttributeValueResponseDTO::getAttributeName));
    }
    

    @Mapping(target = "userName", source = "user.firstName")
	@Mapping(target = "productId", source = "product.id")
	ReviewDTO toDTO(Review review);
	
	
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "product", ignore = true)
	Review toEntity(ReviewDTO reviewDTO);
	
	@Mapping(target = "attributeId", source = "attribute.id")
	@Mapping(target = "productId", source = "product.id")
    AttributeValueDTO toDTO(AttributeValue attributeValue);
    
	@Mapping(target = "attribute", ignore = true)
	@Mapping(target = "product", ignore = true)
	@Mapping(target = "id", ignore = true)
    AttributeValue toEntity(AttributeValueDTO attributeValueDTO);
	
	
	@Mapping(target = "categoryId", source = "category.id")
	ProductListDTO toProductListDTO(Product product);
	
}
