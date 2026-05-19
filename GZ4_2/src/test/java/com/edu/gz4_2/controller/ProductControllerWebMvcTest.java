package com.edu.gz4_2.controller;

import com.edu.gz4_2.entity.Product;
import com.edu.gz4_2.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST (Web Layer) — тестуємо Controller + Spring MVC.
 *
 * @WebMvcTest — піднімає ТІЛЬКИ web-шар (Controller, Filters, ExceptionHandlers).
 *   НЕ піднімає: Service, Repository, Database.
 *   ProductService замінений на @MockitoBean.
 *
 * MockMvc — виконує HTTP-запити БЕЗ запуску реального сервера (in-memory).
 *
 * Що тестуємо:
 *   - HTTP статус коди (200, 201, 404)
 *   - JSON response body
 *   - Request mapping (@GetMapping, @PostMapping)
 *   - Content-Type
 */
@WebMvcTest(ProductController.class)
@DisplayName("ProductController — @WebMvcTest")
class ProductControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("GET /api/products — should return list of products")
    void getAllProducts_shouldReturnList() throws Exception {
        // Arrange
        List<Product> products = List.of(
                Product.builder().id(1L).name("Laptop").price(25000.0).category("Electronics").build(),
                Product.builder().id(2L).name("Book").price(350.0).category("Books").build()
        );
        when(productService.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[0].price", is(25000.0)))
                .andExpect(jsonPath("$[1].name", is("Book")));
    }

    @Test
    @DisplayName("GET /api/products/{id} — should return product by id")
    void getById_shouldReturnProduct() throws Exception {
        // Arrange
        Product product = Product.builder()
                .id(1L).name("Laptop").price(25000.0).category("Electronics").build();
        when(productService.getProductById(1L)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(25000.0)))
                .andExpect(jsonPath("$.category", is("Electronics")));
    }

    @Test
    @DisplayName("GET /api/products/{id} — should return 404 when not found")
    void getById_shouldReturn404_WhenNotFound() throws Exception {
        // Arrange
        when(productService.getProductById(999L))
                .thenThrow(new RuntimeException("Product not found: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Product not found: 999")));
    }

    @Test
    @DisplayName("POST /api/products — should create product and return 201")
    void createProduct_shouldReturn201() throws Exception {
        // Arrange
        Product saved = Product.builder()
                .id(1L).name("Phone").price(15000.0).category("Electronics").build();
        when(productService.createProduct(any(Product.class))).thenReturn(saved);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Phone",
                                    "price": 15000.0,
                                    "category": "Electronics"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Phone")))
                .andExpect(jsonPath("$.price", is(15000.0)));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} — should return 204 No Content")
    void deleteProduct_shouldReturn204() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/products/{id}/discount — should return discount info")
    void getDiscount_shouldReturnDiscountInfo() throws Exception {
        // Arrange
        Product product = Product.builder()
                .id(1L).name("Laptop").price(20000.0).build();
        when(productService.getProductById(1L)).thenReturn(product);
        when(productService.calculateDiscount(1L)).thenReturn(3000.0);

        // Act & Assert
        mockMvc.perform(get("/api/products/1/discount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product", is("Laptop")))
                .andExpect(jsonPath("$.originalPrice", is(20000.0)))
                .andExpect(jsonPath("$.discount", is(3000.0)))
                .andExpect(jsonPath("$.finalPrice", is(17000.0)));
    }
}
