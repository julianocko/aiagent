# generate-entity.md
## Domain Entity Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate a domain entity
for a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:

- architecture.md
- agent.md
- generate-crud.md when applicable
- generate-endpoint.md when applicable
- generate-usecase.md when applicable
- generate-domain-service.md when applicable
- enums guidelines
- lombok guidelines
- keycloak security guidelines
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate a production-ready domain entity that represents a real business concept,
encapsulates its own invariants when appropriate, remains independent from frameworks,
and integrates consistently with the rest of the generated architecture.

A generated entity must be more than a bag of fields.
It must model domain meaning.

Examples:

- Client
- Account
- CallbackConfiguration
- PaymentRequest
- Consultation

The entity must be suitable for safe evolution in a production codebase.

---

# 2. Purpose of a Domain Entity

A domain entity represents a business concept with identity and lifecycle.

A domain entity MUST:

- have clear business meaning
- carry identity
- model relevant state
- protect domain invariants
- expose intention-revealing behavior when needed
- remain independent from framework concerns

A domain entity is not:

- a REST request DTO
- a persistence-only JPA structure
- a controller payload
- a generic mutable structure with unrestricted setters

Entities must serve the domain, not the transport layer.

---

# 3. Expected Command Format

Examples:

generate entity Client

generate entity Client with attributes:
- name: String required
- active: Boolean required
- callbackUrl: String optional
- description: String optional
- createdAt: OffsetDateTime auto
- updatedAt: OffsetDateTime auto

generate entity PaymentRequest with attributes:
- amount: BigDecimal required
- status: PaymentStatus required
- dueDate: LocalDate required

When the feature is not explicitly informed, the agent must infer it from the entity name.

---

# 4. Core Modeling Rules

Every generated entity MUST follow these rules:

- use UUID as identifier by default
- represent a business concept, not an API contract
- avoid framework annotations in the domain layer
- avoid public setters unless truly justified
- protect invariants through constructors, factory methods, or explicit behavior methods
- use value objects and enums when they improve clarity
- keep state changes intention-revealing

Examples of intention-revealing behavior:

- activate()
- deactivate()
- updateCallbackUrl(...)
- changeDescription(...)
- approve()
- cancel()

The generator must prefer expressive domain behavior over generic mutation.

---

# 5. Identity Rules

A domain entity must have identity.

Default identifier type:
- UUID

The identifier should be named consistently, usually:
- id

The entity should clearly distinguish identity from mutable business state.

The generator must avoid using business fields as the technical identifier unless the domain explicitly requires that pattern.

---

# 6. Framework Independence

The domain entity MUST remain independent from:

- Spring annotations
- JPA annotations
- Jackson annotations
- controller/request/response classes
- persistence entity classes
- ResponseEntity
- OpenAPI annotations

If persistence mapping is needed, it must be handled in the infrastructure layer or by dedicated persistence entities/mappers, according to architecture.md.

---

# 7. Rich vs Anemic Model Guidance

The generator should prefer a rich domain model whenever business rules or state transitions exist.

A richer entity should:

- expose meaningful methods
- validate internal consistency
- reject invalid mutations
- centralize obvious invariant checks

Examples:
- activate only if currently inactive
- update callback URL only when a valid value is provided
- approve only from a pending state

A more data-oriented entity may be acceptable when the concept truly has no behavior,
but the generator must not default blindly to anemic models.

---

# 8. Attribute Modeling Rules

The entity must model attributes consistently with business meaning.

Examples:

- name -> likely String, required, may need length validation and uniqueness at broader layers
- active -> boolean or Boolean, depending on domain needs, but primitive boolean is usually preferred when nullability is not meaningful
- callbackUrl -> may become String or a value object such as CallbackUrl if the domain benefits from stronger typing
- description -> optional free text
- createdAt / updatedAt -> audit-related metadata, included only when appropriate to the domain model or architecture

The generator must distinguish:

- required attributes
- optional attributes
- derived attributes
- audit metadata
- closed-set values better modeled as enums

---

# 9. Value Object Rules

The generator should create or recommend value objects when they improve domain clarity or safety.

Examples:

- CallbackUrl
- EmailAddress
- ClientName
- Money
- DocumentNumber

Use value objects when:

- validation rules are reusable
- the type has business meaning beyond raw primitive form
- stronger typing prevents misuse
- formatting/normalization logic belongs to the concept

