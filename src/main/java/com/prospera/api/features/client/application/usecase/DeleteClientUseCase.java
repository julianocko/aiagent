package com.prospera.api.features.client.application.usecase;

import com.prospera.api.features.client.domain.enums.ClientError;
import com.prospera.api.features.client.domain.model.Client;
import com.prospera.api.features.client.domain.repository.ClientRepository;
import com.prospera.api.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteClientUseCase {

    private final ClientRepository repository;

    public void execute(final UUID id) {
        final Client client = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ClientError.NOT_FOUND));
        repository.delete(client);
    }
}
