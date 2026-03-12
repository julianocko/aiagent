package com.prospera.api.features.client.application.usecase;

import com.prospera.api.features.client.application.aggregate.ClientAggregate;
import com.prospera.api.features.client.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchClientByNameUseCase {

    private final ClientRepository repository;

    public Page<ClientAggregate> execute(final String name, final int page, final int size, final String sort) {
        final Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        return repository.searchByName(name, pageable).map(entity -> ClientAggregate.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .callbackUrl(entity.getCallbackUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build());
    }

    private Sort parseSort(final String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "name");
        }
        final String[] parts = sort.split(",");
        if (parts.length == 2) {
            return Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
        }
        return Sort.by(Sort.Direction.ASC, sort);
    }
}
