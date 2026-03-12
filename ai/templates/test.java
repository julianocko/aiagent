package ${basePackage}.features.${feature}.application.usecase;

import ${basePackage}.features.${feature}.domain.enums.${Entity}Error;
import ${basePackage}.features.${feature}.domain.model.${Entity};
import ${basePackage}.features.${feature}.domain.repository.${Entity}Repository;
import ${basePackage}.shared.exception.BusinessException;
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
class Create${Entity}UseCaseTest {

    @Mock
    private ${Entity}Repository repository;

    @InjectMocks
    private Create${Entity}UseCase useCase;

    @Test
    void shouldCreate${Entity}WhenCommandIsValid() {
        final Create${Entity}Command command = new Create${Entity}Command(
                "Example",
                true,
                "https://callback.example.com",
                "description"
        );

        final ${Entity} saved = ${Entity}.builder()
                .id(UUID.randomUUID())
                .name(command.name())
                .active(command.active())
                .callbackUrl(command.callbackUrl())
                .description(command.description())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        when(repository.findByName(command.name())).thenReturn(Optional.empty());
        when(repository.save(any(${Entity}.class))).thenReturn(saved);

        final var result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(command.name(), result.name());
        assertTrue(result.active());
        verify(repository).save(any(${Entity}.class));
    }

    @Test
    void shouldThrowExceptionWhen${Entity}AlreadyExists() {
        final Create${Entity}Command command = new Create${Entity}Command(
                "Example",
                false,
                null,
                null
        );

        when(repository.findByName(command.name())).thenReturn(Optional.of(mock(${Entity}.class)));

        final BusinessException exception =
                assertThrows(BusinessException.class, () -> useCase.execute(command));

        assertEquals(${Entity}Error.ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(repository, never()).save(any());
    }
}
