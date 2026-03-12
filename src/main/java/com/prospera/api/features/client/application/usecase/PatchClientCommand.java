package com.prospera.api.features.client.application.usecase;

import java.util.Optional;

public record PatchClientCommand(
        Optional<String> name,
        Optional<Boolean> active,
        Optional<String> callbackUrl,
        Optional<String> description
) {
}
