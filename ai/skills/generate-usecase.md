# generate-usecase.md
## Use Case Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate an application use case
for a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:

- architecture.md
- agent.md
- generate-crud.md when applicable
- generate-endpoint.md when applicable
- enums guidelines
- lombok guidelines
- keycloak security guidelines
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate a production-ready application use case that represents a single business intent,
respects architectural boundaries, and integrates correctly with the domain, interface,
security, response, and testing standards of the project.

A generated use case must be cohesive, intention-revealing, and safe to evolve.

Examples:

- CreateClientUseCase
- UpdateClientUseCase
- DeleteClientUseCase
- FindClientByIdUseCase
- ListClientsUseCase
- ActivateClientUseCase
- UpdateClientCallbackUrlUseCase
- SearchClientsByNameUseCase

---

# 2. Purpose of the Use Case Layer

The application layer is responsible for orchestrating the execution of business use flows.

A use case MUST:

- represent one business intent
- coordinate domain behavior
- interact through domain abstractions
- remain independent from controller details
- remain independent from persistence implementation details
- return aggregates or application output models
- never expose persistence entities
- never expose controller/request framework concerns

A use case is not a controller, not a repository implementation, and not a domain entity.

---

# 3. Expected Command Format

Examples:

generate usecase create-client

generate usecase update-client-callback-url

generate usecase find-client-by-id

generate usecase list-clients

generate usecase activate-client for feature client

generate usecase search-clients-by-name with input:
- name: String required
- page: Integer optional
- size: Integer optional

When the feature is not explicitly informed, the agent must infer it from the use case intent.

---

# 4. Mandatory Outputs

A generated use case MUST create or update only the artifacts necessary for the business intent,
but it must not under-generate.

The skill may generate or update:

- dedicated use case class
- input aggregate / command / query object
- output aggregate / result object
- domain service dependency when required
- repository abstraction usage
- error enum references
- tests
- interface/controller integration points when the caller requested end-to-end generation
- security implications when the use case maps to a protected endpoint

The core artifact is always the use case itself.

---

# 5. Use Case Classification

The agent must classify the use case before generating it.

## 5.1 Command use case
Used when the use case changes state.

Examples:
- create client
- update client
- delete client
- activate client
- deactivate client
- update callback URL

Typical characteristics:
- writes data
- validates business rules
- may require domain services
- usually mapped to write scopes

## 5.2 Query use case
Used when the use case retrieves data.

Examples:
- find client by id
- list clients
- search clients by name
- list active clients

Typical characteristics:
- reads data
- may support filters and pagination
- should not mutate domain state
- usually mapped to read scopes

## 5.3 Action use case
Used when the use case triggers a business action not captured by simple CRUD replacement.

Examples:
- resend callback
- approve account
- cancel request

Typical characteristics:
- intention-driven
- may validate transitions and domain invariants
- often benefits from domain service support

---

# 6. Design Rules

Each generated use case MUST:

- have a single responsibility
- have a name that clearly expresses intent
- contain orchestration logic only
- use domain repository abstractions
- delegate business rules to domain entities or domain services
- avoid framework annotations when possible in the application layer
- avoid direct coupling to HTTP, JPA, Spring MVC, and serialization concerns

A use case must not become a god class.

---

# 7. Naming Conventions

The skill must generate use cases with consistent naming.

Examples:

Create<Entity>UseCase  
Update<Entity>UseCase  
Patch<Entity>UseCase  
Delete<Entity>UseCase  
Find<Entity>ByIdUseCase  
List<Entities>UseCase  
Activate<Entity>UseCase  
Search<Entities>ByNameUseCase  
Update<Entity>CallbackUrlUseCase

The generated method name must also be intention-revealing.

Preferred examples:

execute(...)
executeById(...)
handle(...)
apply(...)

The project should prefer one naming convention consistently.

---

# 8. Input Modeling Rules

A use case MUST receive a dedicated input model when input is required.

Possible input styles:

- command object
- query object
- input aggregate
- dedicated request-independent DTO

Examples:

CreateClientCommand
UpdateClientCommand
FindClientByIdQuery
ListClientsQuery
SearchClientsByNameQuery
ActivateClientCommand

The input object must belong to the application layer or be an application-facing aggregate.

