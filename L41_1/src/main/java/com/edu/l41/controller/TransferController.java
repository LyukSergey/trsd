package com.edu.l41.controller;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TransferController {

    private final UserRepository userRepository;

    // Сторінка банку — дані поточного залогіненого користувача
    @GetMapping("/bank")
    public String bankPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        return "bank";
    }

    // POST — переказ від поточного залогіненого користувача
    @PostMapping("/transfer")
    public String transfer(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam String to,
                           @RequestParam Double amount,
                           Model model) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("message",
                "Переказано " + amount + " грн на рахунок: " + to);

        return "bank";
    }
}
