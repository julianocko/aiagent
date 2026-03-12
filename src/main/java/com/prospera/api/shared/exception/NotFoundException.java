package com.prospera.api.shared.exception;

public class NotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
