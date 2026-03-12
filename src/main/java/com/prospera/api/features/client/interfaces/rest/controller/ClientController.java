package com.prospera.api.features.client.interfaces.rest.controller;

import com.prospera.api.features.client.application.aggregate.ClientAggregate;
import com.prospera.api.features.client.application.usecase.*;
import com.prospera.api.features.client.interfaces.rest.request.CreateClientRequest;
import com.prospera.api.features.client.interfaces.rest.request.PatchClientRequest;
import com.prospera.api.features.client.interfaces.rest.request.UpdateClientRequest;
import com.prospera.api.shared.response.ApiResponse;
import com.prospera.api.shared.response.ResponseFactory;
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
@RequestMapping("/clients")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final PatchClientUseCase patchClientUseCase;
    private final FindClientByIdUseCase findClientByIdUseCase;
    private final ListClientsUseCase listClientsUseCase;
    private final DeleteClientUseCase deleteClientUseCase;
    private final ActivateClientUseCase activateClientUseCase;
    private final DeactivateClientUseCase deactivateClientUseCase;
    private final SearchClientByNameUseCase searchClientByNameUseCase;
    private final ResponseFactory responseFactory;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_client.write')")
    public ResponseEntity<ApiResponse<ClientAggregate>> create(
            @Valid @RequestBody final CreateClientRequest request
    ) {
        final ClientAggregate aggregate = createClientUseCase.execute(request.toCommand());
        return ResponseEntity
                .created(URI.create("/clients/" + aggregate.id()))
                .body(responseFactory.success(aggregate));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_client.read')")
    public ResponseEntity<ApiResponse<ClientAggregate>> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(responseFactory.success(findClientByIdUseCase.execute(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_client.read')")
    public ResponseEntity<ApiResponse<Iterable<ClientAggregate>>> list(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size,
            @RequestParam(defaultValue = "name,asc") final String sort
    ) {
        final Page<ClientAggregate> result = listClientsUseCase.execute(page, size, sort);
        return ResponseEntity.ok(responseFactory.success(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        ));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_client.read')")
    public ResponseEntity<ApiResponse<Iterable<ClientAggregate>>> search(
            @RequestParam final String name,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int size,
            @RequestParam(defaultValue = "name,asc") final String sort
    ) {
        final Page<ClientAggregate> result = searchClientByNameUseCase.execute(name, page, size, sort);
        return ResponseEntity.ok(responseFactory.success(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_client.write')")
    public ResponseEntity<ApiResponse<ClientAggregate>> update(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdateClientRequest request
    ) {
        return ResponseEntity.ok(responseFactory.success(
                updateClientUseCase.execute(id, request.toCommand())
        ));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_client.write')")
    public ResponseEntity<ApiResponse<ClientAggregate>> patch(
            @PathVariable final UUID id,
            @Valid @RequestBody final PatchClientRequest request
    ) {
        return ResponseEntity.ok(responseFactory.success(
                patchClientUseCase.execute(id, request.toCommand())
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_client.write')")
    public ResponseEntity<Void> delete(@PathVariable final UUID id) {
        deleteClientUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SCOPE_client.write')")
    public ResponseEntity<ApiResponse<ClientAggregate>> activate(@PathVariable final UUID id) {
        return ResponseEntity.ok(responseFactory.success(activateClientUseCase.execute(id)));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SCOPE_client.write')")
    public ResponseEntity<ApiResponse<ClientAggregate>> deactivate(@PathVariable final UUID id) {
        return ResponseEntity.ok(responseFactory.success(deactivateClientUseCase.execute(id)));
    }
}
