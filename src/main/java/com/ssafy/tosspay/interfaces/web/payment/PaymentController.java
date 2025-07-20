package com.ssafy.tosspay.interfaces.web.payment;
import com.ssafy.tosspay.application.payment.dto.PaymentConfirmRequest;
import com.ssafy.tosspay.application.payment.dto.PaymentResponse;
import com.ssafy.tosspay.application.payment.service.PaymentApplicationService;
import com.ssafy.tosspay.common.exception.PaymentException;
import com.ssafy.tosspay.interfaces.web.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    /**
     * 결제 승인
     */
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmPayment(
            @Valid @RequestBody PaymentConfirmRequest request) {

        try {
            PaymentResponse response = paymentApplicationService.confirmPayment(request);
            return ResponseEntity.ok(ApiResponse.success(response, "결제가 완료되었습니다."));
        } catch (PaymentException e) {
            log.error("결제 승인 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            log.error("예상치 못한 결제 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("결제 처리 중 오류가 발생했습니다.", "PAYMENT_ERROR"));
        }
    }

    /**
     * 결제 정보 조회
     */
    @GetMapping("/{paymentKey}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable String paymentKey) {

        try {
            PaymentResponse response = paymentApplicationService.getPayment(paymentKey);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("결제 조회 실패: paymentKey={}, error={}", paymentKey, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), "PAYMENT_NOT_FOUND"));
        }
    }

    /**
     * 결제 취소
     */
    @PostMapping("/{paymentKey}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @PathVariable String paymentKey,
            @RequestParam String cancelReason) {

        try {
            PaymentResponse response = paymentApplicationService.cancelPayment(paymentKey, cancelReason);
            return ResponseEntity.ok(ApiResponse.success(response, "결제가 취소되었습니다."));
        } catch (PaymentException e) {
            log.error("결제 취소 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            log.error("예상치 못한 취소 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("결제 취소 중 오류가 발생했습니다.", "CANCEL_ERROR"));
        }
    }

    /**
     * 웹훅 처리 (토스페이먼츠에서 호출)
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        try {
            log.info("웹훅 수신: {}", payload);
            // 웹훅 처리 로직 구현 (필요시)
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("웹훅 처리 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("FAIL");
        }
    }
}