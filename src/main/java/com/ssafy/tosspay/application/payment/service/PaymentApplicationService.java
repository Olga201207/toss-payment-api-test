package com.ssafy.tosspay.application.payment.service;

import com.ssafy.tosspay.application.order.service.OrderApplicationService;
import com.ssafy.tosspay.application.payment.dto.PaymentConfirmRequest;
import com.ssafy.tosspay.application.payment.dto.PaymentResponse;
import com.ssafy.tosspay.common.exception.PaymentException;
import com.ssafy.tosspay.domain.order.entity.Order;
import com.ssafy.tosspay.domain.order.vo.Money;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import com.ssafy.tosspay.domain.payment.entity.Payment;
import com.ssafy.tosspay.domain.payment.repository.PaymentRepository;
import com.ssafy.tosspay.domain.payment.service.PaymentDomainService;
import com.ssafy.tosspay.domain.payment.vo.PaymentKey;
import com.ssafy.tosspay.domain.payment.vo.PaymentMethod;
import com.ssafy.tosspay.infrastructure.external.toss.TossPaymentsApiClient;
import com.ssafy.tosspay.infrastructure.external.toss.dto.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentApplicationService {

    private final PaymentRepository paymentRepository;
    private final OrderApplicationService orderApplicationService;
    private final PaymentDomainService paymentDomainService;
    private final TossPaymentsApiClient tossPaymentsApiClient;

    public PaymentResponse confirmPayment(PaymentConfirmRequest request) {
        try {
            OrderId orderId = new OrderId(request.getOrderId());
            Money requestAmount = new Money(request.getAmount());

            // 주문 정보 조회
            Order order = orderApplicationService.getOrder(request.getOrderId());

            // 결제 정보 조회 또는 생성
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElse(new Payment(
                            orderId,
                            order.getOrderName(),
                            order.getTotalAmount(),
                            order.getCustomerInfo().getName()
                    ));

            // 도메인 검증
            paymentDomainService.validatePaymentAmount(payment, requestAmount);

            // 토스페이먼츠 API 호출
            TossPaymentResponse tossResponse = tossPaymentsApiClient.confirmPayment(
                    request.getPaymentKey(),
                    request.getOrderId(),
                    request.getAmount()
            );

            // 결제 승인 처리
            payment.approve(
                    new PaymentKey(tossResponse.getPaymentKey()),
//                    PaymentMethod.valueOf(tossResponse.getMethod().toUpperCase()),
                    PaymentMethod.fromString(tossResponse.getMethod()),
                    tossResponse.getRequestedAt(),
                    tossResponse.getApprovedAt(),
                    tossResponse.getReceipt() != null ? tossResponse.getReceipt().getUrl() : null
            );

            // 저장
            Payment savedPayment = paymentRepository.save(payment);

            // 주문 완료 처리
            orderApplicationService.completeOrder(request.getOrderId());

            log.info("결제 승인 완료: orderId={}, paymentKey={}",
                    request.getOrderId(), request.getPaymentKey());

            return PaymentResponse.from(savedPayment, tossResponse);

        } catch (Exception e) {
            // 주문 실패 처리
            orderApplicationService.failOrder(request.getOrderId());

            log.error("결제 승인 실패: orderId={}, error={}",
                    request.getOrderId(), e.getMessage());
            throw new PaymentException("결제 승인 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(new PaymentKey(paymentKey))
                .orElseThrow(() -> new PaymentException("결제 정보를 찾을 수 없습니다: " + paymentKey));

        return PaymentResponse.from(payment);
    }

    public PaymentResponse cancelPayment(String paymentKey, String cancelReason) {
        Payment payment = paymentRepository.findByPaymentKey(new PaymentKey(paymentKey))
                .orElseThrow(() -> new PaymentException("결제 정보를 찾을 수 없습니다: " + paymentKey));

        // 토스페이먼츠 취소 API 호출
        TossPaymentResponse tossResponse = tossPaymentsApiClient.cancelPayment(paymentKey, cancelReason);

        // 결제 취소 처리
        payment.cancel(cancelReason);
        Payment savedPayment = paymentRepository.save(payment);

        log.info("결제 취소 완료: paymentKey={}", paymentKey);

        return PaymentResponse.from(savedPayment, tossResponse);
    }
}