package com.team.printo.controller;


import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.AuthResponse;
import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.BasicResponse.Messages;
import com.team.printo.dto.ChangePasswordRequest;
import com.team.printo.dto.EmailConfirmationRequest;
import com.team.printo.dto.EmailRequest;
import com.team.printo.dto.ErrorDetails;
import com.team.printo.dto.LoginRequest;
import com.team.printo.dto.ResetPasswordDTO;
import com.team.printo.dto.UserRegisterDTO;
import com.team.printo.exception.UserNotFoundException;
import com.team.printo.model.User;
import com.team.printo.service.AuthService;
import com.team.printo.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<BasicResponse> login(@RequestBody LoginRequest loginRequest){
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		User user = authService.getUserByEmail(loginRequest.getEmail());
	    String accessToken = jwtService.generateToken(user);
	    String refreshToken = jwtService.generateRefreshToken(user);
	    jwtService.revokeAllUserTokens(user);
	    jwtService.saveUserToken(user, accessToken);
		return ResponseEntity.ok(new BasicResponse(Messages.LOGIN_SUCCESS,new AuthResponse(accessToken, refreshToken)));
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
	    final String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return unauthorizedResponse(Messages.INVALID_REFRESH_TOKEN);
	    }

	    final String refreshToken = authHeader.substring(7);
	    final String userEmail = jwtService.extractUsername(refreshToken);

	    if (userEmail == null) {
	        return unauthorizedResponse(Messages.COULD_NOT_EXTRACT_USER);
	    }

	    try {
	        User user = authService.getUserByEmail(userEmail);

	        if (!jwtService.validateToken(refreshToken, user)) {
	            return unauthorizedResponse(Messages.INVALID_REFRESH_TOKEN);
	        }

	        String accessToken = jwtService.generateToken(user);
	        jwtService.revokeAllUserTokens(user);
	        jwtService.saveUserToken(user, accessToken);

	        AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
	        return ResponseEntity.ok(new BasicResponse(Messages.NEW_TOKEN_GENERATED, authResponse));

	    } catch (UserNotFoundException e) {
	        return unauthorizedResponse(Messages.USER_NOT_FOUND);
	    }
	}

	private ResponseEntity<ErrorDetails> unauthorizedResponse(String message) {
	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(new ErrorDetails(new Date(), "Unauthorized", message));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
	    final String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body(new ErrorDetails(Messages.TOKEN_NOT_FOUND));
	    }
	    String token = authHeader.substring(7);
	    authService.logout(token);
	    return ResponseEntity.ok(new BasicResponse(Messages.LOGOUT_SUCCESS));
	}
	
	@PostMapping("/register")
	public ResponseEntity<BasicResponse> register(@Valid @RequestBody UserRegisterDTO user){
		return ResponseEntity.ok(new BasicResponse(Messages.REGISTER_SUCCESS,authService.registerUser(user)));
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<BasicResponse> changePassword(@AuthenticationPrincipal UserDetails userDetails,
	                                                    @Valid @RequestBody ChangePasswordRequest request){
	    authService.changePassword(userDetails.getUsername(), request);
	    return ResponseEntity.ok(new BasicResponse(Messages.ChANGE_PASSWORD));
	}
	
	@PostMapping("/confirm-email")
	public ResponseEntity<BasicResponse> confirmEmail(@Valid @RequestBody EmailConfirmationRequest request) {
	    authService.confirmation(request);
	    return ResponseEntity.ok(new BasicResponse(Messages.CONFIRM_EMAIL_SUCCESS));
	}
	
	@PostMapping("/regenerate-code")
	public ResponseEntity<?> regenerateCode(@AuthenticationPrincipal UserDetails userDetails) {
	    if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body(new ErrorDetails(Messages.USER_NOT_AUTHENTICATED));
	    }
	    authService.reGenerateConfirmationCode(userDetails.getUsername());
	    return ResponseEntity.ok(new BasicResponse(Messages.CODE_SENT));
	}
	
	@PostMapping("/forget-password")
	public ResponseEntity<BasicResponse> forgetPassword(@Valid @RequestBody EmailRequest email){
			authService.forgotPassword(email.getEmail());
			return ResponseEntity.ok(new BasicResponse(Messages.CODE_SENT));
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<BasicResponse> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswodDTO){
			authService.resetPassword(resetPasswodDTO);
			return ResponseEntity.ok(new BasicResponse(Messages.ChANGE_PASSWORD));
	}
}

