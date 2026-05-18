package com.edu.l41.config;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService — міст між Spring Security і нашою базою даних.
 *
 * Spring Security не знає як зберігаються наші користувачі.
 * Цей сервіс реалізує інтерфейс UserDetailsService і "перекладає"
 * нашу Entity (User) на об'єкт, який розуміє Spring Security (UserDetails).
 *
 * Flow:
 * 1. Користувач вводить login + password у формі
 * 2. Spring Security викликає loadUserByUsername(login)
 * 3. Ми шукаємо User у БД
 * 4. Повертаємо UserDetails з username, password (hash), roles
 * 5. Spring Security порівнює password через PasswordEncoder
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Шукаємо користувача в БД
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        // 2. Перетворюємо нашу Entity → Spring Security UserDetails
        //    password — вже BCrypt hash з БД
        //    roles() автоматично додає префікс ROLE_
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())       // BCrypt hash!
                .authorities(user.getRole())         // "ROLE_ADMIN" або "ROLE_USER"
                .build();
    }
}
