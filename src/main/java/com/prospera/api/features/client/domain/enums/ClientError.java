package com.prospera.api.features.client.domain.enums;

import com.prospera.api.shared.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ClientError implements ErrorCode {
    ALREADY_EXISTS("CLIENT_ALREADY_EXISTS", "Client with the same name already exists", HttpStatus.CONFLICT),
    ALREADY_ACTIVE("CLIENT_ALREADY_ACTIVE", "Client is already active", HttpStatus.BAD_REQUEST),
    ALREADY_INACTIVE("CLIENT_ALREADY_INACTIVE", "Client is already inactive", HttpStatus.BAD_REQUEST),
    CALLBACK_URL_REQUIRED_FOR_ACTIVE("CALLBACK_URL_REQUIRED_FOR_ACTIVE", "Callback URL must be provided when client is active", HttpStatus.BAD_REQUEST),
    NAME_REQUIRED("CLIENT_NAME_REQUIRED", "Client name is required", HttpStatus.BAD_REQUEST),
    NOT_FOUND("CLIENT_NOT_FOUND", "Client not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ClientError(final String code, final String message, final HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
