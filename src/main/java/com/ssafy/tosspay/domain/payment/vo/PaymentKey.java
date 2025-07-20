package com.ssafy.tosspay.domain.payment.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentKey {

    private String value;

    public PaymentKey(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("결제키는 필수입니다.");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PaymentKey that = (PaymentKey) obj;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}