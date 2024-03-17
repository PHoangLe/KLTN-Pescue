package com.project.pescueshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:4200",
                        "http://localhost:4201",
                        "http://localhost:8080",
                        "https://do-an2-admin.vercel.app",
                        "https://doan02-fe-admin-production.up.railway.app",
                        "https://pescue-shop.vercel.app",
                        "https://doan02-be-production.up.railway.app")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders(
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials")
                .allowCredentials(true);
    }
}
