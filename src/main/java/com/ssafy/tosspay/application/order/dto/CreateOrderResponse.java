package com.ssafy.tosspay.application.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    private String orderId;
    private String orderName;
    private BigDecimal amount;
    private String customerName;
}
