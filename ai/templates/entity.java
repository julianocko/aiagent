package ${basePackage}.features.${feature}.domain.model;

import ${basePackage}.features.${feature}.domain.enums.${Entity}Error;
import ${basePackage}.shared.exception.BusinessException;
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
public class ${Entity} {

    private final UUID id;
    private String name;
    private boolean active;
    private String callbackUrl;
    private String description;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static ${Entity} create(
            final String name,
            final boolean active,
            final String callbackUrl,
            final String description,
            final OffsetDateTime createdAt,
            final OffsetDateTime updatedAt
    ) {
        final ${Entity} entity = ${Entity}.builder()
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
            throw new BusinessException(${Entity}Error.ALREADY_ACTIVE);
        }

        validateCallbackUrlForActiveState(this.callbackUrl);
        this.active = true;
        touch();
    }

    public void deactivate() {
        if (!this.active) {
            throw new BusinessException(${Entity}Error.ALREADY_INACTIVE);
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

    public void updateCallbackUrl(final String callbackUrl) {
        final String normalized = normalize(callbackUrl);
        validateCallbackUrlForActiveState(normalized);
        this.callbackUrl = normalized;
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
            throw new BusinessException(${Entity}Error.NAME_REQUIRED);
        }
    }

    private void validateCallbackUrlForActiveState(final String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(${Entity}Error.CALLBACK_URL_REQUIRED_FOR_ACTIVE);
        }
    }

    private String normalize(final String value) {
        return Objects.isNull(value) ? null : value.trim();
    }

    private void touch() {
        this.updatedAt = OffsetDateTime.now();
    }
}
