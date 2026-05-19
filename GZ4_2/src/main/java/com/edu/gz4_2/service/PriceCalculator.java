package com.edu.gz4_2.service;

import org.springframework.stereotype.Component;

/**
 * PriceCalculator — приклад класу, створеного через TDD.
 *
 * Тести написані ПЕРШИМИ (PriceCalculatorTest.java),
 * потім реалізація — мінімальна, щоб тести пройшли.
 */
@Component
public class PriceCalculator {

    /**
     * Розраховує загальну вартість: ціна × кількість
     */
    public double calculateTotal(double price, int quantity) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        return price * quantity;
    }

    /**
     * Застосовує знижку у відсотках
     */
    public double applyDiscount(double total, double discountPercent) {
        if (discountPercent < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        if (discountPercent > 100) {
            throw new IllegalArgumentException("Discount cannot exceed 100%");
        }
        return total * (1 - discountPercent / 100);
    }

    /**
     * Додає податок у відсотках
     */
    public double calculateWithTax(double total, double taxPercent) {
        if (taxPercent < 0) {
            throw new IllegalArgumentException("Tax cannot be negative");
        }
        return total * (1 + taxPercent / 100);
    }
}
