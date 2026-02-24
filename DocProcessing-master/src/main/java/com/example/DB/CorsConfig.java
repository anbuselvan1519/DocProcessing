package com.example.DB;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {

                registry.addMapping("/**")   // all endpoints
                        .allowedOrigins("http://localhost:5173") // React app
                        .allowedMethods("*")  // GET, POST, PUT, DELETE, etc
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}