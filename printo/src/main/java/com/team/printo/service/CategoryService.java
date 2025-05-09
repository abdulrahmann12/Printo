package com.team.printo.service;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.CategoryDTO;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.CategoryMapper;
import com.team.printo.model.Category;
import com.team.printo.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	private final ImageService imageService;

	
	public CategoryDTO createCategory(CategoryDTO categoryDTO, MultipartFile image) throws Exception {
	    if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
	        throw new IllegalArgumentException("Category with name '" + categoryDTO.getName() + "' already exists.");
	    }
		Category category = categoryMapper.toEntity(categoryDTO);
	    
        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = imageService.uploadImage(image);
                category.setImage(imageUrl);
            } catch (IOException e) {
                throw new Exception("Error occurred while saving image: " + e.getMessage());
            }
        }
        
		if(categoryDTO.getParentId() != null) {
			Category parentCategory = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
			category.setParent(parentCategory);
		}
		Category savedCategory = categoryRepository.save(category);
		
		return categoryMapper.toDTO(savedCategory);
	}
	
	public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO, MultipartFile image) throws Exception {
		Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		
		existingCategory.setName(categoryDTO.getName());
		
        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = imageService.uploadImage(image);
                existingCategory.setImage(imageUrl);
            } catch (IOException e) {
                throw new Exception("Error occurred while saving image: " + e.getMessage());
            }
        }
		if(categoryDTO.getParentId() != null) {
			Category parentCategory = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
			existingCategory.setParent(parentCategory);
		}else {
			existingCategory.setParent(null);
		}
		Category savedCategory = categoryRepository.save(existingCategory);
		return categoryMapper.toDTO(savedCategory);
	}
	
	public void deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		categoryRepository.delete(category);
	}
	
	public List<CategoryDTO> getRootCategories(){
        return categoryRepository.findByParentIsNull()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());	
        }
	
	public List<CategoryDTO> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());	
        }
	
	public CategoryDTO getCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		return categoryMapper.toDTO(category);
	}
	
	public List<CategoryDTO> getSubcategories(Long parentId) {
        return categoryRepository.findByParentId(parentId)
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());	
	}
}
