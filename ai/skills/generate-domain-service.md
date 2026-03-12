# generate-domain-service.md
## Domain Service Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate a domain service
for a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:

- architecture.md
- agent.md
- generate-crud.md when applicable
- generate-endpoint.md when applicable
- generate-usecase.md when applicable
- enums guidelines
- lombok guidelines
- keycloak security guidelines
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate a production-ready domain service that encapsulates business rules,
protects domain invariants, remains framework-independent, and integrates correctly
with the rest of the application architecture.

A generated domain service must not be a generic utility class.
It must represent meaningful domain behavior.

Examples:

- CreateClientDomainService
- UpdateClientDomainService
- ActivateClientDomainService
- DeactivateClientDomainService
- ApproveAccountDomainService
- ResendCallbackDomainService

The service must exist to model business logic, not to move code mechanically.

---

# 2. Purpose of the Domain Service Layer

A domain service exists to model domain behavior that does not fit naturally in a single entity
or that needs to coordinate domain concepts while remaining part of the domain layer.

A domain service MUST:

- represent business logic
- remain independent from frameworks
- protect domain invariants
- use domain models and abstractions
- throw domain exceptions backed by enums
- avoid controller, persistence implementation, and HTTP concerns

A domain service is not:
- a controller helper
- a use case replacement
- an infrastructure adapter
- a generic static utility holder

---

# 3. Expected Command Format

Examples:

generate domain-service activate-client

generate domain-service update-client-callback-url

generate domain-service approve-account

generate domain-service resend-callback for feature client

generate domain-service create-client with rules:
- client name must be unique
- callback URL is required when active is true

When the feature is not explicitly informed, the agent must infer it from the intent.

---

# 4. When a Domain Service Must Be Generated

The agent should generate a domain service when at least one of the following is true:

- the business rule is non-trivial
- the operation validates domain invariants
- multiple domain concepts are coordinated
- the logic is reusable across multiple use cases
- the behavior does not belong naturally to a single entity method
- the operation involves business decisions, transitions, or eligibility rules

Examples:

- a client can only be activated if it is currently inactive
- a callback can only be resent for active clients
- an approval is only valid for pending requests
- an entity name must be unique before creation
- a configuration can only be changed under certain domain rules

The agent must avoid generating a domain service for trivial pass-through persistence operations.

---

# 5. When a Domain Service Must NOT Be Generated

A domain service should usually NOT be generated when:

- the logic is only orchestration between controller and repository
- the behavior clearly belongs inside the entity itself
- the operation is a simple query without business rules
- the only logic is input mapping or response formatting
- the only work is delegating directly to repository.save with no rule enforcement

Examples of poor usage:

- using domain services as wrappers for CRUD without rules
- creating a service only because there is a use case
- placing transport or persistence concerns in the domain service

The skill must generate only meaningful domain services.

---

# 6. Domain Service Responsibilities

A generated domain service MAY:

- validate state transitions
- enforce business constraints
- coordinate entity behavior
- use repository abstractions defined in the domain
- create or mutate domain entities according to business rules
- throw domain exceptions
- use value objects and domain enums

A generated domain service MUST NOT:

- return HTTP responses
- depend on Spring MVC, JPA, or controller classes
- build Problem Details payloads
- use ResponseEntity
- use persistence implementation classes when the architecture requires repository abstractions
- contain OpenAPI concerns
- expose infrastructure details

---

# 7. Naming Conventions

The service name must clearly express the business intent.

Preferred examples:

Create<Entity>DomainService
Update<Entity>DomainService
Delete<Entity>DomainService
Activate<Entity>DomainService
Deactivate<Entity>DomainService
Approve<Entity>DomainService
Resend<Entity>CallbackDomainService
Validate<Entity>EligibilityDomainService

The project should consistently prefer:
<Intent><Entity>DomainService

Avoid generic names such as:
- EntityService
- CommonService
- BusinessService
- ServiceHelper

---

# 8. Interaction with Entities

The domain service should collaborate with entities, not replace them.

Rules:

- entity-specific invariants should stay in the entity whenever appropriate
- the domain service should coordinate rules that involve multiple checks or abstractions
- domain services may invoke expressive entity methods
- domain services must preserve entity consistency

Examples:

Allowed:
- client.activate()
- client.updateCallbackUrl(url)
- approvalRequest.approve()

Not ideal:
- manually mutating entity internals that should be encapsulated by entity methods

The generator must prefer rich domain modeling over anemic procedural logic.

---

# 9. Interaction with Repositories

A domain service may depend on repository abstractions from the domain layer when required by the business rule.

Examples:

- uniqueness checks
- lookup required to validate transitions
- existence checks
- fetching related aggregates/entities necessary for the rule

