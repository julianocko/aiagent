# generate-openapi.md
## OpenAPI Generation Specification
Version: 1.0

This skill defines how the AI development agent must generate and maintain OpenAPI artifacts
for Java 25 + Spring Boot REST APIs.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- response contract
- keycloak security guidelines

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate an OpenAPI contract synchronized with the generated REST API.

The contract may be produced as:
- a single root openapi.yaml
- feature-specific OpenAPI fragments
- both, when the project supports aggregation

---

# 2. Mandatory Coverage

The generated OpenAPI MUST document:
- paths
- operations
- request bodies when applicable
- response schemas
- error responses
- security requirements
- pagination parameters where applicable

---

# 3. Security Documentation Rules

The contract MUST include:
- bearerAuth security scheme
- required scopes for operations when represented

Examples:
- client.read
- client.write

---

# 4. Response Contract Rules

Success responses must reflect the standardized envelope:
{
  "data": {},
  "meta": {}
}

Error responses must reflect Problem Details:
application/problem+json

---

# 5. CRUD Rules

When CRUD is generated, the OpenAPI contract MUST include:
- POST /resources
- GET /resources/{id}
- GET /resources
- PUT /resources/{id}
- PATCH /resources/{id}
- DELETE /resources/{id}

List operations must include:
- page
- size
- sort

---

# 6. Minimum Acceptance Criteria

A generated OpenAPI artifact is valid only if:
- all generated endpoints are represented
- security is represented
- success envelope is represented
- Problem Details is represented
- pagination is represented where applicable

---

# 7. Final Rule

Whenever endpoints or CRUD are generated or changed, OpenAPI generation/update is mandatory.
