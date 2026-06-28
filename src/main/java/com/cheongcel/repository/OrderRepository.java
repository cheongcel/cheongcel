package com.cheongcel.repository;

import com.cheongcel.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerEmailOrderByCreatedAtDesc(String email);
    Optional<Order> findByPaymentKey(String paymentKey);
}
