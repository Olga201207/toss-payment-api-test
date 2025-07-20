package com.ssafy.tosspay.domain.order.service;

import com.ssafy.tosspay.domain.order.entity.Order;
import com.ssafy.tosspay.domain.order.repository.OrderRepository;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;

    public void validateOrderExists(OrderId orderId) {
        if (!orderRepository.findById(orderId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다: " + orderId.getValue());
        }
    }

    public boolean isOrderCompletable(Order order) {
        return order.getStatus() == Order.OrderStatus.PENDING;
    }
}