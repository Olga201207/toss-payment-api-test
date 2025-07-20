package com.ssafy.tosspay.infrastructure.persistence.payment;

import com.ssafy.tosspay.domain.order.vo.Money;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import com.ssafy.tosspay.domain.payment.entity.Payment;
import com.ssafy.tosspay.domain.payment.vo.PaymentKey;
import com.ssafy.tosspay.domain.payment.vo.PaymentMethod;
import com.ssafy.tosspay.domain.payment.vo.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_key", unique = true)
    private String paymentKey;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method;

    @Column(name = "requested_at")
    private String requestedAt;

    @Column(name = "approved_at")
    private String approvedAt;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PaymentJpaEntity(String paymentKey, String orderId, String orderName,
                            BigDecimal amount, String customerName, PaymentStatus status,
                            PaymentMethod method, String requestedAt, String approvedAt,
                            String receiptUrl) {
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
    }

    public static PaymentJpaEntity from(Payment payment) {
        return new PaymentJpaEntity(
                payment.getPaymentKey() != null ? payment.getPaymentKey().getValue() : null,
                payment.getOrderId().getValue(),
                payment.getOrderName(),
                payment.getAmount().getAmount(),
                payment.getCustomerName(),
                payment.getStatus(),
                payment.getMethod(),
                payment.getRequestedAt(),
                payment.getApprovedAt(),
                payment.getReceiptUrl()
        );
    }

    public Payment toDomain() {
        return new Payment(
                this.paymentKey != null ? new PaymentKey(this.paymentKey) : null,
                new OrderId(this.orderId),
                this.orderName,
                new Money(this.amount),
                this.customerName,
                this.status,
                this.method,
                this.requestedAt,
                this.approvedAt,
                this.receiptUrl,
                this.createdAt,
                this.updatedAt
        );
    }

    public void updateFrom(Payment payment) {
        this.paymentKey = payment.getPaymentKey() != null ? payment.getPaymentKey().getValue() : null;
        this.orderName = payment.getOrderName();
        this.amount = payment.getAmount().getAmount();
        this.customerName = payment.getCustomerName();
        this.status = payment.getStatus();
        this.method = payment.getMethod();
        this.requestedAt = payment.getRequestedAt();
        this.approvedAt = payment.getApprovedAt();
        this.receiptUrl = payment.getReceiptUrl();
    }
}