Do not create value objects mechanically for every field.
They must add domain value.

---

# 10. Enum Rules

The generator should use enums when a field represents a closed set of valid states or categories.

Examples:

- ClientStatus
- PaymentStatus
- ConsultationType
- CallbackMode

Enums should be intention-revealing and domain-friendly.

Business error messages are not modeled in the entity itself, but related domain exceptions must use dedicated error enums as required by the exception message rules.

The entity may also use enums for lifecycle/state transitions when appropriate.

---

# 11. Constructors and Creation Rules

Entity creation must preserve invariants from the beginning.

Recommended options:

- constructor with required fields only
- static factory method
- builder when aligned with Lombok guidance and readability
- dedicated factory methods for special creation flows

The generator must avoid exposing invalid partially initialized entities.

Examples:

Client.create(name, callbackUrl, active)
new Client(id, name, active, callbackUrl, description, createdAt, updatedAt)

The chosen style must remain consistent with the project standards and domain clarity.

---

# 12. Mutation Rules

Entity state changes must be controlled.

Preferred approaches:

- explicit behavior methods
- controlled update methods
- invariant validation before mutation
- immutable fields where possible

Avoid:
- large sets of unrestricted setters
- public mutable fields
- procedural mutation from outside the entity for behavior that belongs inside it

Examples:

client.activate()
client.deactivate()
client.updateCallbackUrl(callbackUrl)
client.changeDescription(description)

The generator must prefer meaningful methods over generic setX patterns.

---

# 13. Invariant Rules

The entity should protect local domain invariants whenever they belong naturally inside the entity.

Examples:

- name must not be blank
- active client may require callback URL
- callback URL must be valid for entity rules
- a cancelled request cannot be approved
- an already active client cannot be activated again

When a rule involves multiple entities or external checks, the generator may delegate that responsibility to a domain service instead.

The generator must place invariants at the most coherent domain level.

---

# 14. Audit Field Rules

When audit fields are requested or required by architecture, the generator must decide carefully how they appear in the domain.

Common audit fields:
- createdAt
- updatedAt

Guidelines:

- include them in the domain entity when they are relevant to the business model or ubiquitous language
- otherwise keep them in persistence and expose them later through response models if needed
- remain consistent across the generated layers

If included in the entity, audit fields should not be freely mutable by arbitrary callers.

---

# 15. Nullability Rules

The entity should avoid ambiguous null semantics.

Guidelines:

- required fields must not be nullable in domain meaning
- optional fields may be nullable or wrapped according to project style
- primitive types should be preferred when null has no business meaning
- value objects may help reduce null ambiguity

The generator should not rely on null-heavy designs when a clearer model is possible.

---

# 16. Equality and Identity Rules

Entity equality should be based on identity in a way consistent with the project style.

Guidelines:

- entity identity is usually based on id
- avoid equality based on all mutable fields
- Lombok-generated equals/hashCode must be used carefully for entities

The generator must not mechanically apply @Data or broad equals/hashCode over mutable state in rich entities.

Prefer explicit control.

---

# 17. Lombok Rules

The generated entity should follow the Lombok directive whenever it improves clarity and reduces boilerplate.

Recommended usage patterns:

- @Getter
- @Builder when appropriate
- @AllArgsConstructor only when suitable
- @NoArgsConstructor only when clearly required by the project style
- explicit equals/hashCode strategy if needed

Avoid:
- @Data in rich domain entities
- excessive Lombok that hides business behavior
- exposing mutation patterns unintentionally

The generator must prefer clarity and domain integrity over convenience.

---

# 18. Relationship Rules

If the entity has relationships with other domain concepts, the generator must model them carefully.

Examples:

- a Client may own callback configuration
- an Account may have approval state
- a PaymentRequest may relate to a payer identity

Guidelines:

- model only relationships relevant to the current bounded context
- avoid persistence-driven relationship leakage into the domain if not needed
- do not over-model associations without clear domain purpose

The generator should prefer a clean, cohesive entity over a graph overloaded by infrastructure concerns.

---

# 19. Exception and Error Rules

The entity itself may throw domain/business exceptions when protecting invariants.

Rules:

- no hardcoded business error strings
- exceptions must use enum-backed messages
- exceptions should be intention-revealing
- invariant violations should fail fast

Examples:

