package com.team.printo.config;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.printo.dto.ErrorDetails;
import com.team.printo.repository.TokenRepository;
import com.team.printo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private final JwtService jwtService;
	private final TokenRepository tokenRepository;
	private final UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository) {
	    this.jwtService = jwtService;
	    this.userDetailsService = userDetailsService;
	    this.tokenRepository = tokenRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String authorizationHeader = request.getHeader("Authorization");
			String token = null;
			String username = null;
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                if (authorizationHeader.length() <= 7) {
                    ErrorDetails errorDetails = new ErrorDetails(new Date(), "JWT token is missing after Bearer", request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    ObjectMapper mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(errorDetails));
                    return;
                }
				token = authorizationHeader.substring(7);
				username = jwtService.extractUsername(token);
			}
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				var storedToken = tokenRepository.findByToken(token).orElse(null);
				if (storedToken == null || storedToken.isExpired() || storedToken.isRevoked()) {
				    filterChain.doFilter(request, response);
				    return;
				}
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if(jwtService.validateToken(token, userDetails)) {	
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
			}					
			filterChain.doFilter(request, response);
			
		} catch (IllegalArgumentException ex) {
		    sendErrorResponse(response, "Your session has expired. Please login again.", HttpServletResponse.SC_UNAUTHORIZED, request);
		} catch (Exception ex) {
		    sendErrorResponse(response, "Invalid token.", HttpServletResponse.SC_UNAUTHORIZED, request);
		}
	}
	
	private void sendErrorResponse(HttpServletResponse response, String message, int status, HttpServletRequest request) throws IOException {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getRequestURI());
	    response.setStatus(status);
	    response.setContentType("application/json");
	    ObjectMapper mapper = new ObjectMapper();
	    response.getWriter().write(mapper.writeValueAsString(errorDetails));
	}
	}
