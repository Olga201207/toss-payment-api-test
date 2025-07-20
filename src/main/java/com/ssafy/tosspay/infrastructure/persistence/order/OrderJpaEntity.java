package com.ssafy.tosspay.infrastructure.persistence.order;

import com.ssafy.tosspay.domain.order.entity.Order;
import com.ssafy.tosspay.domain.order.vo.CustomerInfo;
import com.ssafy.tosspay.domain.order.vo.Money;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Order.OrderStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public OrderJpaEntity(String orderId, String orderName, BigDecimal totalAmount,
                          String customerName, String customerEmail, Order.OrderStatus status) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.totalAmount = totalAmount;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.status = status;
    }

    public static OrderJpaEntity from(Order order) {
        return new OrderJpaEntity(
                order.getOrderId().getValue(),
                order.getOrderName(),
                order.getTotalAmount().getAmount(),
                order.getCustomerInfo().getName(),
                order.getCustomerInfo().getEmail(),
                order.getStatus()
        );
    }

    public Order toDomain() {
        return new Order(
                new OrderId(this.orderId),
                this.orderName,
                new Money(this.totalAmount),
                new CustomerInfo(this.customerName, this.customerEmail),
                this.status,
                null, // OrderItem은 별도 처리 (간단화를 위해 생략)
                this.createdAt,
                this.updatedAt
        );
    }

    public void updateFrom(Order order) {
        this.orderName = order.getOrderName();
        this.totalAmount = order.getTotalAmount().getAmount();
        this.customerName = order.getCustomerInfo().getName();
        this.customerEmail = order.getCustomerInfo().getEmail();
        this.status = order.getStatus();
    }
}