package ${basePackage}.features.${feature}.interfaces.rest.assembler;

import ${basePackage}.features.${feature}.application.aggregate.${Entity}Aggregate;
import ${basePackage}.features.${feature}.domain.model.${Entity};
import ${basePackage}.shared.response.ApiResponse;
import ${basePackage}.shared.response.ResponseFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ${Entity}Assembler {

    public ${Entity}Aggregate toAggregate(final ${Entity} entity) {
        return ${Entity}Aggregate.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .callbackUrl(entity.getCallbackUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ApiResponse<${Entity}Aggregate> toResponse(
            final ${Entity} entity,
            final ResponseFactory responseFactory
    ) {
        return responseFactory.success(toAggregate(entity));
    }

    public ApiResponse<Iterable<${Entity}Aggregate>> toPagedResponse(
            final Page<${Entity}> page,
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
