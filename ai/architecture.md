# architecture.md
## REST API Architecture Standard
Version: 2.0

This document defines the architectural standards used by the AI development agent
for generating REST APIs using Java 25 and Spring Boot.

The goal is to ensure consistency, scalability, maintainability, and security across
all generated services.

---

# 1. Core Principles

All generated code MUST follow these principles:

- Clean Architecture
- Domain Driven Design (DDD)
- SOLID principles
- Clean Code
- Test Driven Development (TDD)
- Feature-based modular architecture

Layers must remain independent and respect dependency rules.

Allowed dependency flow:

interfaces -> application -> domain
infrastructure -> domain/application

Domain must NEVER depend on framework code.

---

# 2. Project Structure

The project MUST be organized by feature modules.

Example:

src/main/java/com/company/project/

    features/
        client/
            domain/
            application/
            interfaces/rest/
            infrastructure/persistence/
            infrastructure/config/
            infrastructure/security/

Shared modules:

    common/
        exception/
        response/
        security/
        config/

---

# 3. Technology Stack

The generated project MUST use:

- Java 25
- Spring Boot
- PostgreSQL 17.5
- Flyway
- YAML only
- Maven
- Keycloak OAuth2
- JUnit 5 + Mockito
- JaCoCo

---

# 4. Dependency Rules

## Domain
Contains:
- entities
- value objects
- domain services
- domain exceptions
- enums
- repository interfaces

## Application
Contains:
- use cases
- command/query objects
- orchestration logic

## Interfaces
Contains:
- REST controllers
- request validation
- response formatting

## Infrastructure
Contains:
- database adapters
- JPA entities
- repository implementations
- external integrations
- security configuration

Controllers must NEVER expose domain entities.
Use aggregates/DTOs instead.

---

# 5. Entity Design

Entities must represent business concepts.

Guidelines:
- use UUID as primary identifier
- avoid anemic models when business logic exists
- encapsulate invariants inside domain methods
- avoid exposing setters

Example audit fields:
- createdAt
- updatedAt

---

# 6. Lombok Usage

Lombok MUST be used to reduce boilerplate.

Recommended annotations:
- @Getter
- @Builder
- @AllArgsConstructor
- @NoArgsConstructor when required

Avoid using @Data in rich domain entities.
Prefer explicit control over mutability.

---

# 7. Enums for Error Messages

All exception messages MUST come from enums.

Example:

enum ClientErrors {
    CLIENT_NOT_FOUND("Client not found"),
    CLIENT_ALREADY_EXISTS("Client already exists");

    private final String message;
}

No hardcoded error strings are allowed in services or controllers.

---

# 8. Security (Keycloak)

Authentication MUST be implemented using Keycloak.

Standard flow:
- OAuth2 Client Credentials
- Private Key JWT
- RS256

The API acts as a Resource Server.

Every request must contain:
Authorization: Bearer <token>

Required claims validation:
- issuer
- audience
- expiration
- client_id

Endpoints must enforce authorization using scopes/roles.

Examples:
- SCOPE_client.read
- SCOPE_client.write

---

# 9. REST API Design

The API MUST follow REST conventions.

Standard endpoints:
- POST /resource
- GET /resource/{id}
- PUT /resource/{id}
- PATCH /resource/{id}
- DELETE /resource/{id}
- GET /resource (paginated)

HTTP codes:
- 201 Created
- 200 OK
- 204 No Content
- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found
- 409 Conflict
- 422 Unprocessable Entity

POST responses MUST include Location header.

---

# 10. Response Standard

All successful responses MUST follow the envelope format.

Success:

{
  "data": {},
  "meta": {
    "requestId": "uuid",
    "timestamp": "2026-01-01T12:00:00Z"
  }
}

Paginated responses:

{
  "data": [],
  "meta": {
    "requestId": "uuid",
    "timestamp": "2026-01-01T12:00:00Z",
    "pagination": {
      "page": 1,
      "size": 20,
      "totalElements": 100,
      "totalPages": 5
    }
  }
}

---

# 11. Error Responses

Errors MUST follow RFC7807 Problem Details.

Content-Type:
application/problem+json

Example:

{
  "type": "https://api.example.com/errors/client-not-found",
  "title": "Client not found",
  "status": 404,
  "detail": "The requested client does not exist",
  "instance": "/clients/123",
  "requestId": "uuid",
  "timestamp": "2026-01-01T12:00:00Z"
}

Validation errors may include:

"errors": [
  {"field": "name", "message": "must not be blank"}
]

---

# 12. Persistence

Database: PostgreSQL only.

Flyway migrations MUST follow semantic versioning:

- V1.0.0__create_client_table.sql
- V1.1.0__add_client_status.sql

Flyway schema history table MUST be located in:
public.flyway_schema_history

All migrations must be forward-only.

---

# 13. Repository Pattern

Domain defines repository interfaces.
Infrastructure implements them using Spring Data JPA.

Examples:
- ClientRepository (domain)
- ClientRepositoryImpl (infrastructure)

Repositories must support when applicable:
- save
- findById
- delete
- pagination queries

---

# 14. Assemblers and Mappers

Assemblers convert:
Domain -> Response DTO

Mappers convert:
Persistence Entity <-> Domain

Controllers must never perform manual mapping.

---

# 15. Use Cases

Each business operation must have its own Use Case class.

Examples:
- CreateClientUseCase
- UpdateClientUseCase
- DeleteClientUseCase
- FindClientUseCase

Use cases orchestrate domain logic but do not contain persistence details.

---

# 16. Testing

Testing must include:
- unit tests for domain services
- unit tests for use cases
- controller tests
- integration tests when necessary

Coverage requirements:
- line coverage >= 90%
- branch coverage >= 80%

Build must fail if coverage thresholds are not met.

---

# 17. API Documentation

The API must expose OpenAPI documentation automatically.

Tool:
springdoc-openapi

Endpoints:
- /v3/api-docs
- /swagger-ui.html

---

# 18. Validation

Request validation must use Jakarta Validation.

Examples:
- @NotNull
- @NotBlank
- @Email
- @Size

Validation errors must return Problem Details.

---

# 19. Configuration

All configuration must be written in YAML.

Examples:
- application.yaml
- application-dev.yaml
- application-prod.yaml

No .properties files are allowed.

---

# 20. Agent Compliance

All code generated by the AI agent must comply with:
- architecture.md
- enums guidelines
- lombok guidelines
- keycloak security guidelines
- response contract

If a conflict occurs, architecture.md is the primary source of truth.
