package com.team.printo.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.CategoryDTO;
import com.team.printo.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "image", source = "image")
    CategoryDTO toDto(Category category);
    
    @Mapping(target = "image", source = "image")
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parent", ignore = true) 
    Category toEntity(CategoryDTO categoryDTO);
}
