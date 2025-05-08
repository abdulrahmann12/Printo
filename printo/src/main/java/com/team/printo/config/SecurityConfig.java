package com.team.printo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.team.printo.repository.TokenRepository;
import com.team.printo.repository.UserRepository;
import com.team.printo.service.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(c->c.disable())
			.authorizeHttpRequests(auth-> auth
					.requestMatchers("/").permitAll()
					.requestMatchers("/images/**").permitAll()
					.requestMatchers("/index.html").permitAll()
					.requestMatchers("/api/auth/**").permitAll()
					.requestMatchers(HttpMethod.GET,"/api/products/**").permitAll()
					.requestMatchers("/api/auth/change-password").authenticated()
					.requestMatchers("/api/auth/refresh-token").permitAll()
					.anyRequest().authenticated())
			.sessionManagement(session -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtService, tokenRepository, userDetailsService());
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> (UserDetails) userRepository.findByEmail(username)
				.orElseThrow(()->new UsernameNotFoundException("User not found !"));
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;	
	}	
}
