# generate-crud.md
## Full CRUD Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate a complete CRUD
for a feature/entity in a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:

- architecture.md
- agent.md
- enums guidelines
- lombok guidelines
- keycloak security guidelines
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate a complete CRUD implementation for a given entity/feature, including:

- domain model
- application layer
- REST interface
- persistence layer
- security integration
- migrations
- tests
- response contract
- exception handling

The generated CRUD must be production-ready, standardized, and architecture-compliant.

---

# 2. Expected Command Format

Examples:

generate crud Client

generate crud Client with attributes:
- name: String required
- active: Boolean required
- callbackUrl: String optional
- description: String optional
- createdAt: OffsetDateTime auto
- updatedAt: OffsetDateTime auto

The agent must infer the feature name from the entity name when not explicitly provided.

Examples:
- Client -> clients
- Company -> companies

---

# 3. Mandatory Outputs

A CRUD generation MUST create, update, or validate all required artifacts below.

## 3.1 Domain layer

Generate:

- domain entity
- repository interface
- domain services by operation when business rules exist
- domain exception classes when required
- error enums for all exception messages
- value objects and enums when appropriate

Examples:

Create<Entity>DomainService  
Update<Entity>DomainService  
Delete<Entity>DomainService  
<Entity>Repository  
<Entity>Error

## 3.2 Application layer

Generate:

- use case per operation
- command/query aggregates as needed
- output aggregates
- paged aggregates for listing

Examples:

Create<Entity>UseCase  
Update<Entity>UseCase  
Delete<Entity>UseCase  
Find<Entity>ByIdUseCase  
List<Entities>UseCase

## 3.3 Interface layer

Generate:

- REST controller
- request models or input aggregates
- response models or output aggregates
- response assembler
- OpenAPI annotations when applicable

## 3.4 Infrastructure layer

Generate:

- repository implementation
- persistence entity when domain and persistence models are separated
- persistence mapper
- Spring Data JPA repository
- Flyway migration
- security configuration hooks when applicable

## 3.5 Tests

Generate at minimum:

- domain/service unit tests
- use case unit tests
- controller/web tests
- mapper/assembler tests when relevant
- repository integration tests when relevant

---

# 4. Required Endpoints

A full CRUD MUST generate these endpoints unless explicitly restricted by the user:

- POST /<resources>
- GET /<resources>/{id}
- GET /<resources>
- PUT /<resources>/{id}
- PATCH /<resources>/{id}
- DELETE /<resources>/{id}

Example for Client:

- POST /clients
- GET /clients/{id}
- GET /clients
- PUT /clients/{id}
- PATCH /clients/{id}
- DELETE /clients/{id}

The list endpoint must support pagination by default.

Optional filters may be generated when clearly inferable from the entity fields.

---

# 5. HTTP Contract

The generated CRUD MUST respect the following HTTP semantics:

- POST -> 201 Created
- GET by id -> 200 OK
- GET list -> 200 OK
- PUT -> 200 OK or 204 No Content, but the project should prefer 200 OK with standardized body
- PATCH -> 200 OK
- DELETE -> 204 No Content

POST responses MUST include:

- Location header
- response body using the success response contract unless the project explicitly defines otherwise

---

# 6. Success Response Contract

All successful responses MUST follow the standard response envelope.

Single resource:

{
  "data": {},
  "meta": {
    "requestId": "uuid",
    "timestamp": "ISO-8601"
  }
}

List resource:

{
  "data": [],
  "meta": {
    "requestId": "uuid",
    "timestamp": "ISO-8601",
    "pagination": {
      "page": 0,
      "size": 20,
      "totalElements": 100,
      "totalPages": 5
    }
  }
}

Controllers must not return raw domain entities.

---

# 7. Error Response Contract

All errors MUST follow RFC7807 Problem Details.

Content-Type:
application/problem+json

Errors must include when applicable:

- type
- title
- status
- detail
- instance
- requestId
- timestamp
- errors (for validation failures)

Validation failures must include field-level errors when available.

---

# 8. Security Rules

All generated endpoints MUST be compatible with Keycloak-based security.

The API must behave as an OAuth2 Resource Server.

Each endpoint must require authorization with scopes or authorities.

Recommended scope pattern:

- SCOPE_<resource>.read
- SCOPE_<resource>.write

Example for Client:

- GET endpoints -> SCOPE_client.read
- POST/PUT/PATCH/DELETE -> SCOPE_client.write

The skill must generate controller-level or method-level authorization annotations according to the project standard.

---

# 9. Domain Rules

The generated domain model MUST follow these rules:

