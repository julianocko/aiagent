# generate-endpoint.md
## Endpoint Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate a specific REST endpoint
for an existing or new feature in a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:

- architecture.md
- agent.md
- generate-crud.md when applicable
- enums guidelines
- lombok guidelines
- keycloak security guidelines
- response contract

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate a single endpoint or a small cohesive endpoint flow with all required supporting artifacts,
ensuring it is production-ready, architecture-compliant, secure, tested, and consistent with the API standards.

This skill is intended for cases where the user does not want a full CRUD, but a focused capability such as:

- activate client
- deactivate client
- resend callback
- search clients by name
- list active clients
- update callback URL
- approve account
- cancel request

The generated endpoint must include all necessary supporting pieces across the layers.

---

# 2. Expected Command Format

Examples:

generate endpoint activate-client

generate endpoint update-client-callback-url

generate endpoint search-clients-by-name

generate endpoint approve-account for feature account

generate endpoint resend-callback with request:
- callbackUrl: String required

When the user does not explicitly provide the feature name, the agent must infer it from the endpoint intent.

---

# 3. Generation Scope

A generated endpoint MUST include only the artifacts necessary for that endpoint to work correctly,
but it must not be under-generated.

Depending on the endpoint intent, the skill may generate:

- domain service
- domain exception
- error enum entries
- use case
- input aggregate or request model
- output aggregate or response model
- controller method
- mapper/assembler updates
- repository contract updates
- repository implementation updates
- tests
- OpenAPI annotations
- security annotations

The skill must not generate unrelated CRUD operations.

---

# 4. Endpoint Classification

The agent must classify the endpoint before generating it.

## 4.1 Command endpoint
Used when the endpoint changes state or triggers an action.

Examples:
- activate client
- deactivate client
- approve account
- cancel order
- resend callback

Usually mapped to:
- POST action endpoints, or
- PATCH/PUT when the operation is a resource state change

## 4.2 Query endpoint
Used when the endpoint retrieves information.

Examples:
- search clients by name
- list active clients
- get client summary
- list callbacks by date

Usually mapped to:
- GET endpoints

## 4.3 Partial update endpoint
Used when the endpoint updates a specific part of a resource.

Examples:
- update callback URL
- update description
- update status note

Usually mapped to:
- PATCH or PUT depending on the contract

The classification must align with REST semantics.

---

# 5. REST Design Rules

The generated endpoint MUST use the correct HTTP method according to the business intent.

Recommended mapping:

- GET for retrieval
- POST for action-oriented commands that do not fit pure resource replacement
- PATCH for partial updates
- PUT for full replacement when applicable
- DELETE for removals

Examples:

POST /clients/{id}/activate
POST /clients/{id}/deactivate
PATCH /clients/{id}/callback-url
GET /clients/search?name=...
GET /clients/active
POST /accounts/{id}/approve

The skill must avoid RPC-style naming when a more RESTful design is possible, but action endpoints are allowed when business intent is clearer that way.

---

# 6. Mandatory Outputs

For any generated endpoint, the agent MUST create or update all necessary artifacts.

## 6.1 Application layer
Generate or update:

- dedicated use case
- input aggregate or command object
- output aggregate or query result object when needed

Examples:

ActivateClientUseCase
UpdateClientCallbackUrlUseCase
SearchClientsByNameUseCase

## 6.2 Interface layer
Generate or update:

- controller method
- request model when required
- response model when required
- response assembler or mapper usage
- OpenAPI metadata when applicable

## 6.3 Domain layer
Generate or update when necessary:

- domain service
- domain exception
- error enum entries
- repository contract methods
- domain validations

## 6.4 Infrastructure layer
Generate or update when necessary:

- repository implementation method
- Spring Data repository query
- persistence mapper
- persistence entity mapping if affected

## 6.5 Tests
Generate or update:

- use case tests
- controller tests
- domain service tests when applicable
- repository integration tests when custom query/persistence logic is introduced

---

# 7. Success Response Contract

All successful endpoint responses MUST follow the standard response envelope.

Single result:

{
  "data": {},
  "meta": {
    "requestId": "uuid",
    "timestamp": "ISO-8601"
  }
}

List result:

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

