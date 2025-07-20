package com.ssafy.tosspay.common.exception;

public class PaymentException extends DomainException {

    public PaymentException(String message) {
        super(message, "PAYMENT_ERROR");
    }

    public PaymentException(String message, String errorCode) {
        super(message, errorCode);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, "PAYMENT_ERROR", cause);
    }

    public PaymentException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}