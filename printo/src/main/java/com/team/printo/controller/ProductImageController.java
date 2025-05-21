package com.team.printo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.Messages;
import com.team.printo.dto.ProductImageDTO;
import com.team.printo.service.ProductImagesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product Image Controller", description = "API for managing product images and media files.")
@RestController
@RequestMapping("/api/product-images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImagesService productImagesService;

    @PostMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductImageDTO> uploadImageToProduct(
            @PathVariable Long productId,
            @RequestPart("image") MultipartFile image) throws Exception {
        ProductImageDTO savedImage = productImagesService.addImageToProduct(productId, image);
        return ResponseEntity.ok(savedImage);
    }
    
    @GetMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProductImageDTO>> getImagesByProduct(@PathVariable Long productId) {
        List<ProductImageDTO> images = productImagesService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }
    
    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BasicResponse> deleteImage(@PathVariable Long imageId) {
        productImagesService.deleteImage(imageId);
        return ResponseEntity.ok(new BasicResponse(Messages.DELETE_IMAGE));
    }
    
}
