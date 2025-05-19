package com.team.printo.service;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.team.printo.dto.AttributeDTO;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.AttributeMapper;
import com.team.printo.model.Attribute;
import com.team.printo.model.Category;
import com.team.printo.repository.AttributeRepository;
import com.team.printo.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeService {

	private final AttributeRepository attributeRepository;
	private final AttributeMapper attributeMapper;
	private final CategoryRepository categoryRepository;
	
	
	public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
		if (attributeRepository.existsByNameAndCategoryId(attributeDTO.getName(), attributeDTO.getCategoryId())) {
		    throw new IllegalArgumentException("Attribute with this name already exists in the category");
		}
        Category category = categoryRepository.findById(attributeDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		
        Attribute attribute =  attributeMapper.toEntity(attributeDTO);
        attribute.setCategory(category);
        
        Attribute saved = attributeRepository.save(attribute);
        return attributeMapper.toDTO(saved);
	}
	
	public AttributeDTO updateAttribute(Long attributeId, AttributeDTO attributeDTO) {
		
		if (attributeRepository.existsByNameAndCategoryIdAndIdNot(
		        attributeDTO.getName(), 
		        attributeDTO.getCategoryId(), 
		        attributeId)) {
		    throw new IllegalArgumentException("Attribute with this name already exists in the category");
		}	
		
		Attribute existingAttribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found"));
		
		existingAttribute.setName(attributeDTO.getName());
		
        if (attributeDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(attributeDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingAttribute.setCategory(category);
        }
        
        Attribute updatedAttribute = attributeRepository.save(existingAttribute);
        return attributeMapper.toDTO(updatedAttribute);
	}
	
	public void deleteAttribute(Long attributeId) {
		Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found"));
		attributeRepository.delete(attribute);
	}
	
	public AttributeDTO getAttributeById(Long attributeId) {
		Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found"));
        return attributeMapper.toDTO(attribute);
	}
	
	public List<AttributeDTO> getAttributesByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		return attributeRepository.findByCategoryId(category.getId())
				.stream()
				.map(attributeMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	
}
