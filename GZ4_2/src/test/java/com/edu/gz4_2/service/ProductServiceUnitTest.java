package com.edu.gz4_2.service;

import com.edu.gz4_2.entity.Product;
import com.edu.gz4_2.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST — тестуємо ProductService ІЗОЛЬОВАНО від БД.
 *
 * @Mock — створює фейковий ProductRepository (не ходить у БД)
 * @InjectMocks — створює ProductService і підставляє Mock замість реального Repository
 * @ExtendWith(MockitoExtension.class) — підключає Mockito до JUnit 5
 *
 * AAA Pattern:
 *   Arrange — підготовка даних і налаштування mock
 *   Act     — виклик методу, який тестуємо
 *   Assert  — перевірка результату
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService — Unit Tests (Mockito)")
class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // ═══════════════════════════════════════════
    // createProduct
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("createProduct()")
    class CreateProduct {

        @Test
        @DisplayName("should save product when data is valid")
        void shouldSaveProduct_WhenDataIsValid() {
            // Given
            Product product = Product.builder()
                    .name("Laptop")
                    .price(25000.0)
                    .category("Electronics")
                    .build();

            when(productRepository.save(any(Product.class)))
                    .thenReturn(Product.builder()
                            .id(1L)
                            .name("Laptop")
                            .price(25000.0)
                            .category("Electronics")
                            .build());

            // When
            Product result = productService.createProduct(product);

            // Then
            assertNotNull(result.getId());
            assertEquals("Laptop", result.getName());
            assertEquals(25000.0, result.getPrice());
            verify(productRepository, times(1)).save(eq(product));
        }

        @Test
        @DisplayName("should throw exception when name is empty")
        void shouldThrowException_WhenNameIsEmpty() {
            // Arrange
            Product product = Product.builder()
                    .name("")
                    .price(100.0)
                    .build();

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.createProduct(product)
            );
            assertEquals("Product name cannot be empty", exception.getMessage());
            verify(productRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw exception when name is null")
        void shouldThrowException_WhenNameIsNull() {
            // Arrange
            Product product = Product.builder()
                    .price(100.0)
                    .build();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productService.createProduct(product));
            verify(productRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw exception when price is negative")
        void shouldThrowException_WhenPriceIsNegative() {
            // Arrange
            Product product = Product.builder()
                    .name("Test")
                    .price(-50.0)
                    .build();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productService.createProduct(product));
            verify(productRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw exception when price is zero")
        void shouldThrowException_WhenPriceIsZero() {
            // Arrange
            Product product = Product.builder()
                    .name("Test")
                    .price(0.0)
                    .build();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productService.createProduct(product));
        }
    }

    // ═══════════════════════════════════════════
    // getProductById
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("getProductById()")
    class GetProductById {

        @Test
        @DisplayName("should return product when found")
        void shouldReturnProduct_WhenFound() {
            // Arrange
            Product product = Product.builder()
                    .id(1L)
                    .name("Phone")
                    .price(15000.0)
                    .build();
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            // Act
            Product result = productService.getProductById(1L);

            // Assert
            assertEquals("Phone", result.getName());
            assertEquals(15000.0, result.getPrice());
        }

        @Test
        @DisplayName("should throw exception when not found")
        void shouldThrowException_WhenNotFound() {
            // Arrange
            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> productService.getProductById(999L)
            );
            assertTrue(exception.getMessage().contains("999"));
        }
    }

    // ═══════════════════════════════════════════
    // calculateDiscount — бізнес-логіка
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("calculateDiscount()")
    class CalculateDiscount {

        @Test
        @DisplayName("should return 0 discount when price <= 1000")
        void shouldReturnZeroDiscount_WhenPriceLow() {
            // Arrange
            Product product = Product.builder().id(1L).name("Cable").price(500.0).build();
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            // Act
            Double discount = productService.calculateDiscount(1L);

            // Assert
            assertEquals(0.0, discount);
        }

        @Test
        @DisplayName("should return 10% discount when price > 1000")
        void shouldReturn10PercentDiscount_WhenPriceMedium() {
            // Arrange
            Product product = Product.builder().id(1L).name("Monitor").price(3000.0).build();
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            // Act
            Double discount = productService.calculateDiscount(1L);

            // Assert
            assertEquals(300.0, discount); // 3000 * 0.10
        }

        @Test
        @DisplayName("should return 15% discount when price > 5000")
        void shouldReturn15PercentDiscount_WhenPriceHigh() {
            // Arrange
            Product product = Product.builder().id(1L).name("Laptop").price(20000.0).build();
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            // Act
            Double discount = productService.calculateDiscount(1L);

            // Assert
            assertEquals(3000.0, discount); // 20000 * 0.15
        }
    }

    // ═══════════════════════════════════════════
    // updatePrice
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("updatePrice()")
    class UpdatePrice {

        @Test
        @DisplayName("should update price when valid")
        void shouldUpdatePrice_WhenValid() {
            // Arrange
            Product product = Product.builder().id(1L).name("Phone").price(10000.0).build();
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Product result = productService.updatePrice(1L, 12000.0);

            // Assert
            assertEquals(12000.0, result.getPrice());
            verify(productRepository).save(product);
        }

        @Test
        @DisplayName("should throw exception when new price is negative")
        void shouldThrowException_WhenNewPriceNegative() {
            // Arrange & Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productService.updatePrice(1L, -100.0));
            verify(productRepository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════
    // deleteProduct
    // ═══════════════════════════════════════════

    @Nested
    @DisplayName("deleteProduct()")
    class DeleteProduct {

        @Test
        @DisplayName("should delete product when exists")
        void shouldDeleteProduct_WhenExists() {
            // Arrange
            when(productRepository.existsById(1L)).thenReturn(true);

            // Act
            productService.deleteProduct(1L);

            // Assert
            verify(productRepository).deleteById(1L);
        }

        @Test
        @DisplayName("should throw exception when product not found")
        void shouldThrowException_WhenNotFound() {
            // Arrange
            when(productRepository.existsById(999L)).thenReturn(false);

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> productService.deleteProduct(999L));
            verify(productRepository, never()).deleteById(any());
        }
    }
}
