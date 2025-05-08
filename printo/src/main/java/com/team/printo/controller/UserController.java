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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.RoleRequest;
import com.team.printo.dto.UserDTO;
import com.team.printo.model.User;
import com.team.printo.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	
	@GetMapping("/{userId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId){
		return ResponseEntity.ok(userService.findUserById(userId));
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		return ResponseEntity.ok(userService.findAllUsers());
	}
	
	@PutMapping("/{userId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId,@RequestBody UserDTO userDTO){
		return ResponseEntity.ok(userService.updateUser(userId, userDTO));
	}
	
	@PutMapping("/{userId}/role")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> changeRole(@PathVariable Long userId, @RequestBody RoleRequest request) {
	    userService.changeUserRole(userId, request);
	    return ResponseEntity.ok(new BasicResponse("User role updated successfully"));
	}
	

	@PutMapping("/image")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> updateUserImage(@AuthenticationPrincipal UserDetails details,@RequestPart("image") MultipartFile image)throws Exception{
	    Long userId= ((User) details).getId();
		userService.updateUserImage(userId, image);
	    return ResponseEntity.ok(new BasicResponse("User image updated successfully"));
	}
	
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BasicResponse> deleteUser(@PathVariable Long userId){
	    userService.deleteUser(userId);
	    return ResponseEntity.ok(new BasicResponse("User deleted successfully"));
	}
}
