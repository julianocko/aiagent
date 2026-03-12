package com.prospera.api.features.client.application.usecase;

public record UpdateClientCommand(
        String name,
        boolean active,
        String callbackUrl,
        String description
) {
}
