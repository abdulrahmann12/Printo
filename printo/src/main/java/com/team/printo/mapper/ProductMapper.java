package com.team.printo.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.AttributeValueDTO;
import com.team.printo.dto.ProductDTO;
import com.team.printo.dto.ReviewDTO;
import com.team.printo.model.AttributeValue;
import com.team.printo.model.Product;
import com.team.printo.model.Review;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId",  source = "category.id")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "attributeValues", source = "attributeValues")
    @Mapping(target = "salesCount", source = "salesCount")
    @Mapping(target = "review.id", ignore = true)
	ProductDTO toDTO(Product product);
	
    @Mapping(target = "category.id",  source = "categoryId")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "attributeValues", source = "attributeValues")
    @Mapping(target = "salesCount", source = "salesCount")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "templates", ignore = true)
    @Mapping(target = "designs", ignore = true)
	Product toEntity(ProductDTO productDTO);
	
	
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "productId", source = "product.id")
	ReviewDTO toDTO(Review review);
	
	
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "product", ignore = true)
	Review toEntity(ReviewDTO reviewDTO);
	
	
	
	@Mapping(target = "attributeId", source = "attribute.id")
	@Mapping(target = "productId", source = "product.id")
    AttributeValueDTO toDTO(AttributeValue attributeValue);
    
	@Mapping(target = "attribute", ignore = true)
	@Mapping(target = "product", ignore = true)
    AttributeValue toEntity(AttributeValueDTO attributeValueDTO);
	
	List<AttributeValueDTO> toDTOS(List<AttributeValue> attributeValues);

	
	List<AttributeValue> toEntities(List<AttributeValueDTO> attributeValues);

}
