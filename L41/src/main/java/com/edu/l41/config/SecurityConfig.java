package com.edu.l41.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/evil.html").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .ignoringRequestMatchers("/api/**")
                )
//                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .formLogin(login -> login
                        .defaultSuccessUrl("/api/users", true)
                        .permitAll()
                )

                // ====== CORS ======
                // Крок 1: Закоментовано → CORS заблокований (браузер блокує відповідь)
                // Крок 2: Розкоментувати → CORS дозволений для localhost:3000
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // CORS конфігурація: які origins, методи, заголовки дозволені
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Які origins дозволені (звідки можуть приходити запити)
        config.setAllowedOrigins(List.of("http://localhost:3001"));

        // Які HTTP методи дозволені
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

        // Які заголовки клієнт може відправляти
        config.setAllowedHeaders(List.of("*"));

        // Чи дозволяти cookies/credentials у крос-доменних запитах
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}