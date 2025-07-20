package com.ssafy.tosspay.application.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentConfirmRequest {

    @NotBlank(message = "결제키는 필수입니다")
    private String paymentKey;

    @NotBlank(message = "주문ID는 필수입니다")
    private String orderId;

    @NotNull(message = "금액은 필수입니다")
    @Positive(message = "금액은 양수여야 합니다")
    private BigDecimal amount;
}