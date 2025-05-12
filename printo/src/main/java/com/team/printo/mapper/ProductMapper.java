package com.team.printo.mapper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.AttributeValueDTO;
import com.team.printo.dto.AttributeValueResponseDTO;
import com.team.printo.dto.ProductDTO;
import com.team.printo.dto.ProductRespnseDTO;
import com.team.printo.dto.ReviewDTO;
import com.team.printo.model.AttributeValue;
import com.team.printo.model.Product;
import com.team.printo.model.Review;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId",  source = "category.id")
    @Mapping(target = "attributeValues", source = "attributeValues")
    @Mapping(target = "review.id", ignore = true)
	ProductDTO toDTO(Product product);
	
    @Mapping(target = "category.id",  source = "categoryId")
    @Mapping(target = "attributeValues", source = "attributeValues")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "templates", ignore = true)
    @Mapping(target = "designs", ignore = true)
	Product toEntity(ProductDTO productDTO);
    
    
    // تحويل من Product إلى ProductRespnseDTO
    @Mapping(target = "categoryName", source = "category.name") // هنا نأخذ اسم الفئة بدلاً من الـ id
    @Mapping(target = "images", source = "images") // تحويل الصور
    @Mapping(target = "attributeValues", source = "attributeValues") // تحويل قيم السمات
    ProductRespnseDTO toResponseDTO(Product product);


	
    
    default Map<String, List<AttributeValueResponseDTO>> map(List<AttributeValue> attributeValues) {
        if (attributeValues == null) {
            return Collections.emptyMap();
        }

        Map<String, List<AttributeValueResponseDTO>> attributeValuesMap = new HashMap<>();
        for (AttributeValue attributeValue : attributeValues) {
            String attributeName = attributeValue.getAttribute().getName();  // استخدام name بدلاً من id
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
	
	
	List<AttributeValueResponseDTO> toResponseDTOS(List<AttributeValue> attributeValues);

	
	List<AttributeValue> toResponseEntities(List<AttributeValueResponseDTO> attributeValues);
	
	
	

	

}