Allowed:
- ClientRepository
- AccountRepository
- CallbackRepository

Not allowed:
- Spring Data JPA repository directly in the domain layer
- EntityManager in the domain service
- SQL queries in the domain service

The domain service must depend only on abstractions allowed by architecture.md.

---

# 10. Error Message Rules

All exception messages used by the domain service MUST be defined in enums, as required by the exception message directive fileciteturn5file0.

Rules:

- no hardcoded error strings in the domain service
- exceptions must reference enums directly
- each enum should represent a logical group of related errors
- enums should preferably contain an identifier/code and message

Example:

public enum ClientError {
    CLIENT_NOT_FOUND("Client not found"),
    CLIENT_ALREADY_EXISTS("Client already exists"),
    CLIENT_ALREADY_ACTIVE("Client is already active"),
    CLIENT_CALLBACK_URL_REQUIRED("Callback URL is required");

    private final String message;
}

Usage example:

throw new BusinessException(ClientError.CLIENT_ALREADY_ACTIVE);

The generator must centralize business messages in enums consistently.

---

# 11. Lombok Rules

The generated service and its supporting models should follow the Lombok directive whenever applicable fileciteturn5file1.

Recommended usage:

- @RequiredArgsConstructor for constructor injection
- @Getter for immutable-style supporting models when needed
- @Builder for rich domain command/value creation when appropriate

Guidelines:

- use Lombok to reduce boilerplate
- prefer explicit annotations over excessive magic when clarity matters
- do not use Lombok in a way that hides domain behavior

The generated domain service should remain readable and intention-revealing.

---

# 12. Security Awareness Rules

The domain service must remain independent from transport-level authentication details,
but it must be compatible with the overall Keycloak-based architecture described in the authentication specification fileciteturn5file2.

Rules:

- no Authorization header handling
- no token parsing in domain services
- no coupling to Keycloak SDK in the domain layer
- no direct scope validation in domain services unless architecture.md explicitly introduces a domain abstraction for that

Security enforcement belongs primarily to outer layers.
The domain service should focus on business authorization rules, not OAuth mechanics.

Example:
- valid domain rule: only active client can resend callback
- not valid in domain service: verify bearer token signature

---

# 13. Response Contract Awareness Rules

The domain service must not build API responses, but its outputs must remain compatible with the standardized API response contract fileciteturn5file3.

Therefore, the service should return one of the following depending on the design:

- domain entity
- value object
- domain result model
- void when the business action does not require direct domain return

The application layer or assembler is responsible for transforming domain outputs into:

- success envelopes with data/meta
- Problem Details error payloads

The domain service must stay pure and transport-agnostic.

---

# 14. Domain Service Classification

The agent should classify the service before generating it.

## 14.1 Creation/validation service
Examples:
- CreateClientDomainService
- RegisterAccountDomainService

Typical responsibilities:
- validate uniqueness
- validate required domain prerequisites
- create or prepare valid domain state

## 14.2 Update/change service
Examples:
- UpdateClientDomainService
- UpdateClientCallbackUrlDomainService

Typical responsibilities:
- validate existence
- validate business permission for change
- apply mutation through domain methods

## 14.3 Transition/action service
Examples:
- ActivateClientDomainService
- ApproveAccountDomainService
- ResendCallbackDomainService

Typical responsibilities:
- validate state transitions
- validate eligibility
- trigger domain behavior

## 14.4 Deletion/removal service
Examples:
- DeleteClientDomainService
- RemoveClientConfigurationDomainService

Typical responsibilities:
- validate if removal is allowed
- protect domain integrity before deletion

---

# 15. Input Modeling Rules

A domain service may receive:

- domain entity
- UUID identifier plus supporting values
- domain value objects
- a dedicated domain command model when the behavior is complex

The skill must avoid coupling the domain service to:
- controller request objects
- REST DTOs
- persistence entities
- HTTP-specific classes

Examples of acceptable signatures:

Client create(String name, URI callbackUrl, boolean active)
Client activate(Client client)
void validateDeletion(Client client)
Client updateCallbackUrl(Client client, CallbackUrl callbackUrl)

The chosen signature should reflect business intent clearly.

---

# 16. Output Modeling Rules

A domain service may return:

- domain entity
- domain result/value object
- boolean only when it truly represents domain meaning
- void when the action is validation-only or mutation is applied to the provided aggregate/entity

Avoid returning generic maps, raw strings, or transport objects.

Examples:

Client
ApprovalDecision
CallbackEligibility
void

The output should remain semantically meaningful in the domain.

---

# 17. Domain Exception Rules

The generated domain service should use domain-specific exceptions.

Examples:

