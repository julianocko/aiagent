package ${basePackage}.features.${feature}.infrastructure.persistence.mapper;

import ${basePackage}.features.${feature}.domain.model.${Entity};
import ${basePackage}.features.${feature}.infrastructure.persistence.entity.${Entity}JpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ${Entity}Mapper {

    ${Entity} toDomain(${Entity}JpaEntity entity);

    ${Entity}JpaEntity toJpaEntity(${Entity} entity);
}
