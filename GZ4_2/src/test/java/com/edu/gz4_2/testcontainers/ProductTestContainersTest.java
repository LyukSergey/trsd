package com.edu.gz4_2.testcontainers;

import com.edu.gz4_2.entity.Product;
import com.edu.gz4_2.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRATION TEST з TestContainers — реальна PostgreSQL в Docker.
 *
 * @Testcontainers — автоматично запускає/зупиняє Docker контейнер
 * @Container — оголошує PostgreSQL контейнер
 * @DynamicPropertySource — підставляє URL/credentials від контейнера в Spring
 *
 * Навіщо TestContainers замість H2?
 *   - H2 ≠ PostgreSQL (різний SQL діалект, різні типи даних)
 *   - H2 може пропустити баг, який виникне на реальній БД
 *   - TestContainers запускає СПРАВЖНЮ PostgreSQL → тести максимально наближені до production
 *
 * ВИМОГА: Docker повинен бути запущений!
 */
@SpringBootTest
@Testcontainers
@DisplayName("Product — TestContainers (Real PostgreSQL)")
class ProductTestContainersTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("should save and retrieve product from real PostgreSQL")
    void shouldSaveAndRetrieve() {
        // Arrange
        productRepository.deleteAll();
        Product product = Product.builder()
                .name("Laptop")
                .price(25000.0)
                .category("Electronics")
                .build();

        // Act
        Product saved = productRepository.save(product);
        Product found = productRepository.findById(saved.getId()).orElseThrow();

        // Assert
        assertEquals("Laptop", found.getName());
        assertEquals(25000.0, found.getPrice());
        assertEquals("Electronics", found.getCategory());
    }

    @Test
    @DisplayName("should filter by category in real PostgreSQL")
    void shouldFilterByCategory() {
        // Arrange
        productRepository.deleteAll();
        productRepository.save(Product.builder().name("Laptop").price(25000.0).category("Electronics").build());
        productRepository.save(Product.builder().name("Book").price(350.0).category("Books").build());
        productRepository.save(Product.builder().name("Phone").price(15000.0).category("Electronics").build());

        // Act
        List<Product> electronics = productRepository.findByCategory("Electronics");

        // Assert
        assertEquals(2, electronics.size());
        assertTrue(electronics.stream().allMatch(p -> p.getCategory().equals("Electronics")));
    }

    @Test
    @DisplayName("should find products cheaper than given price")
    void shouldFindAffordableProducts() {
        // Arrange
        productRepository.deleteAll();
        productRepository.save(Product.builder().name("Laptop").price(25000.0).category("Electronics").build());
        productRepository.save(Product.builder().name("Book").price(350.0).category("Books").build());
        productRepository.save(Product.builder().name("Cable").price(150.0).category("Electronics").build());

        // Act
        List<Product> affordable = productRepository.findByPriceLessThan(1000.0);

        // Assert
        assertEquals(2, affordable.size());
        assertTrue(affordable.stream().allMatch(p -> p.getPrice() < 1000.0));
    }
}
