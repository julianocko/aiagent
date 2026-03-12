package ${basePackage}.features.${feature}.domain.repository;

import ${basePackage}.features.${feature}.domain.model.${Entity};
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ${Entity}Repository {

    Optional<${Entity}> findById(UUID id);

    Optional<${Entity}> findByName(String name);

    boolean existsByName(String name);

    Page<${Entity}> findAll(Pageable pageable);

    ${Entity} save(${Entity} entity);

    void delete(${Entity} entity);
}