- use UUID as identifier
- encapsulate business invariants
- avoid public setters when possible
- avoid exposing framework annotations in the domain layer
- use value objects and enums when the domain benefits from stronger typing
- keep domain independent from Spring and JPA

Error messages must never be hardcoded in services or controllers.

All domain or business error messages MUST come from enums.

Example:

public enum ClientError {
    CLIENT_NOT_FOUND("Client not found"),
    CLIENT_ALREADY_EXISTS("Client already exists");

    private final String message;
}

---

# 10. Lombok Rules

Use Lombok to reduce boilerplate while preserving model clarity.

Recommended usage:

- @Getter
- @Builder
- @AllArgsConstructor
- @NoArgsConstructor only when required
- @EqualsAndHashCode when useful
- @ToString carefully

Avoid @Data in rich domain entities.

Prefer explicit modeling over excessive magic.

---

# 11. Generated Artifact Conventions

The skill must generate names consistently.

## 11.1 Domain

- <Entity>
- <Entity>Repository
- <Entity>Error
- Create<Entity>DomainService
- Update<Entity>DomainService
- Delete<Entity>DomainService

## 11.2 Application

- Create<Entity>UseCase
- Update<Entity>UseCase
- Patch<Entity>UseCase
- Delete<Entity>UseCase
- Find<Entity>ByIdUseCase
- List<Entities>UseCase

## 11.3 Interface

- <Entity>Controller
- Create<Entity>Request
- Update<Entity>Request
- Patch<Entity>Request
- <Entity>Response
- <Entity>SummaryResponse
- <Entity>ResponseAssembler

## 11.4 Infrastructure

- <Entity>JpaEntity
- SpringData<Entity>Repository
- <Entity>RepositoryImpl
- <Entity>PersistenceMapper

## 11.5 Database

- V<semver>__create_<entity>_table.sql
- V<semver>__alter_<entity>_table.sql

Example:

V1.0.0__create_client_table.sql

---

# 12. Required File Structure

The generated CRUD must respect the feature-based structure.

Example for client:

features/client/
    domain/
        model/
        service/
        repository/
        exception/
        enums/
    application/
        usecase/
        aggregate/
    interfaces/rest/
        controller/
        request/
        response/
        assembler/
    infrastructure/persistence/
        entity/
        repository/
        mapper/
    infrastructure/config/
    infrastructure/security/
    tests/

The exact subfolder names may vary according to architecture.md, but layer separation is mandatory.

---

# 13. Entity Generation Rules

The entity generation must:

- create a UUID identifier
- model user-provided attributes
- support audit fields when requested or required by the architecture
- use enums when values represent a closed set
- keep constructors consistent with invariants
- avoid setter-based mutation unless the design explicitly requires it

When the user includes fields such as createdAt and updatedAt, the generator must decide whether:
- they are part of the domain model, or
- they are persistence/audit metadata exposed through the response model

This decision must remain consistent across the stack.

---

# 14. Request and Response Modeling

The CRUD generation must create specific models for requests and responses.

Recommended request models:

- Create<Entity>Request
- Update<Entity>Request
- Patch<Entity>Request

Recommended response models:

- <Entity>Response
- <Entity>SummaryResponse
- Paged<Entities>Response or envelope with paginated meta

Requests must use Jakarta Validation annotations when appropriate.

Examples:
- @NotNull
- @NotBlank
- @Size
- @Email
- @Pattern

Response models must not expose internal persistence details.

---

# 15. Use Case Rules

Each use case must represent a single business intent.

Examples:

- CreateClientUseCase
- UpdateClientUseCase
- DeleteClientUseCase
- FindClientByIdUseCase
- ListClientsUseCase

Use cases MUST:

- orchestrate domain logic
- interact through repository abstractions
- avoid infrastructure details
- return aggregates or response-oriented output models
- not expose JPA entities
- not contain controller concerns

When domain rules exist, use cases should delegate to domain services.

---

# 16. Domain Service Rules

Domain services should be generated when at least one of these conditions applies:

- business invariants are non-trivial
- the operation requires validation beyond field presence
- there is orchestration involving multiple domain concepts
- there are reusable business rules across operations

The generator must avoid creating meaningless domain services when a direct use case orchestration is sufficient.

Domain services MUST:

- live in the domain layer
- be framework-independent
- throw domain exceptions using error enums

---

# 17. Repository Rules

The domain layer defines the repository contract.

The infrastructure layer implements it.

The generated repository contract should support, when applicable:

- save
- findById
- existsBy...
- deleteById or delete(entity)
- paginated listing
- filtered listing for relevant fields
- uniqueness validation helpers

