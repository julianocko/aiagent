package com.prospera.api.shared.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Meta(
        String requestId,
        String timestamp,
        Pagination pagination
) {
}
