package com.prospera.api.features.client.infrastructure.persistence.repository;

import com.prospera.api.features.client.domain.model.Client;
import com.prospera.api.features.client.domain.repository.ClientRepository;
import com.prospera.api.features.client.infrastructure.persistence.entity.ClientJpaEntity;
import com.prospera.api.features.client.infrastructure.persistence.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientJpaRepository jpaRepository;
    private final ClientMapper mapper;

    @Override
    public Optional<Client> findById(final UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Client> findByName(final String name) {
        return jpaRepository.findByNameIgnoreCase(name).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNameExcludingId(final String name, final UUID excludeId) {
        return jpaRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId);
    }

    @Override
    public Page<Client> findAll(final Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Client> searchByName(final String name, final Pageable pageable) {
        return jpaRepository.findByNameContainingIgnoreCase(name, pageable).map(mapper::toDomain);
    }

    @Override
    public Client save(final Client entity) {
        final ClientJpaEntity saved = jpaRepository.save(mapper.toJpaEntity(entity));
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(final Client entity) {
        jpaRepository.delete(mapper.toJpaEntity(entity));
    }
}
