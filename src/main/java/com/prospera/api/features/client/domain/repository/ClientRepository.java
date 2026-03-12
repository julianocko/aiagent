package com.prospera.api.features.client.domain.repository;

import com.prospera.api.features.client.domain.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    Optional<Client> findById(UUID id);

    Optional<Client> findByName(String name);

    boolean existsByNameExcludingId(String name, UUID excludeId);

    Page<Client> findAll(Pageable pageable);

    Page<Client> searchByName(String name, Pageable pageable);

    Client save(Client entity);

    void delete(Client entity);
}
