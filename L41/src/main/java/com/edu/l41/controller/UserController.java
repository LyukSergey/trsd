package com.edu.l41.controller;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // ПРОБЛЕМА: будь-хто може отримати ВСІ дані ВСІХ користувачів!
    // Немає автентифікації, немає авторизації, Entity напряму → витік passwordHash
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ПРОБЛЕМА: IDOR — будь-хто може підставити чужий id і побачити чужі дані
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
