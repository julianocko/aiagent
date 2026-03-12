# architecture-validator.md
## Architecture Validation Specification
Version: 1.0

This document defines the validation rules that must be executed after generation.

---

# 1. Goal

Detect and prevent architectural drift in generated code.

The validator acts as a mandatory quality gate.

---

# 2. Layer Validation Rules

## Domain layer must NOT depend on:
- Spring
- JPA
- Jackson
- controller classes
- persistence implementation
- ResponseEntity
- OpenAPI annotations

## Application layer must NOT depend on:
- controller request/response classes
- JPA repositories directly
- ResponseEntity
- servlet/web classes

## Interface layer must NOT:
- expose domain entities directly
- access persistence implementation directly
- contain core business rules

---

# 3. Response Contract Validation

Controllers must:
- return success envelope on success
- return no raw domain objects
- rely on global exception handling for errors

Errors must:
- use Problem Details
- include requestId and timestamp where standardized

---

# 4. Security Validation

Generated endpoints must:
- have read/write protection where applicable
- align with Keycloak/OAuth2 resource server strategy
- avoid leaking security concerns into domain entities

---

# 5. Error Message Validation

Business and domain messages must:
- come from enums
- not be hardcoded in controllers, use cases, or domain services

---

# 6. Test Validation

Generated flows must have:
- domain tests when business rules exist
- use case tests
- controller tests
- repository tests when persistence logic is custom/non-trivial

---

# 7. OpenAPI Validation

When endpoints exist, OpenAPI must:
- include all routes
- include success envelope schemas
- include Problem Details
- include security requirements

---

# 8. Flyway Validation

Migrations must:
- use semantic versioning
- target PostgreSQL only
- keep flyway_schema_history in public schema

---

# 9. Template Validation

No generated file may contain unresolved placeholders.

---

# 10. Final Rule

No generation is considered successful until architecture validation passes.
