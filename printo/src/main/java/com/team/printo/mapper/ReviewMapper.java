package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.ReviewDTO;
import com.team.printo.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "user", source = "user")
	@Mapping(target = "productId", source = "product.id")
	ReviewDTO toDTO(Review review);
	
	
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "product", ignore = true)
	Review toEntity(ReviewDTO reviewDTO);
}
