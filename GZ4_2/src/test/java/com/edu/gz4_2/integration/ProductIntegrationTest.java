package com.edu.gz4_2.integration;

import com.edu.gz4_2.entity.Product;
import com.edu.gz4_2.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST — тестуємо ВСІ шари разом: Controller → Service → Repository → H2 DB.
 *
 * @SpringBootTest — піднімає ПОВНИЙ Spring Context (всі біни, БД, etc.)
 * @AutoConfigureMockMvc — додає MockMvc для HTTP-запитів без реального сервера
 *
 * Тут НЕ використовуємо Mockito — все реальне:
 *   - Реальний ProductService
 *   - Реальний ProductRepository
 *   - Реальна H2 база даних
 *
 * AAA Pattern:
 *   Arrange — заповнюємо БД тестовими даними
 *   Act     — робимо HTTP-запит через MockMvc
 *   Assert  — перевіряємо HTTP відповідь + стан БД
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Product API — Integration Tests (@SpringBootTest + H2)")
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("POST + GET — full create and retrieve flow")
    void shouldCreateAndRetrieveProduct() throws Exception {
        // Arrange & Act — створюємо продукт через API
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "MacBook Pro",
                                    "price": 65000.0,
                                    "category": "Electronics"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("MacBook Pro")));

        // Assert — перевіряємо що продукт збережений в БД
        assertEquals(1, productRepository.count());

        Product saved = productRepository.findAll().get(0);
        assertEquals("MacBook Pro", saved.getName());
        assertEquals(65000.0, saved.getPrice());

        // Act — отримуємо через GET
        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("MacBook Pro")))
                .andExpect(jsonPath("$.category", is("Electronics")));
    }

    @Test
    @DisplayName("GET /api/products — should return all products from DB")
    void shouldReturnAllProducts() throws Exception {
        // Arrange — додаємо продукти напряму в БД
        productRepository.save(Product.builder().name("Laptop").price(25000.0).category("Electronics").build());
        productRepository.save(Product.builder().name("Book").price(350.0).category("Books").build());
        productRepository.save(Product.builder().name("Phone").price(15000.0).category("Electronics").build());

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[2].name", is("Phone")));
    }

    @Test
    @DisplayName("GET /api/products/category/{category} — should filter by category")
    void shouldFilterByCategory() throws Exception {
        // Arrange
        productRepository.save(Product.builder().name("Laptop").price(25000.0).category("Electronics").build());
        productRepository.save(Product.builder().name("Book").price(350.0).category("Books").build());
        productRepository.save(Product.builder().name("Phone").price(15000.0).category("Electronics").build());

        // Act & Assert
        mockMvc.perform(get("/api/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[1].name", is("Phone")));
    }

    @Test
    @DisplayName("PUT /api/products/{id}/price — should update price in DB")
    void shouldUpdatePrice() throws Exception {
        // Arrange
        Product product = productRepository.save(
                Product.builder().name("Laptop").price(25000.0).category("Electronics").build());

        // Act
        mockMvc.perform(put("/api/products/" + product.getId() + "/price")
                        .param("price", "30000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(30000.0)));

        // Assert — перевіряємо що ціна змінилась в БД
        Product updated = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(30000.0, updated.getPrice());
    }

    @Test
    @DisplayName("DELETE /api/products/{id} — should remove from DB")
    void shouldDeleteProduct() throws Exception {
        // Arrange
        Product product = productRepository.save(
                Product.builder().name("Old Product").price(100.0).build());
        assertEquals(1, productRepository.count());

        // Act
        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());

        // Assert
        assertEquals(0, productRepository.count());
    }

    @Test
    @DisplayName("GET /api/products/{id}/discount — full discount calculation with DB")
    void shouldCalculateDiscount() throws Exception {
        // Arrange
        Product product = productRepository.save(
                Product.builder().name("Expensive Laptop").price(20000.0).build());

        // Act & Assert — 15% discount for price > 5000
        mockMvc.perform(get("/api/products/" + product.getId() + "/discount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product", is("Expensive Laptop")))
                .andExpect(jsonPath("$.originalPrice", is(20000.0)))
                .andExpect(jsonPath("$.discount", is(3000.0)))
                .andExpect(jsonPath("$.finalPrice", is(17000.0)));
    }
}
