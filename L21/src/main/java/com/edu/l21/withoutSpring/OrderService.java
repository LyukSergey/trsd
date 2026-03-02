package com.edu.l21.withoutSpring;

/**
 * Слайд 5-7: Проблема БЕЗ Spring Жорстка залежність (tight coupling)
 */
public class OrderService {

    // Проблема: створюємо залежність вручну
    private EmailNotificationService notificationService = new EmailNotificationService();

    public void processOrder(String orderId) {
        System.out.println("Processing order: " + orderId);
        notificationService.sendNotification("Order " + orderId + " processed");
    }


}
