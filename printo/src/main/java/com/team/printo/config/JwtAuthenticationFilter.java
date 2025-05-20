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
import com.team.printo.dto.Messages;
import com.team.printo.exception.AuthenticationCredentialsNotFoundException;
import com.team.printo.exception.InvalidTokenException;
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
	        String token = extractToken(request);
	        String username = jwtService.extractUsername(token);
	        
	        if (username == null) {
	            throw new InvalidTokenException(Messages.COULD_NOT_EXTRACT_USER);
	        }

	        if (SecurityContextHolder.getContext().getAuthentication() == null) {
	            var savedToken = tokenRepository.findByToken(token)
	                .orElseThrow(() -> new InvalidTokenException(Messages.TOKEN_NOT_FOUND));

	            if (savedToken.isExpired() || savedToken.isRevoked()) {
	                throw new InvalidTokenException(Messages.TOKEN_EXPIRED_OR_REVOKED);
	            }

	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            if (userDetails == null) {
	                throw new InvalidTokenException(Messages.USER_NOT_FOUND);
	            }

	            if (jwtService.validateToken(token, userDetails)) {
	                UsernamePasswordAuthenticationToken authenticationToken =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            }
	        }

	        filterChain.doFilter(request, response);

	    } catch (AuthenticationCredentialsNotFoundException ex) {
	        sendErrorResponse(response, Messages.MISSING_TOKEN, HttpServletResponse.SC_UNAUTHORIZED, request);
	    } catch (InvalidTokenException ex) {
	        sendErrorResponse(response, ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED, request);
	    } catch (IllegalArgumentException ex) {
	        sendErrorResponse(response, Messages.SESSION_EXPIRED, HttpServletResponse.SC_UNAUTHORIZED, request);
	    } catch (Exception ex) {
	        sendErrorResponse(response, Messages.AUTH_FAILED, HttpServletResponse.SC_UNAUTHORIZED, request);
	    }
	}
	
	private void sendErrorResponse(HttpServletResponse response, String message, int status, HttpServletRequest request) throws IOException {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), message, request.getRequestURI());
	    response.setStatus(status);
	    response.setContentType("application/json");
	    ObjectMapper mapper = new ObjectMapper();
	    response.getWriter().write(mapper.writeValueAsString(errorDetails));
	}
	

	private String extractToken(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");
	    
	    
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        throw new AuthenticationCredentialsNotFoundException(Messages.MISSING_TOKEN);
	    }
	    String token = authHeader.substring(7);
	    if (token.isEmpty()) {
	        throw new InvalidTokenException(Messages.TOKEN_NOT_FOUND);
	    }
	    return token;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getServletPath();
	    String method = request.getMethod();

	    return path.equals("/") ||
	            path.equals("/index.html") ||
	            path.startsWith("/images/") ||
	            path.startsWith("/v3/api-docs") ||
	            path.startsWith("/swagger-ui") ||
	            path.equals("/api/auth/login") ||
	            path.equals("/api/auth/register") ||
	            path.equals("/api/auth/refresh-token") ||
	            (method.equals("GET") && path.startsWith("/api/products"));
	 }
}
