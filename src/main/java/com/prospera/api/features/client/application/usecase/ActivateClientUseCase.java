package com.prospera.api.features.client.application.usecase;

import com.prospera.api.features.client.application.aggregate.ClientAggregate;
import com.prospera.api.features.client.domain.enums.ClientError;
import com.prospera.api.features.client.domain.model.Client;
import com.prospera.api.features.client.domain.repository.ClientRepository;
import com.prospera.api.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivateClientUseCase {

    private final ClientRepository repository;

    public ClientAggregate execute(final UUID id) {
        final Client client = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ClientError.NOT_FOUND));

        client.activate();
        final Client saved = repository.save(client);
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
