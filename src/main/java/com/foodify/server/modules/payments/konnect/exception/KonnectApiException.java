package com.foodify.server.modules.payments.konnect.exception;

public class KonnectApiException extends RuntimeException {
    public KonnectApiException(String message) {
        super(message);
    }

    public KonnectApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
