package com.cheongcel.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 비회원 주문도 가능하게 email로 관리
    @Column(nullable = false)
    private String customerEmail;

    private String customerName;
    private String phoneNumber;
    private String address;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    // 결제 연동 시 사용 (토스페이먼츠 등)
    private String paymentKey;
    private String paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum OrderStatus {
        PENDING,    // 주문 대기
        PAID,       // 결제 완료
        SHIPPING,   // 배송 중
        DELIVERED,  // 배송 완료
        CANCELLED   // 취소
    }
}
