package com.team.printo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.team.printo.dto.AttributeDTO;
import com.team.printo.dto.Messages;
import com.team.printo.exception.AttributeNotFoundException;
import com.team.printo.exception.CategoryNotFoundException;
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
		    throw new IllegalArgumentException(Messages.ATTRIBUTE_ALREADY_EXISTS);
		}
        Category category = categoryRepository.findById(attributeDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException());
		
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
		    throw new IllegalArgumentException(Messages.ATTRIBUTE_ALREADY_EXISTS);
		}	
		
		Attribute existingAttribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException());
		
		existingAttribute.setName(attributeDTO.getName());
		
        if (attributeDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(attributeDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException());
            existingAttribute.setCategory(category);
        }
        
        Attribute updatedAttribute = attributeRepository.save(existingAttribute);
        return attributeMapper.toDTO(updatedAttribute);
	}
	
	public void deleteAttribute(Long attributeId) {
		Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException());
		attributeRepository.delete(attribute);
	}
	
	public AttributeDTO getAttributeById(Long attributeId) {
		Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AttributeNotFoundException());
        return attributeMapper.toDTO(attribute);
	}
	
	public List<AttributeDTO> getAttributesByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException());
		return attributeRepository.findByCategoryId(category.getId())
				.stream()
				.map(attributeMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	
}