A use case MUST NOT receive:
- controller request classes from the interface layer
- raw HTTP objects
- persistence entities

When the operation has no input payload beyond a path id, the generator may use:
- a dedicated command/query object, or
- a method parameter such as UUID id,
provided the project stays consistent.

---

# 9. Output Modeling Rules

A use case MUST return an application-oriented output model.

Possible output styles:

- output aggregate
- result object
- summary aggregate
- paged aggregate
- void when the operation semantics clearly justify it

Examples:

ClientOutput
ClientSummaryOutput
PagedClientsOutput
ActivateClientResult

The use case MUST NOT return:
- JPA entities
- raw controller response types
- framework response wrappers

For query/list operations, paginated output must carry the information needed by the response envelope.

---

# 10. Interaction with Domain

A generated use case may interact with the domain through:

- domain entities
- repository interfaces
- domain services
- value objects
- enums
- domain exceptions

Rules:

- business invariants belong in domain entities or domain services
- orchestration belongs in the use case
- domain exceptions must use messages from enums
- domain must remain independent from the application framework

The use case must not duplicate domain rules already modeled elsewhere.

---

# 11. When to Generate a Domain Service

The skill should generate or depend on a domain service when:

- business rules are non-trivial
- state transitions require validation
- logic is shared across multiple use cases
- multiple domain concepts must be coordinated
- the behavior is too rich to live comfortably inside the use case itself

Examples:
- activate client only if currently inactive
- resend callback only for active client with valid callback URL
- approve account only from pending status

The skill must avoid generating meaningless domain services for trivial repository pass-through logic.

---

# 12. Repository Usage Rules

A use case must access persistence through repository abstractions defined by the domain.

Allowed examples:

- entityRepository.findById(id)
- entityRepository.save(entity)
- entityRepository.existsByBusinessKey(...)
- entityRepository.findAll(pageable abstraction or application paging abstraction)

Not allowed:

- direct Spring Data repository usage inside the use case when the architecture requires indirection
- direct SQL
- JPA EntityManager in the application layer

The use case should express business intent, not persistence mechanics.

---

# 13. Transaction and Consistency Considerations

When the project standard requires it, the use case may be transactional.

However, the skill must prefer application-layer transactional boundaries only when justified.

Guidelines:

- write use cases often require transactional consistency
- pure query use cases should avoid unnecessary transactions
- transaction behavior must not leak persistence details into the domain model

The generated design must remain aligned with architecture.md.

---

# 14. Error Handling Rules

A generated use case MUST handle expected business failure scenarios through domain/application exceptions.

Typical scenarios:

- entity not found
- duplicate conflict
- invalid state transition
- invalid business rule
- missing dependency state
- forbidden operation by business rule

All user-visible/business error messages MUST come from enums.

No hardcoded error text is allowed.

Example:

ClientError.CLIENT_NOT_FOUND
ClientError.CLIENT_ALREADY_EXISTS
ClientError.CLIENT_CANNOT_BE_ACTIVATED

The use case may throw or propagate domain/application exceptions for later mapping by global exception handlers.

---

# 15. Security Awareness Rules

The use case itself should remain decoupled from transport-level security mechanics,
but the generated flow must remain consistent with secured endpoints.

Examples:

- read-oriented use cases usually support SCOPE_<resource>.read
- write/action use cases usually support SCOPE_<resource>.write

The skill may add documentation or notes about intended scope alignment,
but it should avoid coupling the application layer directly to controller authorization annotations unless architecture.md explicitly permits it.

---

# 16. Query Use Case Rules

For query/list use cases, the skill MUST support the business retrieval semantics cleanly.

Possible capabilities:

- find by id
- list all paginated
- filter by status
- search by name
- list active only
- list by date range when clearly requested

Query use cases must:

- avoid state mutation
- support pagination when returning collections
- return output models tailored for listing or querying
- keep filtering input explicit

Examples:

FindClientByIdUseCase
ListClientsUseCase
SearchClientsByNameUseCase
ListActiveClientsUseCase

---

# 17. Command Use Case Rules

For command/write use cases, the skill MUST support state-changing business intent safely.

Possible operations:

- create
- update
- patch
- delete
- activate
- deactivate
- approve
- cancel
- resend callback

Command use cases must:

