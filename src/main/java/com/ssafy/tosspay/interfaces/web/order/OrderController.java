package com.ssafy.tosspay.interfaces.web.order;

import com.ssafy.tosspay.application.order.dto.CreateOrderRequest;
import com.ssafy.tosspay.application.order.dto.CreateOrderResponse;
import com.ssafy.tosspay.application.order.dto.OrderDto;
import com.ssafy.tosspay.application.order.service.OrderApplicationService;
import com.ssafy.tosspay.interfaces.web.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    /**
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        try {
            CreateOrderResponse response = orderApplicationService.createOrder(request);
            return ResponseEntity.ok(ApiResponse.success(response, "주문이 생성되었습니다."));
        } catch (Exception e) {
            log.error("주문 생성 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), "ORDER_CREATE_FAILED"));
        }
    }

    /**
     * 주문 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(@PathVariable String orderId) {
        try {
            var order = orderApplicationService.getOrder(orderId);
            OrderDto response = OrderDto.from(order);

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("주문 조회 실패: orderId={}, error={}", orderId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), "ORDER_NOT_FOUND"));
        }
    }

    /**
     * 주문 목록 조회 (추가 기능)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<String>> getOrderList() {
        // 실제 구현에서는 페이징 처리된 주문 목록을 반환
        return ResponseEntity.ok(ApiResponse.success("주문 목록 조회 기능은 추후 구현 예정입니다."));
    }
}