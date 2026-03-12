package com.prospera.api.features.client.application.usecase;

public record CreateClientCommand(
        String name,
        boolean active,
        String callbackUrl,
        String description
) {
}
