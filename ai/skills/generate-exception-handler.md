# generate-exception-handler.md
## Exception Handler Generation Specification
Version: 2.0

This skill defines how the AI development agent must generate global exception handling
for a Java 25 + Spring Boot project.

All generated artifacts MUST comply with:
- architecture.md
- agent.md
- response contract
- enums guidelines

If any conflict occurs, architecture.md is the primary source of truth.

---

# 1. Goal

Generate a global exception handling mechanism that maps technical and business
exceptions to RFC7807 Problem Details.

---

# 2. Mappings

At minimum, handle:
- validation error -> 400
- unauthorized -> 401
- forbidden -> 403
- not found -> 404
- conflict -> 409
- business rule violation -> 422 or project-defined status
- unexpected error -> 500

---

# 3. Rules

The handler MUST:
- return application/problem+json
- include requestId and timestamp
- support field errors for validation issues
- consume enum-backed exception messages

---

# 4. Minimum Acceptance Criteria

A generated exception handler is valid only if:
- it centralizes error mapping
- it uses Problem Details
- it aligns with the response contract
