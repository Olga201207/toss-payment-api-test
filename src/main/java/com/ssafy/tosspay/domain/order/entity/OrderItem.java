package com.ssafy.tosspay.domain.order.entity;

import com.ssafy.tosspay.domain.order.vo.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    private String itemName;
    private Money unitPrice;
    private int quantity;
    private Money totalPrice;

    public OrderItem(String itemName, Money unitPrice, int quantity) {
        this.itemName = validateItemName(itemName);
        this.unitPrice = unitPrice;
        this.quantity = validateQuantity(quantity);
        this.totalPrice = unitPrice.multiply(quantity);
    }

    private String validateItemName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        return itemName;
    }

    private int validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        return quantity;
    }
}