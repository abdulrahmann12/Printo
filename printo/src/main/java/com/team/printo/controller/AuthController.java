package com.team.printo.controller;

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
import com.team.printo.dto.Messages;
import com.team.printo.dto.ChangePasswordRequest;
import com.team.printo.dto.EmailConfirmationRequest;
import com.team.printo.dto.EmailRequest;
import com.team.printo.dto.LoginRequest;
import com.team.printo.dto.ResetPasswordDTO;
import com.team.printo.dto.UserRegisterDTO;
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
	public ResponseEntity<BasicResponse> login(@Valid @RequestBody LoginRequest loginRequest){
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		User user = authService.getUserByEmail(loginRequest.getEmail());
	    String accessToken = jwtService.generateToken(user);
	    String refreshToken = jwtService.generateRefreshToken(user);
	    jwtService.revokeAllUserTokens(user);
	    jwtService.saveUserToken(user, accessToken);
		return ResponseEntity.ok(new BasicResponse(Messages.LOGIN_SUCCESS,new AuthResponse(accessToken, refreshToken)));
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<BasicResponse> refreshToken(HttpServletRequest request) {
	    AuthResponse response = authService.refreshToken(request);
	    return ResponseEntity.ok(new BasicResponse(Messages.NEW_TOKEN_GENERATED, response));
	}

	@PostMapping("/logout")
	public ResponseEntity<BasicResponse> logout(HttpServletRequest request) {
	    authService.logout(request);
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
	    return ResponseEntity.ok(new BasicResponse(Messages.CHANGE_PASSWORD));
	}
	
	@PostMapping("/confirm-email")
	public ResponseEntity<BasicResponse> confirmEmail(@Valid @RequestBody EmailConfirmationRequest request) {
	    authService.confirmation(request);
	    return ResponseEntity.ok(new BasicResponse(Messages.CONFIRM_EMAIL_SUCCESS));
	}
	
	@PostMapping("/regenerate-code")
	public ResponseEntity<BasicResponse> regenerateCode(@AuthenticationPrincipal UserDetails userDetails) {

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
			return ResponseEntity.ok(new BasicResponse(Messages.CHANGE_PASSWORD));
	}
}

