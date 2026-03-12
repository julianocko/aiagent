package com.prospera.api.features.client.application.usecase;

import com.prospera.api.features.client.application.aggregate.ClientAggregate;
import com.prospera.api.features.client.domain.enums.ClientError;
import com.prospera.api.features.client.domain.model.Client;
import com.prospera.api.features.client.domain.repository.ClientRepository;
import com.prospera.api.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CreateClientUseCase {

    private final ClientRepository repository;

    public ClientAggregate execute(final CreateClientCommand command) {
        repository.findByName(command.name()).ifPresent(existing -> {
            throw new BusinessException(ClientError.ALREADY_EXISTS);
        });

        final Client entity = Client.create(
                command.name(),
                command.active(),
                command.callbackUrl(),
                command.description(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        final Client saved = repository.save(entity);
        return toAggregate(saved);
    }

    private ClientAggregate toAggregate(final Client entity) {
        return ClientAggregate.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .callbackUrl(entity.getCallbackUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
