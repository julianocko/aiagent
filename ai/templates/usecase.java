package ${basePackage}.features.${feature}.application.usecase;

import ${basePackage}.features.${feature}.application.aggregate.${Entity}Aggregate;
import ${basePackage}.features.${feature}.domain.enums.${Entity}Error;
import ${basePackage}.features.${feature}.domain.model.${Entity};
import ${basePackage}.features.${feature}.domain.repository.${Entity}Repository;
import ${basePackage}.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class Create${Entity}UseCase {

    private final ${Entity}Repository repository;

    public ${Entity}Aggregate execute(final Create${Entity}Command command) {
        repository.findByName(command.name()).ifPresent(existing -> {
            throw new BusinessException(${Entity}Error.ALREADY_EXISTS);
        });

        final ${Entity} entity = ${Entity}.create(
                command.name(),
                command.active(),
                command.callbackUrl(),
                command.description(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        final ${Entity} saved = repository.save(entity);

        return ${Entity}Aggregate.builder()
                .id(saved.getId())
                .name(saved.getName())
                .active(saved.isActive())
                .callbackUrl(saved.getCallbackUrl())
                .description(saved.getDescription())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}
