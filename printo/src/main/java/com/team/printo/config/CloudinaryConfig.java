package com.team.printo.config;


import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
	@Bean
	public Cloudinary cloudinary() {
	    System.out.println("Creating Cloudinary bean...");
	    Map<String, String> config = new HashMap<>();
	    config.put("cloud_name", "djhbgtqbg");
	    config.put("api_key", "732883554933543");
	    config.put("api_secret", "heAj_OozrTISn_M2nH4roNyGPXE");
	    return new Cloudinary(config);
	}
}