package com.ssafy.tosspay.common.exception;

import com.ssafy.tosspay.interfaces.web.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ApiResponse<Void>> handlePaymentException(PaymentException e) {
        log.error("Payment error: {}", e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(DomainException e) {
        log.error("Domain error: {}", e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiResponse<Void>> handleValidationException(Exception e) {
        String message;
        if (e instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) e).getBindingResult()
                    .getAllErrors().get(0).getDefaultMessage();
        } else {
            message = ((BindException) e).getBindingResult()
                    .getAllErrors().get(0).getDefaultMessage();
        }

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message, "VALIDATION_ERROR"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument: {}", e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage(), "INVALID_ARGUMENT"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("내부 서버 오류가 발생했습니다.", "INTERNAL_SERVER_ERROR"));
    }
}