- validate existence when needed
- enforce business rules
- persist changes through repository abstractions
- return output models or no content semantics according to the project standard

Examples:

CreateClientUseCase
UpdateClientUseCase
DeleteClientUseCase
ActivateClientUseCase
UpdateClientCallbackUrlUseCase

---

# 18. Pagination Rules

When the use case returns collections, the skill MUST model pagination when appropriate.

Default parameters may include:

- page
- size
- sort

Output should contain or make possible:

- current page
- page size
- total elements
- total pages

The application layer must not return controller-specific pagination wrappers,
but it must expose enough information for the response contract to be assembled later.

---

# 19. Validation Rules

A use case may rely on pre-validated input from the interface layer,
but it must still protect business invariants.

Guidelines:

- syntactic validation belongs mostly to request/input validation
- business validation belongs to domain/application rules
- the use case should never assume impossible domain states just because controller validation exists

Examples:
- callback URL string format may be syntactically validated at input level
- callback URL allowed by business policy belongs to domain/application logic

---

# 20. Mapping Rules

The use case should work with application and domain models, not with REST response wrappers.

Allowed transformations:

- input aggregate -> domain value objects/entities
- domain entity -> output aggregate
- domain collection/page -> paged output aggregate

The use case should not produce:
- ResponseEntity
- problem detail payloads
- HTTP headers

Those concerns belong outside the application layer.

---

# 21. Template Shape Guidance

A generated use case should typically contain:

- clear class name
- constructor-based dependency injection
- one public execution method
- orchestration logic
- repository/domain service interaction
- mapping to output object
- focused exception paths

Pseudo-shape:

public class CreateClientUseCase {
    private final ClientRepository clientRepository;
    private final CreateClientDomainService createClientDomainService;

    public ClientOutput execute(CreateClientCommand command) {
        // validate/find/orchestrate
        // apply domain behavior
        // persist
        // map output
    }
}

The exact syntax may vary, but the architectural responsibility must remain the same.

---

# 22. Lombok Rules

Lombok may be used in input/output models and in the use case class when aligned with project standards.

Recommended usage:
- @RequiredArgsConstructor for constructor injection
- @Builder for command/result models
- @Getter for immutable-style models

Avoid unnecessary Lombok magic in ways that obscure intent.

The application layer should remain readable and explicit.

---

# 23. Test Generation Rules

The skill MUST generate tests for the use case.

## 23.1 Mandatory test scenarios
At minimum, generate tests for:

- success path
- not found scenario when applicable
- conflict/duplicate scenario when applicable
- business rule violation when applicable
- correct repository interaction
- correct domain service interaction when present
- correct output mapping

## 23.2 Query use case tests
Add when relevant:

- paginated result handling
- filter propagation
- empty result behavior

## 23.3 Command use case tests
Add when relevant:

- creation success
- update success
- delete success
- action/state transition success
- invalid transition failure

The generated tests must contribute to JaCoCo thresholds:

- line coverage >= 90%
- branch coverage >= 80%

---

# 24. Minimum Acceptance Criteria

A generated use case is valid only if all of the following are true:

- it represents one business intent
- it respects application layer boundaries
- it does not expose controller or persistence internals
- it uses repository abstractions
- it delegates rich business rules appropriately
- it uses enum-backed error messages
- it returns application-oriented output
- it has tests
- naming is consistent with the feature and operation

If any criterion fails, the agent must correct the output.

---

# 25. Non-Goals

This skill does not automatically generate:

- full controller layers unless requested
- repository implementations unless required by a broader generation flow
- full CRUD unless requested
- asynchronous/event-driven orchestration unless requested
- batch processing unless explicitly requested

Those concerns should be handled by other dedicated skills.

---

# 26. Recommended Companion Skills

This use case skill works best with:

- generate-feature.md
- generate-crud.md
- generate-endpoint.md
- generate-domain-service.md
- generate-entity.md
- generate-aggregate.md
- generate-assembler.md
- generate-test.md
- generate-exception-handler.md

---

# 27. Final Rule

When the user requests "generate usecase <intent>", the agent must not create only a service skeleton.

It must generate a coherent application-layer use case with the proper input/output models,
repository interactions, business orchestration, exception paths, and tests, aligned with the project standards.
