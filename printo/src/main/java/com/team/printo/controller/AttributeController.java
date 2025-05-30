package com.team.printo.controller;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.AttributeDTO;
import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.Messages;
import com.team.printo.service.AttributeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Attribute Controller", description = "API for managing product attributes and specifications.")
@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {

	private final AttributeService attributeService;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> createAttribute(@Valid @RequestBody AttributeDTO attributeDTO){
		AttributeDTO savedAttribute = attributeService.createAttribute(attributeDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.ADD_ATTRIBUTE,savedAttribute));
	}

	@PutMapping("/{attributeId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> updateAttribute(@PathVariable Long attributeId, @Valid @RequestBody AttributeDTO attributeDTO){
		AttributeDTO updatedAttribute = attributeService.updateAttribute(attributeId, attributeDTO);
		return ResponseEntity.ok(new BasicResponse(Messages.UPDATE_ATTRIBUTE,updatedAttribute));
	}
	
	@DeleteMapping("/{attributeId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deleteAttribute(@PathVariable Long attributeId){
		attributeService.deleteAttribute(attributeId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_ATTRIBUTE));
	}
	
	@GetMapping("/{attributeId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable Long attributeId){
		AttributeDTO attribute = attributeService.getAttributeById(attributeId);
		return ResponseEntity.ok(attribute);
	}
	
	@GetMapping("/category/{categoryId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<AttributeDTO>> getAttributesByCategory(@PathVariable Long categoryId){
		List<AttributeDTO> attributes = attributeService.getAttributesByCategory(categoryId);
		return ResponseEntity.ok(attributes);
	}
	
}
