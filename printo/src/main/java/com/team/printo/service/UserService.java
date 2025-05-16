package com.team.printo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.printo.dto.RoleRequest;
import com.team.printo.dto.UserDTO;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.UserMapper;
import com.team.printo.model.User;
import com.team.printo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final ImageService imageService;
	private final AuthService authService;
	
	public UserDTO findUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return userMapper.toDTO(user);		
	}
	
	public List<UserDTO> findAllUsers() {
		List<User> users = userRepository.findAll();
		return userMapper.toDTOS(users);		
	}
	
	public UserDTO updateUser(Long userId, UserDTO userDTO,String token) {
		
		User user = userMapper.toEntity(userDTO);
		
	    User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    existingUser.setFirstName(user.getFirstName());
	    existingUser.setLastName(user.getLastName());
	    existingUser.setEmail(user.getEmail());
	    existingUser.setPhone(user.getPhone());
	    if(!existingUser.getEmail().equals(userDTO.getEmail())) {
	    	authService.logout(token);
	    }
	    User savedUser = userRepository.save(existingUser);
	    
	    return userMapper.toDTO(savedUser);
	}

	public void updateUserImage(Long userId, MultipartFile image) throws Exception{
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    if (image == null || image.isEmpty()) {
	        throw new IllegalArgumentException("Image file is empty or null");
	    }

	    try {
	        String imageUrl = imageService.uploadImage(image);
	        user.setImage(imageUrl);
	        userRepository.save(user);
	    } catch (Exception e) {
	        throw new RuntimeException("Error occurred while uploading image: " + e.getMessage(), e);
	    }
	}

	@Transactional
	public void changeUserRole(Long userId, RoleRequest request) {
		User user = userRepository.findById(userId)
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		user.setRole(request.getRole());
		System.out.println("Old role: " + user.getRole());
		System.out.println("New role: " + request.getRole());
		userRepository.save(user);
	}
	
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId)
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		userRepository.delete(user);
	}
}
