# agent.md
## AI Development Agent Specification
Version: 2.0

This document defines how the AI development agent operates when generating
Java REST APIs using Spring Boot.

The agent must follow strict architectural, security, and coding standards
defined in the project.

The purpose of this agent is to generate production-ready backend code
aligned with modern enterprise architecture.

---

# 1. Agent Responsibilities

The agent is responsible for:
- generating consistent backend code
- enforcing architecture standards
- applying security best practices
- respecting the API response contract
- generating tests and migrations
- ensuring clean separation of layers

The agent MUST prioritize architecture compliance over convenience.

---

# 2. Source of Truth

The agent must consult documentation in the following priority order:

1. architecture.md
2. enums guidelines
3. lombok guidelines
4. keycloak security guidelines
5. response contract

If conflicts occur, architecture.md prevails.

---

# 3. Supported Commands

The agent understands the following commands:

- generate feature
- generate crud
- generate endpoint
- generate usecase
- generate domain-service
- generate entity
- generate aggregate
- generate assembler
- generate repository-impl
- generate test
- generate security
- generate exception-enum
- generate exception-handler
- generate response-wrapper

---

# 4. Agent Generation Workflow

When executing a command, the agent must follow this workflow:

1. Read architecture.md.
2. Apply additional standards:
   - enums rules
   - lombok rules
   - keycloak rules
   - response contract
3. Determine required artifacts.
4. Generate code respecting layer boundaries.
5. Apply response contract.
6. Generate tests.
7. Validate architecture compliance.

---

# 5. Security Rules

All generated APIs must integrate with Keycloak.

Security model:
- OAuth2 Resource Server
- Bearer Token authentication

Endpoints must require scopes:
- SCOPE_resource.read
- SCOPE_resource.write

Security must be enforced using Spring Security.

---

# 6. Response Contract Enforcement

Success responses must follow:

{
  "data": {},
  "meta": {
    "requestId": "uuid",
    "timestamp": "ISO8601"
  }
}

Errors must follow RFC7807:
application/problem+json

Controllers must never return raw domain objects.

---

# 7. Exception Handling

Exceptions must be:
- domain specific
- mapped to Problem Details
- based on error enums

Global exception handling must be implemented.

---

# 8. Persistence Rules

Repositories are defined in the domain layer.
Infrastructure implements them using JPA.

Database: PostgreSQL only.

Migrations must use Flyway semantic versioning:
V1.0.0__create_table.sql

Flyway history table:
public.flyway_schema_history

---

# 9. REST Conventions

Controllers must respect REST standards.

- POST -> 201 Created
- GET -> 200 OK
- DELETE -> 204 No Content

POST responses must include Location header.
Pagination must return metadata.

---

# 10. Logging and Observability

All requests must generate:
- requestId
- timestamp

These must appear in responses and logs.

---

# 11. Validation

Requests must use Jakarta Validation.
Validation errors must return Problem Details.

---

# 12. Testing Strategy

The agent must generate:
- unit tests for domain logic
- unit tests for use cases
- controller tests
- integration tests when required

JaCoCo must enforce coverage thresholds.

---

# 13. Agent Compliance

All generated code must comply with:
- architecture.md
- security standards
- response standards
- error enum rules
- lombok usage rules

If the generated code violates architecture constraints,
the agent must correct the output automatically.

---

# 14. Future Extensions

The architecture allows future commands such as:
- generate integration
- generate event
- generate scheduler

The agent must remain extensible.
