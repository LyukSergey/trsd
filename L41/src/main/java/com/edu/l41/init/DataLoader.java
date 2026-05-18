package com.edu.l41.init;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        userRepository.save(User.builder()
                .firstName("Іван")
                .lastName("Петренко")
                .email("ivan.petrenko@gmail.com")
                .phone("+380991234567")
                .address("вул. Хрещатик 1, Київ")
                .passwordHash("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy")
                .balance(50000.0)
                .build());

        userRepository.save(User.builder()
                .firstName("Марія")
                .lastName("Коваленко")
                .email("maria.kovalenko@gmail.com")
                .phone("+380997654321")
                .address("вул. Франка 5, Львів")
                .passwordHash("$2a$10$Xk3mRzW1vB2hK9pLmNqYOeD4fG5hJ6kL7mN8oP9qR0sT1uV2wX3y")
                .balance(30000.0)
                .build());

        userRepository.save(User.builder()
                .firstName("Олексій")
                .lastName("Шевченко")
                .email("oleksiy.shevchenko@ukr.net")
                .phone("+380501112233")
                .address("пр. Свободи 10, Харків")
                .passwordHash("$2a$10$aB3cD4eF5gH6iJ7kL8mN9oP0qR1sT2uV3wX4yZ5aB6cD7eF8gH9i")
                .balance(10000.0)
                .build());

        System.out.println("=== Завантажено " + userRepository.count() + " користувачів ===");
    }
}
