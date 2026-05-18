package com.edu.l41.controller;

import com.edu.l41.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ендпоінт тільки для ADMIN.
 * В SecurityConfig: .requestMatchers("/api/admin/**").hasRole("ADMIN")
 *
 * admin/admin123 → 200 OK
 * user/user123   → 403 Forbidden
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
                "totalUsers", userRepository.count(),
                "message", "This endpoint is only for ADMIN role"
        );
    }
}
