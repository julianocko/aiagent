package com.prospera.api.features.client.infrastructure.persistence.mapper;

import com.prospera.api.features.client.domain.model.Client;
import com.prospera.api.features.client.infrastructure.persistence.entity.ClientJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toDomain(final ClientJpaEntity entity) {
        return Client.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .callbackUrl(entity.getCallbackUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ClientJpaEntity toJpaEntity(final Client domain) {
        final ClientJpaEntity entity = new ClientJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setActive(domain.isActive());
        entity.setCallbackUrl(domain.getCallbackUrl());
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
