package com.prospera.api.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(final BusinessException ex) {
        final ErrorCode errorCode = ex.getErrorCode();
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), errorCode.getMessage());
        enrich(problem, errorCode.getCode());
        return ResponseEntity.status(errorCode.getStatus()).body(problem);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(final NotFoundException ex) {
        final ErrorCode errorCode = ex.getErrorCode();
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), errorCode.getMessage());
        enrich(problem, errorCode.getCode());
        return ResponseEntity.status(errorCode.getStatus()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(final Exception ex) {
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        enrich(problem, "UNEXPECTED_ERROR");
        return ResponseEntity.internalServerError().body(problem);
    }

    private void enrich(final ProblemDetail problemDetail, final String code) {
        problemDetail.setProperty("code", code);
        problemDetail.setProperty("requestId", UUID.randomUUID().toString());
        problemDetail.setProperty("timestamp", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}