Repository implementations should use Spring Data JPA.

The generator must separate:

- domain repository abstraction
- Spring Data repository
- repository implementation adapter

when the architecture requires that separation.

---

# 18. Mapping Rules

The CRUD generation must clearly separate responsibilities.

## Mapper
Used for:
- persistence entity <-> domain entity
- internal aggregate conversions when needed

## Assembler
Used for:
- domain or aggregate -> API response representation
- HATEOAS links when the architecture requires them

Controllers must not manually map fields.

---

# 19. Persistence Rules

The generated persistence layer MUST use PostgreSQL only.

Flyway rules:

- semantic versioning is mandatory
- schema history must remain in public.flyway_schema_history
- migrations must be forward-only

The initial migration should create the table with:

- uuid primary key
- required business fields
- timestamps when applicable
- uniqueness constraints when inferable
- indexes when justified

The generator should avoid overengineering but must create obvious constraints such as:
- NOT NULL for required fields
- UNIQUE for strongly implied unique business keys when specified by the user

---

# 20. Controller Rules

The generated controller MUST:

- use @RestController
- define clear request mappings
- receive validated request models
- delegate to use cases
- return standardized ResponseEntity responses
- enforce authorization
- never expose domain entities directly
- integrate with response assembler or response wrapper

The controller should also be prepared for OpenAPI documentation when the project uses springdoc-openapi.

---

# 21. Exception Handling Rules

The CRUD generation must either generate or integrate with global exception handling.

At minimum, the following scenarios must be covered:

- validation error -> 400
- unauthorized -> 401
- forbidden -> 403
- entity not found -> 404
- conflict/duplicate -> 409
- business rule violation -> 422 or project-defined status

All mapped errors must return Problem Details.

---

# 22. Pagination Rules

List endpoints MUST support pagination.

Default conventions should include:

- page
- size
- sort

Response metadata should include:

- page
- size
- totalElements
- totalPages

The actual payload format must follow the standard response envelope.

---

# 23. OpenAPI Rules

When API documentation is part of the project standard, generated CRUD endpoints should include documentation metadata.

At minimum, the generation should support future inclusion of:

- operation summary
- operation description
- response codes
- security requirements

---

# 24. Test Generation Rules

The skill MUST generate tests aligned with the architecture.

## 24.1 Domain tests
Generate tests for:
- invariants
- business rules
- domain service behavior
- domain exceptions

## 24.2 Use case tests
Generate tests for:
- success path
- not found path
- duplicate/conflict path when applicable
- validation/orchestration rules

## 24.3 Controller tests
Generate tests for:
- create success
- get by id success
- list success
- update success
- patch success
- delete success
- validation failure
- unauthorized/forbidden when applicable
- not found handling

## 24.4 Repository tests
Generate integration tests when repository behavior is non-trivial or custom.

The generated tests must contribute to the required JaCoCo thresholds:

- line coverage >= 90%
- branch coverage >= 80%

---

# 25. Generation Decision Rules

When generating a CRUD, the agent must make consistent decisions based on the provided attributes.

Examples:
- boolean active -> suitable for patch/activate/deactivate flows if requested later
- callbackUrl -> validate format and length
- description -> likely optional text field
- createdAt/updatedAt -> audit metadata, usually not writable by API consumers

The generator must prefer sensible defaults that align with enterprise REST API behavior.

---

# 26. Minimum Acceptance Criteria

A generated CRUD is considered valid only if all of the following are true:

- architecture layers are respected
- controller does not expose domain entities
- security scopes are applied
- success responses use the standard envelope
- errors use Problem Details
- exception messages come from enums
- migration follows semantic versioning
- PostgreSQL is the only database target
- tests are generated
- naming conventions are consistent

If any acceptance criterion fails, the agent must correct the generated output.

---

# 27. Non-Goals

This skill does not automatically generate:

- asynchronous messaging flows unless requested
- external integrations unless required by the entity behavior
- batch processing unless requested
- advanced reporting endpoints unless requested

Those concerns should be handled by dedicated skills.

---

# 28. Recommended Next Skills

This CRUD skill works best when combined with:

- generate-feature.md
- generate-endpoint.md
- generate-usecase.md
- generate-domain-service.md
- generate-entity.md
- generate-assembler.md
- generate-test.md
- generate-security.md
- generate-exception-handler.md
- generate-response-wrapper.md

---

# 29. Final Rule

When the user requests "generate crud <Entity>", the agent must not produce only skeleton files.

It must generate a coherent, end-to-end CRUD implementation consistent with the project standards and ready to evolve safely.
