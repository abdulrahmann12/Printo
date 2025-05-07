package com.team.printo.service;

import java.util.Optional;
import java.util.Random;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.team.printo.dto.ChangePasswordRequest;
import com.team.printo.dto.ResetPasswodDTO;
import com.team.printo.dto.UserDTO;
import com.team.printo.dto.UserRegisterDTO;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.UserMapper;
import com.team.printo.model.Token;
import com.team.printo.model.User;
import com.team.printo.model.User.Role;
import com.team.printo.repository.TokenRepository;
import com.team.printo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final UserMapper userMapper;
	private final TokenRepository tokenRepository;
	
	
	public UserDTO registerUser(UserRegisterDTO userDTO) {
		User user = userMapper.toEntity(userDTO);
		Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
		if(optionalUser.isPresent()) {
			throw new IllegalStateException("Email is Already Exist");
		}
		user.setRole(Role.USER);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setConfirmationCode(generateConfirmationCode());
		user.setEmailConfirmation(false);
		emailService.sendCode(user,"Confirm Your Email Address");
		User savedUser = userRepository.save(user);
		return userMapper.toDTO(savedUser);
	}
	
	public void forgetPassword(String email) {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		String resetCode = generateConfirmationCode();
		user.setConfirmationCode(resetCode);
		userRepository.save(user);
		emailService.sendCode(user,"Reset Your Password");
	}
	
	public void resetPassword(ResetPasswodDTO resetPasswodDTO) {
		User user = userRepository.findByEmail(resetPasswodDTO.getEmail())
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		if(!resetPasswodDTO.getCode().equals(user.getConfirmationCode())) {
	        throw new BadCredentialsException("Invalid reset code");
		}
		user.setPassword(passwordEncoder.encode(resetPasswodDTO.getNewPassword()));
		user.setConfirmationCode(null);
		userRepository.save(user);
	}
	
	public void changePassword(String email, ChangePasswordRequest request) {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new BadCredentialsException("Current Password is incorrect");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}
	
	public void reGenerateConfirmationCode(String email) {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		String resetCode = generateConfirmationCode();
		user.setConfirmationCode(resetCode);
		userRepository.save(user);
		emailService.sendCode(user,"Confirmation Code");	
	}
	
	public void confirmation(String email, String code) {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		if(code.equals(user.getConfirmationCode())) {
			user.setConfirmationCode(null);
			user.setEmailConfirmation(true);
			userRepository.save(user);
		}else {
			throw new BadCredentialsException("Invalid confirmation code");
		}
	}
	
	public void saveUserToken(User user, String jwtToken) {
	    Token token = Token.builder()
	            .user(user)
	            .token(jwtToken)
	            .expired(false)
	            .revoked(false)
	            .build();
	    tokenRepository.save(token);
	}
	
	public void revokeAllUserTokens(User user) {
	    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
	    validUserTokens.forEach(token -> {
	        token.setExpired(true);
	        token.setRevoked(true);
	        tokenRepository.delete(token);
	    });
	    tokenRepository.deleteAll(validUserTokens);
	}
	
	public void logout(String token) {
		var storedToken = tokenRepository.findByToken(token).orElse(null);
		if(storedToken != null) {
	        storedToken.setExpired(true);
	        storedToken.setRevoked(true);
	        tokenRepository.save(storedToken);
		}
	}
	
	private String generateConfirmationCode() {
		Random random = new Random();
		int code = 1000+random.nextInt(90000);
		return String.valueOf(code);
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).
				orElseThrow(()-> new ResourceNotFoundException("User not found."));
	}
	
	public void changeUserRole(Long userId, Role newRole) {
		User user = userRepository.findById(userId)
			    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
		user.setRole(newRole);
		userRepository.save(user);
	}
}
