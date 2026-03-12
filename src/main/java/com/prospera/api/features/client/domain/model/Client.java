package com.prospera.api.features.client.domain.model;

import com.prospera.api.features.client.domain.enums.ClientError;
import com.prospera.api.shared.exception.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Client {

    private final UUID id;
    private String name;
    private boolean active;
    private String callbackUrl;
    private String description;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static Client create(
            final String name,
            final boolean active,
            final String callbackUrl,
            final String description,
            final OffsetDateTime createdAt,
            final OffsetDateTime updatedAt
    ) {
        final Client entity = Client.builder()
                .id(UUID.randomUUID())
                .name(name)
                .active(active)
                .callbackUrl(callbackUrl)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        entity.validateState();
        return entity;
    }

    public void activate() {
        if (this.active) {
            throw new BusinessException(ClientError.ALREADY_ACTIVE);
        }

        validateCallbackUrlForActiveState(this.callbackUrl);
        this.active = true;
        touch();
    }

    public void deactivate() {
        if (!this.active) {
            throw new BusinessException(ClientError.ALREADY_INACTIVE);
        }

        this.active = false;
        touch();
    }

    public void update(
            final String name,
            final String callbackUrl,
            final String description
    ) {
        validateName(name);

        this.name = name.trim();
        this.callbackUrl = normalize(callbackUrl);
        this.description = normalize(description);

        validateState();
        touch();
    }

    public void applyPatch(
            final String name,
            final Boolean active,
            final String callbackUrl,
            final String description
    ) {
        if (name != null) {
            validateName(name);
            this.name = name.trim();
        }
        if (callbackUrl != null) {
            this.callbackUrl = normalize(callbackUrl);
        }
        if (description != null) {
            this.description = normalize(description);
        }
        if (active != null) {
            if (active) {
                validateCallbackUrlForActiveState(this.callbackUrl);
            }
            this.active = active;
        }
        validateState();
        touch();
    }

    private void validateState() {
        validateName(this.name);

        if (this.active) {
            validateCallbackUrlForActiveState(this.callbackUrl);
        }
    }

    private void validateName(final String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ClientError.NAME_REQUIRED);
        }
    }

    private void validateCallbackUrlForActiveState(final String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ClientError.CALLBACK_URL_REQUIRED_FOR_ACTIVE);
        }
    }

    private String normalize(final String value) {
        return Objects.isNull(value) ? null : value.trim();
    }

    private void touch() {
        this.updatedAt = OffsetDateTime.now();
    }
}
