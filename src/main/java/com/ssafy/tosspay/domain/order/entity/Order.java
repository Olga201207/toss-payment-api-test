package com.ssafy.tosspay.domain.order.entity;

import com.ssafy.tosspay.domain.order.vo.CustomerInfo;
import com.ssafy.tosspay.domain.order.vo.Money;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    private OrderId orderId;
    private String orderName;
    private Money totalAmount;
    private CustomerInfo customerInfo;
    private OrderStatus status;
    private List<OrderItem> orderItems = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(String orderName, CustomerInfo customerInfo, List<OrderItem> orderItems) {
        this.orderId = OrderId.generate();
        this.orderName = validateOrderName(orderName);
        this.customerInfo = customerInfo;
        this.orderItems = new ArrayList<>(orderItems);
        this.totalAmount = calculateTotalAmount();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 재구성을 위한 생성자 (인프라 계층에서 사용)
    public Order(OrderId orderId, String orderName, Money totalAmount,
                 CustomerInfo customerInfo, OrderStatus status,
                 List<OrderItem> orderItems, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.totalAmount = totalAmount;
        this.customerInfo = customerInfo;
        this.status = status;
        this.orderItems = orderItems != null ? new ArrayList<>(orderItems) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void complete() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("대기중인 주문만 완료할 수 있습니다.");
        }
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("완료된 주문은 취소할 수 없습니다.");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = OrderStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    private String validateOrderName(String orderName) {
        if (orderName == null || orderName.trim().isEmpty()) {
            throw new IllegalArgumentException("주문명은 필수입니다.");
        }
        return orderName;
    }

    private Money calculateTotalAmount() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(new Money(BigDecimal.ZERO), Money::add);
    }

    public enum OrderStatus {
        PENDING, COMPLETED, CANCELLED, FAILED
    }
}