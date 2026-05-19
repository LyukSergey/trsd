package com.edu.l41.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фільтр, який логує стан SecurityContext ПІСЛЯ автентифікації.
 *
 * Реєструється ПІСЛЯ BasicAuthenticationFilter в SecurityConfig:
 *   .addFilterAfter(securityContextLoggingFilter, BasicAuthenticationFilter.class)
 *
 * На цьому етапі BasicAuthenticationFilter вже зробив:
 *   SecurityContextHolder.getContext().setAuthentication(authToken)
 */
@Slf4j
@Component
public class SecurityContextLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            log.info("");
            log.info("  [Крок 6] SecurityContextLoggingFilter — ПІСЛЯ автентифікації");
            log.info("           SecurityContext ЗАПОВНЕНИЙ фільтром BasicAuthenticationFilter:");
            log.info("           principal     = '{}'", auth.getName());
            log.info("           authorities   = {}", auth.getAuthorities());
            log.info("           authenticated = {}", auth.isAuthenticated());
            log.info("           credentials   = {} ← обнулені (пароль видалено з пам'яті)",
                    auth.getCredentials());
            log.info("           Запит {} {} → йде далі до Controller",
                    request.getMethod(), request.getRequestURI());
            log.info("══════════════════════════════════════════════════════════");
        }

        filterChain.doFilter(request, response);
    }
}
