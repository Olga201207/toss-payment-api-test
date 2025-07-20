package com.ssafy.tosspay.domain.payment.service;

import com.ssafy.tosspay.domain.order.vo.Money;
import com.ssafy.tosspay.domain.payment.entity.Payment;
import com.ssafy.tosspay.domain.payment.repository.PaymentRepository;
import com.ssafy.tosspay.domain.payment.vo.PaymentKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentDomainService {

    private final PaymentRepository paymentRepository;

    public void validatePaymentAmount(Payment payment, Money requestAmount) {
        if (!payment.getAmount().equals(requestAmount)) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }
    }

    public void validatePaymentKeyUnique(PaymentKey paymentKey) {
        if (paymentRepository.findByPaymentKey(paymentKey).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 결제키입니다: " + paymentKey.getValue());
        }
    }
}