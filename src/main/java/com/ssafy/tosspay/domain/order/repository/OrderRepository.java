package com.ssafy.tosspay.domain.order.repository;

import com.ssafy.tosspay.domain.order.entity.Order;
import com.ssafy.tosspay.domain.order.vo.OrderId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(OrderId orderId);
    void delete(Order order);
}