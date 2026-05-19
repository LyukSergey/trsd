package com.edu.l41.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Кастомний AuthenticationProvider, який логує КОЖЕН крок автентифікації.
 *
 * Замість стандартного DaoAuthenticationProvider — робить те саме,
 * але з детальним логуванням для навчальних цілей.
 */
@Slf4j
@Component
public class LoggingAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public LoggingAuthenticationProvider(UserDetailsService userDetailsService,
                                         PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        // ═══ Крок 1 — Створення токена ═══
        log.info("");
        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║          SPRING SECURITY AUTHENTICATION FLOW            ║");
        log.info("╚══════════════════════════════════════════════════════════╝");
        log.info("");
        log.info("  [Крок 1] UsernamePasswordAuthenticationToken створено");
        log.info("           username = '{}'", username);
        log.info("           password = '{}' (raw, ще не перевірений)", rawPassword);
        log.info("           authenticated = {} ← ще НЕ автентифікований!", authentication.isAuthenticated());

        // ═══ Крок 2 — AuthenticationManager ═══
        log.info("");
        log.info("  [Крок 2] AuthenticationManager (ProviderManager) делегує роботу");
        log.info("           → LoggingAuthenticationProvider.authenticate()");

        // ═══ Крок 3 — AuthenticationProvider ═══
        log.info("");
        log.info("  [Крок 3] AuthenticationProvider виконує автентифікацію:");
        log.info("           3a) Завантажити UserDetails через UserDetailsService");
        log.info("           3b) Перевірити пароль через PasswordEncoder");

        // ═══ Крок 4a — UserDetailsService ═══
        log.info("");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // ═══ Крок 4b — PasswordEncoder ═══
        log.info("");
        log.info("  [Крок 4b] PasswordEncoder.matches() — порівняння паролів");
        log.info("            Введений пароль (raw): '{}'", rawPassword);
        log.info("            Хеш з БД (BCrypt):     '{}'", userDetails.getPassword());

        boolean passwordMatches = passwordEncoder.matches(rawPassword, userDetails.getPassword());

        log.info("            BCrypt.matches() = {}", passwordMatches);

        if (!passwordMatches) {
            log.warn("");
            log.warn("  [РЕЗУЛЬТАТ] AUTHENTICATION FAILED!");
            log.warn("              Пароль НЕ збігається → BadCredentialsException → 401");
            log.warn("══════════════════════════════════════════════════════════");
            throw new BadCredentialsException("Bad credentials");
        }

        // ═══ Крок 5 — SecurityContextHolder ═══
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,  // credentials обнуляємо після автентифікації
                        userDetails.getAuthorities()
                );

        log.info("");
        log.info("  [Крок 5] SecurityContextHolder — зберігаємо Authentication");
        log.info("           Новий токен створено:");
        log.info("           principal  = '{}'", authToken.getName());
        log.info("           authorities = {}", authToken.getAuthorities());
        log.info("           authenticated = {} ← ТЕПЕР автентифікований!", authToken.isAuthenticated());
        log.info("");
        log.info("  [РЕЗУЛЬТАТ] AUTHENTICATION SUCCESS!");
        log.info("              Користувач '{}' з роллю {} — автентифікований",
                username, userDetails.getAuthorities());
        log.info("══════════════════════════════════════════════════════════");

        // SecurityContext ще порожній на цьому етапі
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        log.info("  [Примітка] SecurityContext зараз: {}",
                currentAuth != null ? currentAuth.getName() : "EMPTY (буде заповнений фільтром)");

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
