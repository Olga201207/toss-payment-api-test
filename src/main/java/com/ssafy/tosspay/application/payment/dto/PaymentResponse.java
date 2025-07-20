package com.ssafy.tosspay.application.payment.dto;

import com.ssafy.tosspay.domain.payment.entity.Payment;
import com.ssafy.tosspay.infrastructure.external.toss.dto.TossPaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
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

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
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
                .build();
    }

    public static PaymentResponse from(Payment payment, TossPaymentResponse tossResponse) {
        return PaymentResponse.builder()
                .paymentKey(payment.getPaymentKey().getValue())
                .orderId(payment.getOrderId().getValue())
                .orderName(payment.getOrderName())
                .amount(payment.getAmount().getAmount())
                .customerName(payment.getCustomerName())
                .status(payment.getStatus().name())
                .method(payment.getMethod().name())
                .requestedAt(payment.getRequestedAt())
                .approvedAt(payment.getApprovedAt())
                .receiptUrl(payment.getReceiptUrl())
                .build();
    }
}