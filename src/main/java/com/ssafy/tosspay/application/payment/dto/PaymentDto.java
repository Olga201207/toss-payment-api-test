package com.ssafy.tosspay.application.payment.dto;

import com.ssafy.tosspay.domain.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private BigDecimal amount;
    private String customerName;
    private String status;
    private String method;
    private String requestedAt;
    private String approvedAt;
    private String receiptUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentDto from(Payment payment) {
        return PaymentDto.builder()
                .paymentKey(payment.getPaymentKey() != null ? payment.getPaymentKey().getValue() : null)
                .orderId(payment.getOrderId().getValue())
                .orderName(payment.getOrderName())
                .amount(payment.getAmount().getAmount())
                .customerName(payment.getCustomerName())
                .status(payment.getStatus().name())
                .method(payment.getMethod() != null ? payment.getMethod().name() : null)
                .requestedAt(payment.getRequestedAt())
                .approvedAt(payment.getApprovedAt())
                .receiptUrl(payment.getReceiptUrl())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
