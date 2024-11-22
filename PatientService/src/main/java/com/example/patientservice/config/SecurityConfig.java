package com.example.patientservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/patient/**").permitAll()
                        .requestMatchers("/api/documents/**").permitAll()// Allows all requests to /api/patient/** endpoints
                        .anyRequest().authenticated()                    // Requires authentication for other endpoints
                )
                .csrf(csrf -> csrf.disable());                           // Disable CSRF for testing purposes
        return http.build();
    }
}