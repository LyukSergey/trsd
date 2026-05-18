package com.edu.l41.controller;

import com.edu.l41.entity.User;
import com.edu.l41.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TransferController {

    private final UserRepository userRepository;

    // Сторінка банку — форма переказу
    @GetMapping("/bank")
    public String bankPage(Model model) {
        User ivan = userRepository.findById(1L).orElseThrow();
        model.addAttribute("user", ivan);
        return "bank";
    }

    // POST — виконати переказ
    @PostMapping("/transfer")
    public String transfer(@RequestParam String to,
                           @RequestParam Double amount,
                           Model model) {

        User ivan = userRepository.findById(1L).orElseThrow();

        ivan.setBalance(ivan.getBalance() - amount);
        userRepository.save(ivan);

        model.addAttribute("user", ivan);
        model.addAttribute("message",
                "Переказано " + amount + " грн на рахунок: " + to);

        return "bank";
    }
}
