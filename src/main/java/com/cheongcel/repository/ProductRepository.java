package com.cheongcel.repository;

import com.cheongcel.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPublishedTrueOrderByCreatedAtDesc();
    List<Product> findByCategoryAndPublishedTrue(String category);
}