- ClientNotFoundException
- ClientAlreadyExistsException
- ClientAlreadyActiveException
- InvalidClientStateException
- CallbackUrlRequiredException

Rules:

- exceptions must reference error enums
- exceptions should remain meaningful and intention-revealing
- exceptions should be reusable by use cases and global exception handlers
- exceptions must not expose internal technical details unnecessarily

The generator may reuse a standard BusinessException(ErrorEnum) pattern if that is the established project style.

---

# 18. Business Rule Examples

The generator should model rules explicitly when inferable.

Examples for Client:

## 18.1 Create
- client name must be unique
- callback URL may be required depending on active status

## 18.2 Activate
- client must exist
- client must not already be active
- callback URL may be required before activation

## 18.3 Update callback URL
- client must exist
- callback URL must satisfy syntactic and domain requirements

## 18.4 Delete
- client cannot be deleted when linked to active configuration, if such rule exists

The generated domain service should prefer explicit named validations over inline boolean chaos.

---

# 19. Service Shape Guidance

A generated domain service should usually contain:

- a clear class name
- constructor-based dependency injection when dependencies exist
- one primary public method aligned with the intent
- focused private validation helpers when useful
- repository abstraction usage only when justified
- exception throwing using enum-backed messages

Pseudo-shape:

@RequiredArgsConstructor
public class ActivateClientDomainService {

    private final ClientRepository clientRepository;

    public Client execute(Client client) {
        validateCanActivate(client);
        client.activate();
        return client;
    }

    private void validateCanActivate(Client client) {
        if (client.isActive()) {
            throw new BusinessException(ClientError.CLIENT_ALREADY_ACTIVE);
        }
    }
}

The exact syntax may vary, but the architectural role must remain intact.

---

# 20. Atomicity Rule

The previous architecture guidance defined that domain services must be atomic and operation-oriented fileciteturn5file20.

Therefore:

- each operation should have its own dedicated domain service when a domain service is necessary
- do not merge unrelated actions into one large service
- avoid a single service containing create/update/delete/activate/deactivate altogether

Examples of good separation:

- CreateClientDomainService
- UpdateClientDomainService
- DeleteClientDomainService
- ActivateClientDomainService

This improves readability, reuse, and testability.

---

# 21. Relationship with Use Cases

Use cases should invoke domain services when business rules justify their existence, consistent with the architecture guidance that use cases coordinate domain services and aggregates fileciteturn5file23turn5file11.

Rules:

- the use case orchestrates the flow
- the domain service protects business rules
- the use case persists through repository abstractions as needed
- the domain service should not replace the application layer orchestration role

The generator must keep these responsibilities separated.

---

# 22. Testing Rules

The skill MUST generate tests for the domain service.

## 22.1 Mandatory scenarios
At minimum, generate tests for:

- success path
- business rule violation path
- invalid state transition path when applicable
- not found/precondition path when applicable
- repository interaction when the service depends on repositories
- correct exception enum usage behavior when relevant

## 22.2 Test style
Tests should:

- follow TDD-oriented structure, consistent with the existing testing direction fileciteturn5file8
- be clear and intention-revealing
- isolate domain behavior
- avoid unnecessary framework bootstrapping for pure domain tests

Examples:

- shouldActivateClientWhenInactive()
- shouldThrowWhenClientIsAlreadyActive()
- shouldThrowWhenCallbackUrlIsRequiredForActivation()

Generated tests must contribute to JaCoCo coverage targets established in the project architecture fileciteturn5file11.

---

# 23. Minimum Acceptance Criteria

A generated domain service is valid only if all of the following are true:

- it models real business logic
- it is framework-independent
- it respects domain layer boundaries
- it uses repository abstractions only when justified
- it does not expose HTTP or persistence implementation concerns
- all business messages come from enums
- naming is operation-oriented and explicit
- tests are generated
- it remains atomic and cohesive

If any criterion fails, the agent must correct the generated output.

---

# 24. Non-Goals

This skill does not automatically generate:

- full CRUD
- controllers
- HTTP response wrappers
- persistence implementations
- OpenAPI documentation
- OAuth/token handling
- asynchronous orchestration unless explicitly requested

Those concerns belong to other skills.

---

# 25. Recommended Companion Skills

This domain service skill works best with:

- generate-feature.md
- generate-crud.md
- generate-endpoint.md
- generate-usecase.md
- generate-entity.md
- generate-aggregate.md
- generate-assembler.md
- generate-test.md
- generate-exception-handler.md

---

# 26. Final Rule

When the user requests "generate domain-service <intent>", the agent must not create a generic service stub.

It must generate a meaningful, atomic, domain-oriented service with clear business intent,
enum-backed exceptions, proper collaboration with entities and repository abstractions,
and tests aligned with the project standards.
