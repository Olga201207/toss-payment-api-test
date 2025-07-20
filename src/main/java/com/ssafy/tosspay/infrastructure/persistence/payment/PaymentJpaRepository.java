package com.ssafy.tosspay.infrastructure.persistence.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, Long> {
    Optional<PaymentJpaEntity> findByOrderId(String orderId);
    Optional<PaymentJpaEntity> findByPaymentKey(String paymentKey);
}