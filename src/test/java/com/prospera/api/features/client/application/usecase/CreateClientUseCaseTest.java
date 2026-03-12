package com.prospera.api.features.client.application.usecase;

import com.prospera.api.features.client.domain.enums.ClientError;
import com.prospera.api.features.client.domain.model.Client;
import com.prospera.api.features.client.domain.repository.ClientRepository;
import com.prospera.api.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private CreateClientUseCase useCase;

    @Test
    void shouldCreateClientWhenCommandIsValid() {
        final CreateClientCommand command = new CreateClientCommand(
                "Example",
                true,
                "https://callback.example.com",
                "description"
        );

        final Client saved = Client.builder()
                .id(UUID.randomUUID())
                .name(command.name())
                .active(command.active())
                .callbackUrl(command.callbackUrl())
                .description(command.description())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        when(repository.findByName(command.name())).thenReturn(Optional.empty());
        when(repository.save(any(Client.class))).thenReturn(saved);

        final var result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(command.name(), result.name());
        assertTrue(result.active());
        verify(repository).save(any(Client.class));
    }

    @Test
    void shouldThrowExceptionWhenClientAlreadyExists() {
        final CreateClientCommand command = new CreateClientCommand(
                "Example",
                false,
                null,
                null
        );

        when(repository.findByName(command.name())).thenReturn(Optional.of(mock(Client.class)));

        final BusinessException exception =
                assertThrows(BusinessException.class, () -> useCase.execute(command));

        assertEquals(ClientError.ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(repository, never()).save(any());
    }
}
