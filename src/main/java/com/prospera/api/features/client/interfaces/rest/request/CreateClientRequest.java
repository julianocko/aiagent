package com.prospera.api.features.client.interfaces.rest.request;

import com.prospera.api.features.client.application.usecase.CreateClientCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateClientRequest(
        @NotBlank
        @Size(min = 3, max = 255)
        String name,
        boolean active,
        @Size(max = 512)
        @URL
        String callbackUrl,
        @Size(max = 1024)
        String description
) {
    public CreateClientCommand toCommand() {
        return new CreateClientCommand(name, active, callbackUrl, description);
    }
}
