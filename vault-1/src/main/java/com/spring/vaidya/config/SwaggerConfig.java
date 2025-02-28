package com.spring.vaidya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.Value;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Configuration
public class SwaggerConfig {

	@org.springframework.beans.factory.annotation.Value("${spring.application.name:DefaultAppName}")
    private String appName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("My API Documentation"));
    }

   
}