- ClientError.CLIENT_ALREADY_ACTIVE
- ClientError.CLIENT_CALLBACK_URL_REQUIRED
- PaymentRequestError.REQUEST_ALREADY_APPROVED

The entity should not know about Problem Details or HTTP status mapping.

---

# 20. Persistence Awareness Rules

The domain entity must remain independent from persistence implementation,
but its design must be compatible with later persistence mapping.

Guidelines:

- use persistence-agnostic types when reasonable
- avoid leaking database-only concerns into the domain model
- persistence entity and mapper should be generated separately when required
- do not place JPA annotations in the domain entity unless architecture.md explicitly allows it

The infrastructure layer is responsible for persistence mapping.

---

# 21. Security Awareness Rules

The domain entity must remain independent from transport-level security and authentication.

Rules:

- no token information inside the entity
- no Keycloak-specific dependencies
- no Authorization header concerns
- no scope annotations

The entity may still express business authorization semantics as domain rules,
for example whether a state transition is allowed in a business sense.

OAuth and Keycloak mechanics belong outside the entity.

---

# 22. Response Contract Awareness Rules

The domain entity must not be designed around the response envelope,
but it must remain compatible with the standardized API response contract.

Therefore:

- do not embed meta/requestId/timestamp in the entity
- do not shape the entity as if it were the final API response
- keep domain concepts separate from transport representation

Assemblers and response models handle transformation for the API layer.

---

# 23. Template Shape Guidance

A generated entity should typically include:

- identity field
- required business fields
- optional business fields
- constructor/factory preserving invariants
- intention-revealing behavior methods
- controlled accessors
- domain validation helpers when useful

Pseudo-shape:

@Getter
public class Client {

    private final UUID id;
    private String name;
    private boolean active;
    private String callbackUrl;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Client(
        UUID id,
        String name,
        boolean active,
        String callbackUrl,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.active = active;
        this.callbackUrl = callbackUrl;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validateState();
    }

    public void activate() {
        if (this.active) {
            throw new BusinessException(ClientError.CLIENT_ALREADY_ACTIVE);
        }
        validateCallbackUrlWhenActivating();
        this.active = true;
    }

    public void updateCallbackUrl(String callbackUrl) {
        // validation + assignment
    }
}

The exact syntax may vary, but the domain responsibility must remain intact.

---

# 24. Testing Rules

The skill MUST generate tests for the entity when the entity has meaningful behavior or invariants.

At minimum, generate tests for:

- valid creation
- invalid creation when required fields are missing or invalid
- allowed state transition
- forbidden state transition
- invariant-protecting behavior
- mutation methods with business meaning

Examples:

- shouldCreateClientWithValidData()
- shouldFailWhenClientNameIsBlank()
- shouldActivateInactiveClient()
- shouldThrowWhenActivatingAlreadyActiveClient()
- shouldUpdateCallbackUrlWhenValid()

The generated tests must contribute to JaCoCo thresholds:

- line coverage >= 90%
- branch coverage >= 80%

Pure data-only entities may require fewer tests, but any meaningful behavior must be tested.

---

# 25. Minimum Acceptance Criteria

A generated entity is valid only if all of the following are true:

- it models a real domain concept
- it has identity
- it respects domain-layer independence
- it does not expose transport or persistence concerns
- it protects relevant invariants at the correct level
- it uses explicit, intention-revealing behavior when needed
- it avoids uncontrolled setter-based mutation
- it uses enums/value objects when justified
- it aligns with Lombok guidance
- tests are generated when behavior exists

If any criterion fails, the agent must correct the output.

---

# 26. Non-Goals

This skill does not automatically generate:

- full CRUD flows
- controllers
- repository implementations
- persistence entities
- OpenAPI documentation
- response wrappers
- OAuth/token handling

Those concerns belong to other dedicated skills.

---

# 27. Recommended Companion Skills

This entity skill works best with:

- generate-feature.md
- generate-crud.md
- generate-endpoint.md
- generate-usecase.md
- generate-domain-service.md
- generate-aggregate.md
- generate-assembler.md
- generate-test.md
- generate-repository-impl.md

---

# 28. Final Rule

When the user requests "generate entity <Name>", the agent must not create only a field container.

It must generate a meaningful domain entity with identity, proper invariants,
clear behavior, framework independence, and tests aligned with the project standards.
