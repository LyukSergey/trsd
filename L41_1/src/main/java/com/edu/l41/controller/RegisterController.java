package com.edu.l41.controller;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           Model model) {

        // Перевірка чи username вже зайнятий
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username '" + username + "' вже зайнятий!");
            return "register";
        }

        // Створюємо користувача з BCrypt хешем пароля
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))  // BCrypt!
                .role("ROLE_USER")
                .firstName(firstName)
                .lastName(lastName)
                .balance(100000.0)
                .build();

        userRepository.save(user);

        model.addAttribute("success",
                "Користувач '" + username + "' зареєстрований! Тепер можете увійти.");
        return "register";
    }
}
