package com.ssafy.tosspay.infrastructure.persistence.order;

import com.ssafy.tosspay.domain.order.entity.Order;
import com.ssafy.tosspay.domain.order.repository.OrderRepository;
import com.ssafy.tosspay.domain.order.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        Optional<OrderJpaEntity> existingEntity = orderJpaRepository.findByOrderId(order.getOrderId().getValue());

        if (existingEntity.isPresent()) {
            OrderJpaEntity entity = existingEntity.get();
            entity.updateFrom(order);
            return orderJpaRepository.save(entity).toDomain();
        } else {
            OrderJpaEntity entity = OrderJpaEntity.from(order);
            return orderJpaRepository.save(entity).toDomain();
        }
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findByOrderId(orderId.getValue())
                .map(OrderJpaEntity::toDomain);
    }

    @Override
    public void delete(Order order) {
        orderJpaRepository.findByOrderId(order.getOrderId().getValue())
                .ifPresent(orderJpaRepository::delete);
    }
}