package com.ssafy.tosspay.infrastructure.persistence.payment;

import com.ssafy.tosspay.domain.order.vo.OrderId;
import com.ssafy.tosspay.domain.payment.entity.Payment;
import com.ssafy.tosspay.domain.payment.repository.PaymentRepository;
import com.ssafy.tosspay.domain.payment.vo.PaymentKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        Optional<PaymentJpaEntity> existingEntity =
                paymentJpaRepository.findByOrderId(payment.getOrderId().getValue());

        if (existingEntity.isPresent()) {
            PaymentJpaEntity entity = existingEntity.get();
            entity.updateFrom(payment);
            return paymentJpaRepository.save(entity).toDomain();
        } else {
            PaymentJpaEntity entity = PaymentJpaEntity.from(payment);
            return paymentJpaRepository.save(entity).toDomain();
        }
    }

    @Override
    public Optional<Payment> findByOrderId(OrderId orderId) {
        return paymentJpaRepository.findByOrderId(orderId.getValue())
                .map(PaymentJpaEntity::toDomain);
    }

    @Override
    public Optional<Payment> findByPaymentKey(PaymentKey paymentKey) {
        return paymentJpaRepository.findByPaymentKey(paymentKey.getValue())
                .map(PaymentJpaEntity::toDomain);
    }

    @Override
    public void delete(Payment payment) {
        paymentJpaRepository.findByOrderId(payment.getOrderId().getValue())
                .ifPresent(paymentJpaRepository::delete);
    }
}