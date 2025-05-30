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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.Messages;
import com.team.printo.dto.ProductRequestDTO;
import com.team.printo.dto.ProductListDTO;
import com.team.printo.dto.ProductResponseDTO;
import com.team.printo.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product Controller", description = "API for managing printable products and their details.")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    
    @PostMapping
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BasicResponse> createProduct(
    		@Valid @RequestPart("product") ProductRequestDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
    	ProductResponseDTO createdProduct = productService.createProduct(productDTO, image);
        return ResponseEntity.ok(new BasicResponse(Messages.ADD_PRODUCT, createdProduct));
    }
    
    
    @PutMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BasicResponse> updateProduct(
    		@PathVariable Long productId,
    		@Valid @RequestPart("product") ProductRequestDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
    	ProductResponseDTO updateProduct = productService.updateProduct(productId,productDTO, image);
        return ResponseEntity.ok(new BasicResponse(Messages.UPDATE_PRODUCT, updateProduct));
    }
    
    @DeleteMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BasicResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new BasicResponse(Messages.DELETE_PRODUCT));
    }
    

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ProductListDTO>> getProductAll(){
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	@GetMapping("/{productId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long productId){
		return ResponseEntity.ok(productService.getProductById(productId));
	}
	
    @GetMapping("/category/{categoryId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ProductListDTO>> getProductsByCategoryId(@PathVariable Long categoryId){
        List<ProductListDTO> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);	   
    }
    
	@GetMapping("/best-Selling/{numberOfItem}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ProductListDTO>> getTopSellingProducts(@PathVariable int numberOfItem){
		return ResponseEntity.ok(productService.getTopSellingProducts(numberOfItem));
	}
	
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()") 
    public List<ProductListDTO> fastSearchByName(@RequestParam String keyword) {
        return productService.fastSearch(keyword);
    }

    @GetMapping("/filter-price")
    @PreAuthorize("isAuthenticated()") 
    public List<ProductListDTO> fastSearchByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return productService.fastSearchByPriceRange(minPrice, maxPrice);
    }
}
