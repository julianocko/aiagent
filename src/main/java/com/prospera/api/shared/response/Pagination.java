package com.prospera.api.shared.response;

public record Pagination(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
