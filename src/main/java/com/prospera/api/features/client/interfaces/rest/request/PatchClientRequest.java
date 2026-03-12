package com.prospera.api.features.client.interfaces.rest.request;

import com.prospera.api.features.client.application.usecase.PatchClientCommand;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.Optional;

public record PatchClientRequest(
        @Size(min = 3, max = 255)
        String name,
        Boolean active,
        @Size(max = 512)
        @URL
        String callbackUrl,
        @Size(max = 1024)
        String description
) {
    public PatchClientCommand toCommand() {
        return new PatchClientCommand(
                Optional.ofNullable(name),
                Optional.ofNullable(active),
                Optional.ofNullable(callbackUrl),
                Optional.ofNullable(description)
        );
    }
}
