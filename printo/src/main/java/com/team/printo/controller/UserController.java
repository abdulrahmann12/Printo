package com.team.printo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.Messages;
import com.team.printo.dto.RoleRequest;
import com.team.printo.dto.UserDTO;
import com.team.printo.model.User;
import com.team.printo.model.User.Role;
import com.team.printo.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User Controller", description = "API for managing user profiles and account settings.")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	
	@GetMapping("/profile")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDTO> getUserById(@AuthenticationPrincipal UserDetails userDetails){
		Long userId = ((User) userDetails).getId();
		return ResponseEntity.ok(userService.findUserById(userId));
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		return ResponseEntity.ok(userService.findAllUsers());
	}
	
	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDTO> updateUser(
			@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody UserDTO userDTO,
			@RequestHeader("Authorization") String authHeader
			){
		Long userId = ((User) userDetails).getId();
		String token = authHeader.replace("Bearer ", "");
		return ResponseEntity.ok(userService.updateUser(userId, userDTO,token));
	}
	
	@PutMapping("/{userId}/role")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> changeRole(@PathVariable Long userId, @RequestBody RoleRequest request) {
	    userService.changeUserRole(userId, request);
	    return ResponseEntity.ok(new BasicResponse(Messages.USER_UPDATE_PROFILE));
	}
	

	@PutMapping("/image")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> updateUserImage(@AuthenticationPrincipal UserDetails details,@RequestPart("image") MultipartFile image)throws Exception{
	    Long userId= ((User) details).getId();
		userService.updateUserImage(userId, image);
	    return ResponseEntity.ok(new BasicResponse(Messages.USER_UPDATE_IMAGE));
	}
	
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deleteUser(@PathVariable Long userId){
	    userService.deleteUser(userId);
	    return ResponseEntity.ok(new BasicResponse(Messages.DELETE_USER));
	}
	
    @GetMapping("/user-roles")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Role> getAllRoles() {
        return userService.getAllRoles();
    }
}
	