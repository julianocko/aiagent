package com.prospera.api.features.client.interfaces.rest.assembler;

import com.prospera.api.features.client.application.aggregate.ClientAggregate;
import com.prospera.api.features.client.domain.model.Client;
import com.prospera.api.shared.response.ApiResponse;
import com.prospera.api.shared.response.ResponseFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ClientAssembler {

    public ClientAggregate toAggregate(final Client entity) {
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

    public ApiResponse<ClientAggregate> toResponse(
            final Client entity,
            final ResponseFactory responseFactory
    ) {
        return responseFactory.success(toAggregate(entity));
    }

    public ApiResponse<Iterable<ClientAggregate>> toPagedResponse(
            final Page<Client> page,
            final ResponseFactory responseFactory
    ) {
        return responseFactory.success(
                page.map(this::toAggregate).getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
