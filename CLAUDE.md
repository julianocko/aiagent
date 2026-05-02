# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=CreateClientUseCaseTest

# Skip tests during build
./mvnw clean package -DskipTests
```

Required environment variables (set in application.yaml via `${VAR}` placeholders):
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` — PostgreSQL connection
- `PORT` — server port
- `CLIENT_ID`, `ISSUER_URI` — Keycloak OAuth2 configuration

## Architecture

The project follows a **feature-sliced DDD** structure under `com.prospera.api`. Each business feature lives in `features/<feature>/` and is divided into four strict layers:

```
features/<feature>/
  domain/           # Pure Java — no Spring, no JPA, no web imports
    model/          # Rich domain entities with embedded business rules
    repository/     # Repository interfaces (domain contracts)
    enums/          # Error enums implementing ErrorCode
  application/
    usecase/        # One class per use case; coordinates domain + repository
    aggregate/      # Read-only records returned from use cases (Lombok @Builder records)
  interfaces/rest/
    controller/     # Spring MVC; delegates to use cases, wraps in ApiResponse
    request/        # Request DTOs with Jakarta Validation + toCommand() method
    assembler/      # Maps domain/aggregate to response representations
  infrastructure/persistence/
    entity/         # JPA entities (@Entity)
    repository/     # Spring Data JPA interfaces + ClientRepositoryImpl
    mapper/         # Maps between JPA entity and domain model
```

Cross-feature shared code lives in `com.prospera.api.shared`:
- `response/` — `ApiResponse<T>`, `Meta`, `Pagination`, `ResponseFactory` (wraps all success responses)
- `exception/` — `BusinessException`, `NotFoundException`, `ErrorCode` interface, `GlobalExceptionHandler`
- `security/` — `SecurityConfig` (OAuth2 resource server, JWT)

## Key Design Rules

**Layer boundaries** — The domain layer must remain framework-free. Use cases must not import controller request/response classes or JPA repositories. Controllers must never expose raw domain objects.

**Error handling** — Every feature defines its own error enum (e.g., `ClientError`) implementing `ErrorCode` with code, message, and HTTP status. Exceptions (`BusinessException`, `NotFoundException`) carry an `ErrorCode`. `GlobalExceptionHandler` converts them to RFC 7807 `ProblemDetail`.

**Response contract** — All successful controller responses go through `ResponseFactory` and return `ApiResponse<T>` with a `meta` block containing `requestId` and `timestamp`. Paginated responses include a `Pagination` wrapper.

**Security** — All endpoints require a valid Keycloak JWT. Per-endpoint authorization uses `@PreAuthorize("hasAuthority('SCOPE_<feature>.read')")` or `SCOPE_<feature>.write`.

**Persistence** — PostgreSQL only. DDL is managed exclusively by Flyway migrations in `src/main/resources/db/migration/` with semantic versioning (`V1.0.0__create_client_table.sql`). The Flyway schema history table stays in the `public` schema; the application schema is `client`.

**Lombok** — Domain models use `@Builder(toBuilder = true)` + `@AllArgsConstructor(access = AccessLevel.PRIVATE)` + `@Getter` + `@EqualsAndHashCode(of = "id")`. Aggregates are records with `@Builder`. Use cases and infrastructure classes use `@RequiredArgsConstructor`.

## AI Agent System

The `ai/` directory contains the specification for an AI code-generation agent. The main entry points are:

- `ai/agent.md` — agent responsibilities and supported commands
- `ai/orchestrator.md` — execution pipeline and dependency ordering between skills
- `ai/architecture.md` — canonical architectural rules (source of truth on conflicts)
- `ai/architecture-validator.md` — post-generation validation checklist
- `ai/template-variables.md` — variable substitution system (`${Entity}`, `${feature}`, etc.)
- `ai/skills/generate-*.md` — individual generation skill specs
- `ai/templates/` — Java and YAML file templates
- `ai/definitions/` — YAML feature definition files consumed by generation commands

Supported agent commands: `generate feature`, `generate crud`, `generate endpoint`, `generate usecase`, `generate entity`, `generate aggregate`, `generate assembler`, `generate repository-impl`, `generate test`, `generate security`, `generate exception-enum`, `generate exception-handler`, `generate response-wrapper`, `generate domain-service`.

When generating new artifacts, consult `ai/orchestrator.md` for the correct execution order and `ai/template-variables.md` for consistent naming across Java, SQL, YAML, and OpenAPI files.
