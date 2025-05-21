package com.team.printo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.CategoryDTO;
import com.team.printo.dto.Messages;
import com.team.printo.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Category Controller", description = "API for managing product categories and subcategories.")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestPart("category") CategoryDTO categoryDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
		CategoryDTO createdCategory = categoryService.createCategory(categoryDTO, image);
        return ResponseEntity.ok(createdCategory);		
	}
	
	@PutMapping("/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> updateCategory(
			@PathVariable Long categoryId,
			@Valid @RequestPart("category") CategoryDTO categoryDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
		CategoryDTO updateCategory = categoryService.updateCategory(categoryId, categoryDTO, image);
        return ResponseEntity.ok(updateCategory);		
	}
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<CategoryDTO>> getAllCategories(){
		return ResponseEntity.ok(categoryService.getAllCategories());
	}
	
	@GetMapping("/root")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<CategoryDTO>> getRootCategories(){
		return ResponseEntity.ok(categoryService.getRootCategories());
	}
	
	@GetMapping("/{categoryId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId){
		return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
	}
	
    @GetMapping("/{parentId}/subcategories")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<CategoryDTO>> getSubcategories(@PathVariable Long parentId){
		return ResponseEntity.ok(categoryService.getSubcategories(parentId));
	}
    
	@DeleteMapping("/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deleteCategory(@PathVariable Long categoryId){
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.ok(new BasicResponse(Messages.DELETE_CATEGORY));
	}

}
