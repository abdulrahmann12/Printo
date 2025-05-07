package com.team.printo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.team.printo.repository.TokenRepository;
import com.team.printo.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
			
		}catch (ExpiredJwtException ex) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"message\": \"Your session has expired. Please login again.\"}");
	    } catch (Exception ex) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"message\": \"Invalid token.\"}");
	        }
		}
	}
