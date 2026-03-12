package com.prospera.api.features.client.application.aggregate;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ClientAggregate(
        UUID id,
        String name,
        boolean active,
        String callbackUrl,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
