package com.prospera.api.features.client.infrastructure.persistence.repository;

import com.prospera.api.features.client.infrastructure.persistence.entity.ClientJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<ClientJpaEntity, UUID> {

    Optional<ClientJpaEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);

    Page<ClientJpaEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
