package com.ssafy.tosspay.application.order.dto;

import com.ssafy.tosspay.domain.order.entity.Order;
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
public class OrderDto {
    private String orderId;
    private String orderName;
    private BigDecimal totalAmount;
    private String customerName;
    private String customerEmail;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId().getValue())
                .orderName(order.getOrderName())
                .totalAmount(order.getTotalAmount().getAmount())
                .customerName(order.getCustomerInfo().getName())
                .customerEmail(order.getCustomerInfo().getEmail())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}