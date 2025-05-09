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
import com.team.printo.dto.ProductDTO;
import com.team.printo.dto.ProductListDTO;
import com.team.printo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    
    @PostMapping
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(
    		@Valid @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        ProductDTO createdProduct = productService.createProduct(productDTO, image);
        return ResponseEntity.ok(createdProduct);
    }
    
    
    @PutMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(
    		@PathVariable Long productId,
    		@Valid @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        ProductDTO updateProduct = productService.updateProduct(productId,productDTO, image);
        return ResponseEntity.ok(updateProduct);
    }
    
    @DeleteMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BasicResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new BasicResponse("Product deleted successfully"));
    }
    

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ProductListDTO>> getProductAll(){
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	@GetMapping("/{productId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId){
		return ResponseEntity.ok(productService.getProductById(productId));
	}
	
    @GetMapping("/category/{categoryId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<ProductListDTO>> getProductsByCategoryId(@PathVariable Long categoryId){
        List<ProductListDTO> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);	   
    }
    
}
