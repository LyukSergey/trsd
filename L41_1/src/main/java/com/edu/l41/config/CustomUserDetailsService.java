package com.edu.l41.config;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Крок 4a — UserDetailsService
 *
 * Міст між Spring Security і нашою базою даних.
 * DaoAuthenticationProvider викликає loadUserByUsername() —
 * ми шукаємо User у БД і повертаємо UserDetails.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("══════════════════════════════════════════════════════════");
        log.info("  [Крок 4a] UserDetailsService.loadUserByUsername(\"{}\")", username);
        log.info("  Шукаємо користувача в БД...");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("  [Крок 4a] Користувач '{}' НЕ знайдений → UsernameNotFoundException", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        log.info("  [Крок 4a] Знайдено: username='{}', role='{}', password(hash)='{}...'",
                user.getUsername(),
                user.getRole(),
                user.getPassword().substring(0, 20));
        log.info("  [Крок 4a] Повертаємо UserDetails → далі PasswordEncoder перевірить пароль");
        log.info("══════════════════════════════════════════════════════════");

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }
}
