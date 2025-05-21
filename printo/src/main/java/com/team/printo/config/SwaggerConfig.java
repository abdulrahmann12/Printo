package com.team.printo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Printo API")
                .version("1.0")
                .description("Backend API for managing an online printing service, including user addresses, products, and orders.")
                .contact(new Contact()
                    .name("abdulraman Ahmed")
                    .email("abdulraman.ahmedd@gmail.com")
                )
            );
    }
}