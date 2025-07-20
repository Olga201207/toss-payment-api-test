package com.ssafy.tosspay.application.order.service;

import com.ssafy.tosspay.application.order.dto.CreateOrderRequest;
import com.ssafy.tosspay.application.order.dto.CreateOrderResponse;
import com.ssafy.tosspay.domain.order.entity.Order;
import com.ssafy.tosspay.domain.order.entity.OrderItem;
import com.ssafy.tosspay.domain.order.repository.OrderRepository;
import com.ssafy.tosspay.domain.order.service.OrderDomainService;
import com.ssafy.tosspay.domain.order.vo.CustomerInfo;
import com.ssafy.tosspay.domain.order.vo.Money;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        CustomerInfo customerInfo = new CustomerInfo(
                request.getCustomerName(),
                request.getCustomerEmail()
        );

        // 단일 상품 주문 (실제 환경에서는 여러 상품 처리)
        OrderItem orderItem = new OrderItem(
                request.getOrderName(),
                new Money(request.getAmount()),
                1
        );

        Order order = new Order(
                request.getOrderName(),
                customerInfo,
                List.of(orderItem)
        );

        Order savedOrder = orderRepository.save(order);

        log.info("주문 생성 완료: orderId={}", savedOrder.getOrderId().getValue());

        return CreateOrderResponse.builder()
                .orderId(savedOrder.getOrderId().getValue())
                .orderName(savedOrder.getOrderName())
                .amount(savedOrder.getTotalAmount().getAmount())
                .customerName(savedOrder.getCustomerInfo().getName())
                .build();
    }

    @Transactional(readOnly = true)
    public Order getOrder(String orderId) {
        return orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다: " + orderId));
    }

    public void completeOrder(String orderId) {
        Order order = getOrder(orderId);
        order.complete();
        orderRepository.save(order);
        log.info("주문 완료: orderId={}", orderId);
    }

    public void failOrder(String orderId) {
        Order order = getOrder(orderId);
        order.fail();
        orderRepository.save(order);
        log.info("주문 실패: orderId={}", orderId);
    }
}