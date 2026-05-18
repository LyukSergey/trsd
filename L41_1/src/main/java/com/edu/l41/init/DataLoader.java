package com.edu.l41.init;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ADMIN — може видаляти користувачів, бачити адмін-ендпоінти
        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))  // BCrypt хешування!
                .role("ROLE_ADMIN")
                .firstName("Іван")
                .lastName("Петренко")
                .email("ivan.petrenko@gmail.com")
                .phone("+380991234567")
                .address("вул. Хрещатик 1, Київ")
                .balance(50000.0)
                .build());

        // USER — звичайний користувач
        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))  // BCrypt хешування!
                .role("ROLE_USER")
                .firstName("Марія")
                .lastName("Коваленко")
                .email("maria.kovalenko@gmail.com")
                .phone("+380997654321")
                .address("вул. Франка 5, Львів")
                .balance(30000.0)
                .build());

        userRepository.save(User.builder()
                .username("oleksiy")
                .password(passwordEncoder.encode("pass123"))
                .role("ROLE_USER")
                .firstName("Олексій")
                .lastName("Шевченко")
                .email("oleksiy.shevchenko@ukr.net")
                .phone("+380501112233")
                .address("пр. Свободи 10, Харків")
                .balance(10000.0)
                .build());

        System.out.println("=== Завантажено " + userRepository.count() + " користувачів ===");
        System.out.println("=== Логіни: admin/admin123 (ADMIN), user/user123 (USER), oleksiy/pass123 (USER) ===");
    }
}
