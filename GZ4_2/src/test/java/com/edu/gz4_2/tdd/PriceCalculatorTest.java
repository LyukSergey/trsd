package com.edu.gz4_2.tdd;

import com.edu.gz4_2.service.PriceCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD: Red-Green-Refactor демонстрація.
 *
 * Ці тести були написані ПЕРЕД реалізацією PriceCalculator.
 *
 * Крок 1 (RED):    Написати тест → він FAIL (клас ще не існує)
 * Крок 2 (GREEN):  Написати мінімальну реалізацію → тест PASS
 * Крок 3 (REFACTOR): Покращити код, зберігаючи зелені тести
 *
 * AAA Pattern у кожному тесті:
 *   Arrange — підготовка вхідних даних
 *   Act     — виклик методу
 *   Assert  — перевірка результату
 */
@DisplayName("PriceCalculator — TDD Red-Green-Refactor")
class PriceCalculatorTest {

    private final PriceCalculator calculator = new PriceCalculator();

    // ═══════════════════════════════════════════
    // calculateTotal()
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("calculateTotal(price, quantity)")
    class CalculateTotal {

        @Test
        @DisplayName("should multiply price by quantity")
        void shouldMultiplyPriceByQuantity() {
            // Arrange
            double price = 100.0;
            int quantity = 3;

            // Act
            double total = calculator.calculateTotal(price, quantity);

            // Assert
            assertEquals(300.0, total);
        }

        @Test
        @DisplayName("should return 0 when quantity is 0")
        void shouldReturnZero_WhenQuantityIsZero() {
            assertEquals(0.0, calculator.calculateTotal(100.0, 0));
        }

        @Test
        @DisplayName("should throw exception when price is negative")
        void shouldThrowException_WhenPriceIsNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> calculator.calculateTotal(-10.0, 1));
        }

        @Test
        @DisplayName("should throw exception when quantity is negative")
        void shouldThrowException_WhenQuantityIsNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> calculator.calculateTotal(100.0, -1));
        }
    }

    // ═══════════════════════════════════════════
    // applyDiscount()
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("applyDiscount(total, discountPercent)")
    class ApplyDiscount {

        @Test
        @DisplayName("should apply 10% discount")
        void shouldApply10PercentDiscount() {
            // Arrange
            double total = 1000.0;
            double discountPercent = 10.0;

            // Act
            double result = calculator.applyDiscount(total, discountPercent);

            // Assert
            assertEquals(900.0, result);
        }

        @Test
        @DisplayName("should apply 0% discount (no change)")
        void shouldApplyZeroDiscount() {
            assertEquals(500.0, calculator.applyDiscount(500.0, 0.0));
        }

        @Test
        @DisplayName("should apply 100% discount (free)")
        void shouldApply100PercentDiscount() {
            assertEquals(0.0, calculator.applyDiscount(500.0, 100.0));
        }

        @Test
        @DisplayName("should throw exception when discount > 100%")
        void shouldThrowException_WhenDiscountOver100() {
            assertThrows(IllegalArgumentException.class,
                    () -> calculator.applyDiscount(100.0, 150.0));
        }

        @Test
        @DisplayName("should throw exception when discount is negative")
        void shouldThrowException_WhenDiscountNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> calculator.applyDiscount(100.0, -5.0));
        }
    }

    // ═══════════════════════════════════════════
    // calculateWithTax()
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("calculateWithTax(total, taxPercent)")
    class CalculateWithTax {

        @Test
        @DisplayName("should add 20% VAT")
        void shouldAdd20PercentVAT() {
            // Arrange
            double total = 1000.0;
            double taxPercent = 20.0;

            // Act
            double result = calculator.calculateWithTax(total, taxPercent);

            // Assert
            assertEquals(1200.0, result);
        }

        @Test
        @DisplayName("should add 0% tax (no change)")
        void shouldAddZeroTax() {
            assertEquals(500.0, calculator.calculateWithTax(500.0, 0.0));
        }

        @Test
        @DisplayName("should throw exception when tax is negative")
        void shouldThrowException_WhenTaxNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> calculator.calculateWithTax(100.0, -10.0));
        }
    }
}
