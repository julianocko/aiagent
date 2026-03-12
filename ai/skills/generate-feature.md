# generate-feature.md
## Feature Module Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate the complete
base structure for a feature module in a Java 25 + Spring Boot project.

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

Generate a complete, production-ready feature skeleton organized by business capability,
not by technical layer only.

Examples:
- client
- account
- callback
- payment-request

The generated feature must be ready to receive CRUD, endpoints, use cases,
domain services, persistence adapters, tests, and security rules.

---

# 2. Expected Command Format

Examples:

generate feature client
generate feature account
generate feature payment-request

---

# 3. Mandatory Outputs

A generated feature MUST create the module structure below or its equivalent
defined by architecture.md.

Example:

features/<feature>/
    domain/
        model/
        repository/
        service/
        exception/
        enums/
        valueobject/
    application/
        usecase/
        aggregate/
        command/
        query/
    interfaces/rest/
        controller/
        request/
        response/
        assembler/
    infrastructure/
        persistence/
            entity/
            repository/
            mapper/
        config/
        security/
    tests/

The exact subfolders may vary, but the layer separation is mandatory.

---

# 4. Feature Responsibilities

A feature module must encapsulate:
- its own domain rules
- its own application use cases
- its own REST interface
- its own persistence adapters
- its own tests

Shared code must go only to shared/common modules when it is truly cross-cutting.

The generator must avoid creating “god” shared modules for feature-specific logic.

---

# 5. Naming Rules

Feature names should be:
- business-oriented
- singular/plural according to project convention
- lowercase for package/folder naming
- consistent with routes and class naming

Examples:
- feature folder: client
- route base: /clients
- entity: Client
- controller: ClientController

---

# 6. Configuration Rules

The feature should be ready to integrate with:
- YAML-based configuration
- Keycloak security
- response wrapper
- exception handling
- OpenAPI
- Flyway

The generator may create placeholders or package locations for:
- feature security customization
- feature configuration beans
- feature-specific properties mapping when justified

---

# 7. Testing Rules

A feature structure must always include a place for:
- domain tests
- application tests
- controller tests
- persistence tests

The generator must not leave testing as an afterthought.

---

# 8. Minimum Acceptance Criteria

A generated feature is valid only if:
- it respects architecture.md
- it is organized by business capability
- the layers are clearly separated
- it is ready to receive CRUD/endpoints/use cases
- it supports testing and security integration
- naming is consistent

---

# 9. Final Rule

When the user requests "generate feature <name>", the agent must not create only empty folders.

It must generate a coherent feature module foundation aligned with the project standards.
