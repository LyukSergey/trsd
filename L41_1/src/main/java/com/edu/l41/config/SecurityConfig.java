package com.edu.l41.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * PasswordEncoder — Spring Security використовує його для:
     * 1. Хешування паролів при реєстрації (encode)
     * 2. Порівняння пароля при логіні (matches)
     *
     * BCrypt: salt + cost factor → захист від brute-force та rainbow tables
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * SecurityFilterChain — головна конфігурація безпеки.
     *
     * Flow автентифікації:
     * 1. Користувач POST /login з username + password
     * 2. UsernamePasswordAuthenticationFilter перехоплює
     * 3. AuthenticationManager → DaoAuthenticationProvider
     * 4. DaoAuthenticationProvider викликає CustomUserDetailsService.loadUserByUsername()
     * 5. CustomUserDetailsService шукає User у БД, повертає UserDetails
     * 6. DaoAuthenticationProvider порівнює пароль через PasswordEncoder.matches()
     * 7. Якщо ОК → Authentication зберігається в SecurityContextHolder
     * 8. Якщо НІ → 401 Unauthorized
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Публічні ресурси — без автентифікації
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/evil.html").permitAll()

                        // GET /api/users — дозволено всім (каталог)
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()

                        // DELETE /api/users/** — тільки ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // /bank, /transfer — будь-який автентифікований
                        .requestMatchers("/bank", "/transfer").authenticated()

                        // /api/admin/** — тільки ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Все інше — автентифікація
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .ignoringRequestMatchers("/api/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                // Form Login — для браузера
                .formLogin(login -> login
                        .defaultSuccessUrl("/bank", true)
                        .permitAll()
                )
                // HTTP Basic — для curl / Postman / REST клієнтів
                .httpBasic(basic -> {})

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // ====== CORS ======
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        ;

        return http.build();
    }

    // CORS конфігурація
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}