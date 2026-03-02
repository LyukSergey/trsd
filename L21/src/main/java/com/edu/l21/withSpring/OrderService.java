package com.edu.l21.withSpring;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // Spring автоматично підставить залежність
    private final NotificationService notificationService;

    // Constructor Injection (рекомендований підхід)
    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void processOrder(String orderId) {
        System.out.println("Processing order: " + orderId);
        notificationService.sendNotification("Order " + orderId + " processed");
    }


}