Action endpoint without body may use:
- 204 No Content when that is the project standard
- 200 OK with standardized envelope when the project prefers returning the updated representation

The project should prefer consistency across similar endpoints.

---

# 8. Error Response Contract

All errors MUST follow RFC7807 Problem Details.

Content-Type:
application/problem+json

Error responses must include when applicable:

- type
- title
- status
- detail
- instance
- requestId
- timestamp
- errors for validation failures

All business and domain messages must come from enums.

No hardcoded error messages are allowed in use cases, services, or controllers.

---

# 9. Security Rules

All generated endpoints MUST integrate with Keycloak-based security.

The API acts as an OAuth2 Resource Server.

Authorization must be enforced with scopes or authorities.

Recommended patterns:

- SCOPE_<resource>.read for GET endpoints
- SCOPE_<resource>.write for POST/PATCH/PUT/DELETE endpoints
- more specific authorities when the project defines finer control

Examples:

- GET /clients/active -> SCOPE_client.read
- POST /clients/{id}/activate -> SCOPE_client.write
- PATCH /clients/{id}/callback-url -> SCOPE_client.write

The skill must generate method-level or controller-level authorization according to the project standard.

---

# 10. Input Modeling Rules

When an endpoint requires payload input, the skill MUST generate a specific request model or input aggregate.

Examples:

ActivateClientRequest
UpdateClientCallbackUrlRequest
SearchClientsRequest

Input models must use Jakarta Validation when applicable.

Examples:
- @NotNull
- @NotBlank
- @Size
- @Pattern
- @Email

The input model must represent only what the endpoint needs.

It must not reuse larger request objects just for convenience.

---

# 11. Output Modeling Rules

When the endpoint returns data, the skill MUST generate the proper output model.

Examples:

ClientResponse
ClientSummaryResponse
ClientActivationResponse
PagedClientResponse

Output models must not expose:
- domain internals
- persistence internals
- framework internals

The agent must generate tailored response models when the endpoint intent differs from the standard CRUD representation.

---

# 12. Use Case Rules

Each generated endpoint MUST have a dedicated use case when it represents a distinct business intent.

Examples:

ActivateClientUseCase
ResendClientCallbackUseCase
SearchClientsByNameUseCase
UpdateClientCallbackUrlUseCase

Use cases MUST:

- represent one business intent
- orchestrate domain logic
- use repository abstractions
- avoid controller concerns
- avoid persistence-specific details
- return output aggregates or endpoint-oriented results

When the operation contains relevant business rules, the use case should delegate to a domain service.

---

# 13. Domain Service Rules

A domain service must be generated when the endpoint includes business rules such as:

- status transition validation
- invariant enforcement
- duplicate prevention
- reusable business decision logic
- multi-step domain orchestration

Examples:
- only active clients can resend callbacks
- callback URL must meet a domain-specific rule
- approval is only allowed from pending status

Domain services MUST:

- live in the domain layer
- be framework-independent
- throw domain exceptions backed by enums

The skill must not create meaningless domain services for trivial pass-through operations.

---

# 14. Repository Rules

If the endpoint requires persistence access beyond existing methods, the skill must update repository contracts and implementations.

Possible additions:

- existsBy...
- findByStatus
- findByNameContainingIgnoreCase
- findActiveClients
- update-specific query when justified
- paginated filtered queries

The domain layer defines the abstraction.
The infrastructure layer implements it.

If a custom query is introduced, repository tests should be generated when relevant.

---

# 15. Controller Rules

The generated controller endpoint MUST:

- use @RestController
- use the correct HTTP mapping annotation
- validate incoming requests
- delegate to the dedicated use case
- return standardized ResponseEntity
- enforce authorization
- never expose domain entities directly
- use assembler/mapper/response wrapper as defined by the project

Examples of acceptable endpoint shapes:

POST /clients/{id}/activate
PATCH /clients/{id}/callback-url
GET /clients/search
GET /clients/active

The skill must preserve naming consistency with existing endpoints in the feature.

---

# 16. OpenAPI Rules

When OpenAPI is part of the project standard, the generated endpoint should include documentation metadata.

At minimum, support or generate:

- summary
- description
- response codes
- security requirement
- request schema
- response schema

This is especially important for non-standard action endpoints.

---

# 17. Exception Handling Rules

