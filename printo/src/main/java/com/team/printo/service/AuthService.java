package com.team.printo.service;

import java.util.Optional;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.team.printo.dto.ChangePasswordRequest;
import com.team.printo.dto.EmailConfirmationRequest;
import com.team.printo.dto.Messages;
import com.team.printo.dto.ResetPasswordDTO;
import com.team.printo.dto.UserDTO;
import com.team.printo.dto.UserRegisterDTO;
import com.team.printo.exception.EmailAlreadyExistsException;
import com.team.printo.exception.InvalidConfirmationCodeException;
import com.team.printo.exception.InvalidCurrentPasswordException;
import com.team.printo.exception.InvalidResetCodeException;
import com.team.printo.exception.InvalidTokenException;
import com.team.printo.exception.UserNotFoundException;
import com.team.printo.mapper.UserMapper;
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
			throw new EmailAlreadyExistsException();
		}
		user.setRole(Role.USER);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setConfirmationCode(generateConfirmationCode());
		user.setEmailConfirmation(false);
		emailService.sendCode(user,Messages.CONFIRM_EMAIL);
		User savedUser = userRepository.save(user);
		return userMapper.toDTO(savedUser);
	}
	
	public void forgotPassword(String email) {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new UserNotFoundException(email));
		String resetCode = generateConfirmationCode();
		user.setConfirmationCode(resetCode);
		userRepository.save(user);
		emailService.sendCode(user,Messages.RESET_PASSWORD);
	}
	
	public void resetPassword(ResetPasswordDTO resetPasswodDTO) {
		User user = userRepository.findByEmail(resetPasswodDTO.getEmail())
			    .orElseThrow(() -> new UserNotFoundException(resetPasswodDTO.getEmail()));
		if(!resetPasswodDTO.getCode().equals(user.getConfirmationCode())) {
			throw new InvalidResetCodeException();
		}
		user.setPassword(passwordEncoder.encode(resetPasswodDTO.getNewPassword()));
		user.setConfirmationCode(null);
		userRepository.save(user);
	}
	
	public void changePassword(String email, ChangePasswordRequest request) {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new UserNotFoundException(email));
		if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new InvalidCurrentPasswordException();
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}
	
	public void reGenerateConfirmationCode(String email) {
		User user = userRepository.findByEmail(email)
			    .orElseThrow(() -> new UserNotFoundException(email));
		String resetCode = generateConfirmationCode();
		user.setConfirmationCode(resetCode);
		userRepository.save(user);
		emailService.sendCode(user,Messages.CONFIRM_EMAIL);	
	}
	
	public void confirmation(EmailConfirmationRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
			    .orElseThrow(() -> new UserNotFoundException(request.getEmail()));
		if(request.getConfirmationCode().equals(user.getConfirmationCode())) {
			user.setConfirmationCode(null);
			user.setEmailConfirmation(true);
			userRepository.save(user);
		}else {
			throw new InvalidConfirmationCodeException();
		}
	}
	

	public void logout(String token) {
		var storedToken = tokenRepository.findByToken(token).orElse(null);
		if(storedToken != null && !storedToken.isExpired()) {
	        storedToken.setExpired(true);
	        storedToken.setRevoked(true);
	        tokenRepository.save(storedToken);
		}else {
			throw new InvalidTokenException();
		}
	}
	
	private String generateConfirmationCode() {
		Random random = new Random();
		int code = 1000+random.nextInt(90000);
		return String.valueOf(code);
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).
				orElseThrow(()-> new UserNotFoundException(email));
	}
	
	

}
