package com.cheongcel.service;

import com.cheongcel.domain.Product;
import com.cheongcel.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts(String category) {
        if (category != null && !category.isBlank()) {
            return productRepository.findByCategoryAndPublishedTrue(category);
        }
        return productRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    public List<Product> getFeaturedProducts(int limit) {
        return productRepository.findByPublishedTrueOrderByCreatedAtDesc()
                .stream().limit(limit).toList();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