The generated endpoint MUST integrate with global exception handling.

At minimum, support these mappings when applicable:

- validation error -> 400
- unauthorized -> 401
- forbidden -> 403
- entity not found -> 404
- conflict -> 409
- business rule violation -> 422 or project-defined status

The endpoint generation must either create needed exception types or reuse the existing standardized exception model.

All mapped errors must return Problem Details.

---

# 18. Pagination and Filtering Rules

For query endpoints that return collections, the skill MUST support pagination unless the endpoint is explicitly small and bounded by business definition.

Default conventions:

- page
- size
- sort

When filters are part of the endpoint intent, the skill should model them explicitly.

Examples:

GET /clients/search?name=jo&page=0&size=20
GET /clients?active=true&page=0&size=20

Responses must follow the standard response envelope with pagination metadata.

---

# 19. Naming Conventions

Generated artifacts must follow consistent naming.

Examples for endpoint: activate client

- ActivateClientUseCase
- ActivateClientRequest (if payload exists)
- ActivateClientResponse (if custom response exists)
- ActivateClientDomainService (if required)

Examples for endpoint: update client callback URL

- UpdateClientCallbackUrlUseCase
- UpdateClientCallbackUrlRequest
- UpdateClientCallbackUrlResponse
- UpdateClientCallbackUrlDomainService (only if justified)

The generated route, method name, class names, and test names must remain aligned.

---

# 20. Test Generation Rules

The skill MUST generate tests for the endpoint flow.

## 20.1 Use case tests
Generate tests for:
- success path
- not found path
- forbidden business transition when applicable
- conflict path when applicable
- invalid input orchestration when applicable

## 20.2 Controller tests
Generate tests for:
- success response
- validation failure
- unauthorized/forbidden
- not found handling
- response contract compliance

## 20.3 Domain service tests
Generate when domain service exists:
- invariant checks
- allowed transition
- forbidden transition
- correct exception behavior

## 20.4 Repository tests
Generate when:
- custom query exists
- custom persistence behavior is introduced

The generated tests must contribute to JaCoCo coverage requirements:

- line coverage >= 90%
- branch coverage >= 80%

---

# 21. Decision Rules

The skill must make sensible architectural decisions based on the endpoint intent.

Examples:

## 21.1 State transition
Request:
generate endpoint activate-client

Preferred result:
- POST /clients/{id}/activate
- ActivateClientUseCase
- domain validation for allowed transition
- security scope write
- success response with updated representation or no content according to project standard

## 21.2 Search endpoint
Request:
generate endpoint search-clients-by-name

Preferred result:
- GET /clients/search?name=
- SearchClientsByNameUseCase
- paginated response
- repository search query
- read scope

## 21.3 Specific field update
Request:
generate endpoint update-client-callback-url

Preferred result:
- PATCH /clients/{id}/callback-url
- UpdateClientCallbackUrlRequest
- UpdateClientCallbackUrlUseCase
- URL validation
- write scope

The agent must favor explicit, intention-revealing design.

---

# 22. Minimum Acceptance Criteria

A generated endpoint is valid only if all of the following are true:

- correct HTTP method is used
- route naming is coherent
- dedicated use case exists
- response contract is respected
- errors use Problem Details
- security scope is enforced
- controller does not expose domain entities
- request validation exists when needed
- tests are generated
- all business messages come from enums

If any criterion fails, the agent must correct the generated output.

---

# 23. Non-Goals

This skill does not automatically generate:

- a full CRUD unless requested
- batch endpoints unless requested
- asynchronous/event-driven flows unless requested
- external integrations unless required by the endpoint
- report/export endpoints unless requested

Those should be handled by dedicated skills.

---

# 24. Recommended Companion Skills

This endpoint skill works best with:

- generate-feature.md
- generate-crud.md
- generate-usecase.md
- generate-domain-service.md
- generate-entity.md
- generate-assembler.md
- generate-test.md
- generate-security.md
- generate-exception-handler.md
- generate-response-wrapper.md

---

# 25. Final Rule

When the user requests "generate endpoint <intent>", the agent must not produce only a controller method stub.

It must generate a coherent, end-to-end endpoint implementation with the necessary supporting artifacts, aligned with the project standards and ready to evolve safely.
