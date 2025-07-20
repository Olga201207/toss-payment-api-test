package com.ssafy.tosspay.domain.payment.entity;

import com.ssafy.tosspay.domain.order.vo.Money;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import com.ssafy.tosspay.domain.payment.vo.PaymentKey;
import com.ssafy.tosspay.domain.payment.vo.PaymentMethod;
import com.ssafy.tosspay.domain.payment.vo.PaymentStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    private PaymentKey paymentKey;
    private OrderId orderId;
    private String orderName;
    private Money amount;
    private String customerName;
    private PaymentStatus status;
    private PaymentMethod method;
    private String requestedAt;
    private String approvedAt;
    private String receiptUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Payment(OrderId orderId, String orderName, Money amount, String customerName) {
        this.orderId = orderId;
        this.orderName = validateOrderName(orderName);
        this.amount = amount;
        this.customerName = validateCustomerName(customerName);
        this.status = PaymentStatus.READY;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 재구성을 위한 생성자
    public Payment(PaymentKey paymentKey, OrderId orderId, String orderName, Money amount,
                   String customerName, PaymentStatus status, PaymentMethod method,
                   String requestedAt, String approvedAt, String receiptUrl,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.amount = amount;
        this.customerName = customerName;
        this.status = status;
        this.method = method;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.receiptUrl = receiptUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void approve(PaymentKey paymentKey, PaymentMethod method,
                        String requestedAt, String approvedAt, String receiptUrl) {
        if (this.status != PaymentStatus.READY) {
            throw new IllegalStateException("준비 상태의 결제만 승인할 수 있습니다.");
        }

        this.paymentKey = paymentKey;
        this.method = method;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.receiptUrl = receiptUrl;
        this.status = PaymentStatus.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        if (!this.status.canCancel()) {
            throw new IllegalStateException("취소할 수 없는 결제 상태입니다.");
        }

        this.status = PaymentStatus.CANCELED;
        this.updatedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = PaymentStatus.ABORTED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isApproved() {
        return this.status == PaymentStatus.DONE;
    }

    private String validateOrderName(String orderName) {
        if (orderName == null || orderName.trim().isEmpty()) {
            throw new IllegalArgumentException("주문명은 필수입니다.");
        }
        return orderName;
    }

    private String validateCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("고객명은 필수입니다.");
        }
        return customerName;
    }
}
