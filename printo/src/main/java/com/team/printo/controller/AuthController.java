package com.team.printo.controller;

import org.springframework.security.core.Authentication;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.AuthResponse;
import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.ChangePasswordRequest;
import com.team.printo.dto.EmailConfirmationRequest;
import com.team.printo.dto.ErrorDetails;
import com.team.printo.dto.LoginRequest;
import com.team.printo.dto.ResetPasswodDTO;
import com.team.printo.dto.UserRegisterDTO;
import com.team.printo.exception.ResourceNotFoundException;
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
		return ResponseEntity.ok(new BasicResponse("Login Successfully",new AuthResponse(accessToken, refreshToken)));
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
	    final String authHeader = request.getHeader("Authorization");
	    final String refreshToken;
	    final String userEmail;
	    if (authHeader == null || !authHeader.startsWith("Bearer "))
		    return ResponseEntity
		    	    .status(HttpStatus.UNAUTHORIZED)
		    	    .body(new ErrorDetails(
		    	        new Date(),
		    	        "Unauthorized",
		    	        "Invalid or expired refresh token"
		    	    ));
	    refreshToken = authHeader.substring(7);
	    userEmail = jwtService.extractUsername(refreshToken);

	    if (userEmail != null) {
	        User user = authService.getUserByEmail(userEmail);

	        if (jwtService.validateToken(refreshToken, user)) {
	            String accessToken = jwtService.generateToken(user);
	            jwtService.revokeAllUserTokens(user);
	            jwtService.saveUserToken(user, accessToken);
	            return ResponseEntity.ok(new BasicResponse("New Token Generated Successfully",new AuthResponse(accessToken, refreshToken)));
	        }
	    }
	    return ResponseEntity
	    	    .status(HttpStatus.UNAUTHORIZED)
	    	    .body(new ErrorDetails(
	    	        new Date(),
	    	        "Unauthorized",
	    	        "Invalid or expired refresh token"
	    	    ));
	    }
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
	    final String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body(new ErrorDetails("Token not found or invalid"));
	    }

	    String token = authHeader.substring(7);
	    authService.logout(token);

	    return ResponseEntity.ok(new BasicResponse("Logged out successfully"));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO user){
		return ResponseEntity.ok(new BasicResponse("Register Successfully",authService.registerUser(user)));
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		authService.changePassword(email, request);
		return ResponseEntity.ok(new BasicResponse("Password Changed Successfuly"));
	}
	
	@PostMapping("/confirm-email")
	public ResponseEntity<?> confirmEmail(@RequestBody EmailConfirmationRequest request) {
	    authService.confirmation(request);
	    return ResponseEntity.ok(new BasicResponse("Email confirmed successfully"));
	}
	
	@PostMapping("/regenerate-code")
	public ResponseEntity<?> regenerateCode(@AuthenticationPrincipal UserDetails userDetails) {
	    if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body(new ErrorDetails("User is not authenticated"));
	    }

	    authService.reGenerateConfirmationCode(userDetails.getUsername());
	    return ResponseEntity.ok(new BasicResponse("Code sent successfully"));
	}
	
	@PostMapping("/forget-password")
	public ResponseEntity<?> forgetPassword(@Valid @RequestParam("email") String email){
			authService.forgetPassword(email);
			return ResponseEntity.ok(new BasicResponse("code sent successfuly"));
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswodDTO resetPasswodDTO){
			authService.resetPassword(resetPasswodDTO);
			return ResponseEntity.ok(new BasicResponse("password changed successfuly"));
	}
}

