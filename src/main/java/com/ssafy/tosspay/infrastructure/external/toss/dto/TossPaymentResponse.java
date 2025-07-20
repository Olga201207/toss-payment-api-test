package com.ssafy.tosspay.infrastructure.external.toss.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private BigDecimal amount;
    private String status;
    private String method;
    private String requestedAt;
    private String approvedAt;
    private Receipt receipt;

    @Data
    public static class Receipt {
        private String url;
    }
}