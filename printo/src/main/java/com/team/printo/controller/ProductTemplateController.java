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
import com.team.printo.dto.ProductTemplateDTO;
import com.team.printo.service.ProductTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product-template")
@RequiredArgsConstructor
public class ProductTemplateController {

    private final ProductTemplateService productTempleteService;

    @PostMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductTemplateDTO> uploadTempleteToProduct(
            @PathVariable Long productId,
            @RequestPart("image") MultipartFile image) throws Exception {
    	ProductTemplateDTO savedImage = productTempleteService.addTempleteToProduct(productId, image);
        return ResponseEntity.ok(savedImage);
    }
    
    @GetMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProductTemplateDTO>> getTempletesByProductId(@PathVariable Long productId) {
        List<ProductTemplateDTO> images = productTempleteService.getTempletesByProductId(productId);
        return ResponseEntity.ok(images);
    }
    
    @DeleteMapping("/{templeteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BasicResponse> deleteTemplate(@PathVariable Long templeteId) {
    	productTempleteService.deleteTemplate(templeteId);
        return ResponseEntity.ok(new BasicResponse(Messages.DELETE_TEMPLATE));
    }
    
}
