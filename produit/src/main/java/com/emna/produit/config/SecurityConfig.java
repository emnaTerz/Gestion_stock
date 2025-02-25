package com.emna.produit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for API requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/product/**").permitAll() // Allow all requests to "/client/**"
                        .anyRequest().authenticated());
        return http.build();
    }
}
