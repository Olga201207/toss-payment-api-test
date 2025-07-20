package com.ssafy.tosspay.infrastructure.external.toss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentConfirmRequest {
    private String paymentKey;
    private String orderId;
    private BigDecimal amount;
}