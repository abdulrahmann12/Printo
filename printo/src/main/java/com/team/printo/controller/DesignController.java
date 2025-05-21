package com.team.printo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.DesignDTO;
import com.team.printo.dto.Messages;
import com.team.printo.model.User;
import com.team.printo.service.DesignService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Design Controller", description = "API for uploading and managing user print designs.")
@RestController
@RequestMapping("/api/design")
@RequiredArgsConstructor
public class DesignController {

	private final DesignService designService;
	
    @PostMapping("/product/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BasicResponse> addDesign(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId,
            @Valid @RequestPart(value = "image", required = false) MultipartFile image
    ) throws Exception {
        Long userId = ((User) userDetails).getId();
        DesignDTO newDesign = designService.addDesign(userId, productId, image);
        return ResponseEntity.ok(new BasicResponse(Messages.UPLOAD_DESIGN, newDesign));
    }
    
    @GetMapping("/my-designs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DesignDTO>> getMyDesigns(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((User) userDetails).getId();
        List<DesignDTO> designs = designService.getDesignsByUserId(userId);
        return ResponseEntity.ok(designs);
    }
    
    @DeleteMapping("/{designId}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BasicResponse> deleteDesign(
            @PathVariable Long designId
    ) {
        designService.deleteDesign(designId);
        return ResponseEntity.ok(new BasicResponse(Messages.DELETE_DESIGN));
    }
}
