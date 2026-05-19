package com.edu.gz4_2.service;

import com.edu.gz4_2.entity.Product;
import com.edu.gz4_2.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getAffordableProducts(Double maxPrice) {
        return productRepository.findByPriceLessThan(maxPrice);
    }

    public Product updatePrice(Long id, Double newPrice) {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        Product product = getProductById(id);
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Бізнес-логіка: розрахунок знижки
     * - Якщо ціна > 1000 → 10% знижка
     * - Якщо ціна > 5000 → 15% знижка
     * - Інакше → без знижки
     */
    public Double calculateDiscount(Long productId) {
        Product product = getProductById(productId);
        double price = product.getPrice();

        if (price > 5000) {
            return price * 0.15;
        } else if (price > 1000) {
            return price * 0.10;
        }
        return 0.0;
    }
}
