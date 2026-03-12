package ${basePackage}.features.${feature}.interfaces.rest.controller;

import ${basePackage}.features.${feature}.application.aggregate.${Entity}Aggregate;
import ${basePackage}.features.${feature}.application.usecase.Create${Entity}UseCase;
import ${basePackage}.features.${feature}.application.usecase.Delete${Entity}UseCase;
import ${basePackage}.features.${feature}.application.usecase.Find${Entity}ByIdUseCase;
import ${basePackage}.features.${feature}.application.usecase.List${Entities}UseCase;
import ${basePackage}.features.${feature}.application.usecase.Update${Entity}UseCase;
import ${basePackage}.features.${feature}.interfaces.rest.assembler.${Entity}Assembler;
import ${basePackage}.features.${feature}.interfaces.rest.request.Create${Entity}Request;
import ${basePackage}.features.${feature}.interfaces.rest.request.Update${Entity}Request;
import ${basePackage}.shared.response.ApiResponse;
import ${basePackage}.shared.response.ResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/${entities}")
public class ${Entity}Controller {

    private final Create${Entity}UseCase create${Entity}UseCase;
    private final Update${Entity}UseCase update${Entity}UseCase;
    private final Find${Entity}ByIdUseCase find${Entity}ByIdUseCase;
    private final List${Entities}UseCase list${Entities}UseCase;
    private final Delete${Entity}UseCase delete${Entity}UseCase;
    private final ${Entity}Assembler ${entity}Assembler;
    private final ResponseFactory responseFactory;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_${feature}.write')")
    public ResponseEntity<ApiResponse<${Entity}Aggregate>> create(
            @Valid @RequestBody final Create${Entity}Request request
    ) {
        final ${Entity}Aggregate aggregate = create${Entity}UseCase.execute(request.toCommand());
        return ResponseEntity
                .created(URI.create("/${entities}/" + aggregate.id()))
                .body(responseFactory.success(aggregate));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_${feature}.read')")
    public ResponseEntity<ApiResponse<${Entity}Aggregate>> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(responseFactory.success(find${Entity}ByIdUseCase.execute(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_${feature}.read')")
    public ResponseEntity<ApiResponse<Iterable<${Entity}Aggregate>>> list(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size,
            @RequestParam(defaultValue = "name,asc") final String sort
    ) {
        final Page<${Entity}Aggregate> result = list${Entities}UseCase.execute(page, size, sort);
        return ResponseEntity.ok(responseFactory.success(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_${feature}.write')")
    public ResponseEntity<ApiResponse<${Entity}Aggregate>> update(
            @PathVariable final UUID id,
            @Valid @RequestBody final Update${Entity}Request request
    ) {
        return ResponseEntity.ok(responseFactory.success(
                update${Entity}UseCase.execute(id, request.toCommand())
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_${feature}.write')")
    public ResponseEntity<Void> delete(@PathVariable final UUID id) {
        delete${Entity}UseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
