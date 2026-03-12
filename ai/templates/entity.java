package ${basePackage}.features.${feature}.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.OffsetDateTime;
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
        return ${Entity}.builder()
                .id(UUID.randomUUID())
                .name(name)
                .active(active)
                .callbackUrl(callbackUrl)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
