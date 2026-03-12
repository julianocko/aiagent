package com.prospera.api.shared.response;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ResponseFactory {

    public <T> ApiResponse<T> success(final T data) {
        return new ApiResponse<>(data, baseMeta());
    }

    public <T> ApiResponse<T> success(
            final T data,
            final int page,
            final int size,
            final long totalElements,
            final int totalPages
    ) {
        final Pagination pagination = new Pagination(page, size, totalElements, totalPages);
        return new ApiResponse<>(data, baseMeta(pagination));
    }

    private Meta baseMeta() {
        return baseMeta(null);
    }

    private Meta baseMeta(final Pagination pagination) {
        return new Meta(
                UUID.randomUUID().toString(),
                OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                pagination
        );
    }
}
