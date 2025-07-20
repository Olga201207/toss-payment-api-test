package com.ssafy.tosspay.domain.payment.repository;

import com.ssafy.tosspay.domain.order.vo.OrderId;
import com.ssafy.tosspay.domain.payment.entity.Payment;
import com.ssafy.tosspay.domain.payment.vo.PaymentKey;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(OrderId orderId);
    Optional<Payment> findByPaymentKey(PaymentKey paymentKey);
    void delete(Payment payment);
}